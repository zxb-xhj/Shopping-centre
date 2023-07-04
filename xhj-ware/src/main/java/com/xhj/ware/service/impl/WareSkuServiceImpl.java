package com.xhj.ware.service.impl;

import com.alibaba.fastjson.TypeReference;
import com.common.exception.NoStockException;
import com.common.to.SkuHasStockTo;
import com.common.to.mq.OrderTo;
import com.common.to.mq.StockDetailTo;
import com.common.to.mq.StockLockedTo;
import com.common.utils.R;
import com.xhj.ware.entity.WareOrderTaskDetailEntity;
import com.xhj.ware.entity.WareOrderTaskEntity;
import com.xhj.ware.feign.OrderFeignService;
import com.xhj.ware.service.WareOrderTaskDetailService;
import com.xhj.ware.service.WareOrderTaskService;
import com.xhj.ware.vo.OrderItemVo;
import com.xhj.ware.vo.OrderVo;
import com.xhj.ware.vo.WareSkuLockVo;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.Data;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.common.utils.PageUtils;
import com.common.utils.Query;

import com.xhj.ware.dao.WareSkuDao;
import com.xhj.ware.entity.WareSkuEntity;
import com.xhj.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    WareOrderTaskService wareOrderTaskService;

    @Autowired
    WareOrderTaskDetailService wareOrderTaskDetailService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    OrderFeignService orderFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        /**
         * skuId: 1
         * wareId: 2
         */
        QueryWrapper<WareSkuEntity> queryWrapper = new QueryWrapper<>();
        String skuId = (String) params.get("skuId");
        if(!StringUtils.isEmpty(skuId)){
            queryWrapper.eq("sku_id",skuId);
        }

        String wareId = (String) params.get("wareId");
        if(!StringUtils.isEmpty(wareId)){
            queryWrapper.eq("ware_id",wareId);
        }


        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                queryWrapper
        );

        return new PageUtils(page);
    }

    @Override
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
//        //1、判断如果还没有这个库存记录新增
//        List<WareSkuEntity> entities = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
//        if(entities == null || entities.size() == 0){
//            WareSkuEntity skuEntity = new WareSkuEntity();
//            skuEntity.setSkuId(skuId);
//            skuEntity.setStock(skuNum);
//            skuEntity.setWareId(wareId);
//            skuEntity.setStockLocked(0);
//            //TODO 远程查询sku的名字，如果失败，整个事务无需回滚
//            //1、自己catch异常
//            //TODO 还可以用什么办法让异常出现以后不回滚？高级
//            try {
//                R info = productFeignService.info(skuId);
//                Map<String,Object> data = (Map<String, Object>) info.get("skuInfo");
//
//                if(info.getCode() == 0){
//                    skuEntity.setSkuName((String) data.get("skuName"));
//                }
//            }catch (Exception e){
//
//            }
//
//
//            wareSkuDao.insert(skuEntity);
//        }else{
//            wareSkuDao.addStock(skuId,wareId,skuNum);
        }

    @Override
    public List<SkuHasStockTo> getSkusHasStock(List<Long> skuIds) {
        List<SkuHasStockTo> skuHasStockTos = skuIds.stream().map(skuId -> {
            SkuHasStockTo skuHasStockTo = new SkuHasStockTo();
            Map<String, Object> maps = this.getMap(new QueryWrapper<WareSkuEntity>()
                    .select("SUM(stock-stock_locked) as stocksum").eq("sku_id", skuId));
            if (maps != null){
                skuHasStockTo.setHasStock(Integer.parseInt(maps.get("stocksum").toString()) >0);
            }else {
                skuHasStockTo.setHasStock(false);
            }
            skuHasStockTo.setSkuId(skuId);
            return skuHasStockTo;
        }).collect(Collectors.toList());
        return skuHasStockTos;
    }

    /**
     * 为某个订单锁定库存
     * @param vo
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean orderLockStock(WareSkuLockVo vo) {
        /**
         * 保存库存工作单详情信息
         * 追溯
         */
        WareOrderTaskEntity wareOrderTaskEntity = new WareOrderTaskEntity();
        wareOrderTaskEntity.setOrderSn(vo.getOrderSn());
        wareOrderTaskEntity.setCreateTime(new Date());
        wareOrderTaskService.save(wareOrderTaskEntity);
        //1、按照下单的收货地址，找到一个就近仓库，锁定库存
        //2、找到每个商品在哪个仓库都有库存
        List<OrderItemVo> locks = vo.getLocks();
        List<SkuWareHasStock> collect = locks.stream().map((item) -> {
            SkuWareHasStock stock = new SkuWareHasStock();
            Long skuId = item.getSkuId();
            stock.setSkuId(skuId);
            stock.setNum(item.getCount());
            //查询这个商品在哪个仓库有库存
            List<Long> wareIdList = this.listWareIdHasSkuStock(skuId);
            stock.setWareId(wareIdList);

            return stock;
        }).collect(Collectors.toList());

        //2、锁定库存
        for (SkuWareHasStock hasStock : collect) {
            boolean skuStocked = false;
            Long skuId = hasStock.getSkuId();
            List<Long> wareIds = hasStock.getWareId();

            if (org.springframework.util.StringUtils.isEmpty(wareIds)) {
                //没有任何仓库有这个商品的库存
                throw new NoStockException(skuId);
            }

            //1、如果每一个商品都锁定成功,将当前商品锁定了几件的工作单记录发给MQ
            //2、锁定失败。前面保存的工作单信息都回滚了。发送出去的消息，即使要解锁库存，由于在数据库查不到指定的id，所有就不用解锁
            for (Long wareId : wareIds) {
                //锁定成功就返回1，失败就返回0
                Long count = this.lockSkuStock(skuId,wareId,hasStock.getNum());
                if (count == 1){
                    skuStocked = true;
                    WareOrderTaskDetailEntity taskDetailEntity = WareOrderTaskDetailEntity.builder()
                            .skuId(skuId)
                            .skuName("")
                            .skuNum(hasStock.getNum())
                            .taskId(wareOrderTaskEntity.getId())
                            .wareId(wareId)
                            .lockStatus(1)
                            .build();
                    wareOrderTaskDetailService.save(taskDetailEntity);

                    //TODO 告诉MQ库存锁定成功
                    StockLockedTo lockedTo = new StockLockedTo();
                    lockedTo.setId(wareOrderTaskEntity.getId());
                    StockDetailTo detailTo = new StockDetailTo();
                    BeanUtils.copyProperties(taskDetailEntity,detailTo);
                    lockedTo.setDetailTo(detailTo);
                    rabbitTemplate.convertAndSend("stock-event-exchange","stock.locked",lockedTo);
                    break;
                }else {
                    //当前仓库锁失败，重试下一个仓库
                }
            }
            if (skuStocked == false) {
                //当前商品所有仓库都没有锁住
                throw new NoStockException(skuId);
            }
        }

        //3、肯定全部都是锁定成功的
        return true;
    }

    @Override
    public void unlockStock(StockLockedTo to) {
        //库存工作单的id
        StockDetailTo detail = to.getDetailTo();
        Long detailId = detail.getId();

        /**
         * 解锁
         * 1、查询数据库关于这个订单锁定库存信息
         *   有：证明库存锁定成功了
         *      解锁：订单状况
         *          1、没有这个订单，必须解锁库存
         *          2、有这个订单，不一定解锁库存
         *              订单状态：已取消：解锁库存
         *                      已支付：不能解锁库存
         */
        WareOrderTaskDetailEntity taskDetailInfo = wareOrderTaskDetailService.getById(detailId);
        if (taskDetailInfo != null) {
            //查出wms_ware_order_task工作单的信息
            Long id = to.getId();
            WareOrderTaskEntity orderTaskInfo = wareOrderTaskService.getById(id);
            //获取订单号查询订单状态
            String orderSn = orderTaskInfo.getOrderSn();
            //远程查询订单信息
            R orderData = orderFeignService.getOrderStatus(orderSn);
            if (orderData.getCode() == 0) {
                //订单数据返回成功
                OrderVo orderInfo = orderData.getData("data", new TypeReference<OrderVo>() {});

                //判断订单状态是否已取消或者支付或者订单不存在
                if (orderInfo == null || orderInfo.getStatus() == 4) {
                    //订单已被取消，才能解锁库存
                    if (taskDetailInfo.getLockStatus() == 1) {
                        //当前库存工作单详情状态1，已锁定，但是未解锁才可以解锁
                        unLockStock(detail.getSkuId(),detail.getWareId(),detail.getSkuNum(),detailId);
                    }
                }
            } else {
                //消息拒绝以后重新放在队列里面，让别人继续消费解锁
                //远程调用服务失败
                throw new RuntimeException("远程调用服务失败");
            }
        } else {
            //无需解锁
        }
    }

    @Transactional
    @Override
    public void unlockStock(OrderTo orderTo) {
        WareOrderTaskEntity orderTask = wareOrderTaskService.getOne(new QueryWrapper<WareOrderTaskEntity>().eq("order_sn", orderTo.getOrderSn()));
        List<WareOrderTaskDetailEntity> list = wareOrderTaskDetailService.list(
                new QueryWrapper<WareOrderTaskDetailEntity>()
                        .eq("task_id", orderTask.getId())
                        .eq("lock_status", 1));
        if (list!=null&& list.size()>0){
            for (WareOrderTaskDetailEntity entity : list) {
                lockSkuStock(entity.getSkuId(), entity.getWareId(), entity.getSkuNum());
            }
        }
    }

    /**
     * 解锁库存的方法
     * @param skuId
     * @param wareId
     * @param num
     * @param taskDetailId
     */
    public void unLockStock(Long skuId,Long wareId,Integer num,Long taskDetailId) {

        //库存解锁
//        wareSkuDao.unLockStock(skuId,wareId,num);
        this.update(new UpdateWrapper<WareSkuEntity>().setSql("stock_locked = stock_locked -"+num)
                .eq("sku_id",skuId).eq("ware_id",wareId));

        //更新工作单的状态
        WareOrderTaskDetailEntity taskDetailEntity = new WareOrderTaskDetailEntity();
        taskDetailEntity.setId(taskDetailId);
        //变为已解锁
        taskDetailEntity.setLockStatus(2);
        wareOrderTaskDetailService.updateById(taskDetailEntity);
    }

    private Long lockSkuStock(Long skuId, Long wareId, Integer num) {
        UpdateWrapper<WareSkuEntity> wrapper = new UpdateWrapper<WareSkuEntity>()
                .setSql("stock_locked=stock_locked-"+num)
                .eq("sku_id", skuId).eq("ware_id", wareId)
                .ge("stock-stock_locked", num);
        Long update = this.update(wrapper)?1L:0L;
        return update;
    }

    private List<Long> listWareIdHasSkuStock(Long skuId) {
        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("sku_id",skuId)
                .gt("stock-stock_locked","0");
        List<Long> longList = this.list(wrapper).stream().map(wareSkuEntity -> wareSkuEntity.getWareId()).collect(Collectors.toList());
        return longList;
    }


    @Data
    class SkuWareHasStock {
        private Long skuId;
        private Integer num;
        private List<Long> wareId;
    }
}