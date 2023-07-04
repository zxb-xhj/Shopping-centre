package com.xhj.coupon.controller;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


import com.xhj.coupon.entity.vo.SeckillSessionEntityVo;
import com.xhj.coupon.entity.SeckillSessionEntity;
import com.xhj.coupon.service.SeckillSessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.common.utils.PageUtils;
import com.common.utils.R;



/**
 * 秒杀活动场次
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-09 18:34:50
 */
@RestController
@Slf4j
@RequestMapping("coupon/seckillsession")
public class SeckillSessionController {
    @Autowired
    private SeckillSessionService seckillSessionService;


    /**
     * 查询最近三天需要参加秒杀商品的信息
     * @return
     */
    @GetMapping(value = "/Lates3DaySession")
    public R getLates3DaySession() {

        List<SeckillSessionEntity> seckillSessionEntities = seckillSessionService.getLates3DaySession();

        return R.ok().setData(seckillSessionEntities);
    }
    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("coupon:seckillsession:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = seckillSessionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    //@RequiresPermissions("coupon:seckillsession:info")
    public R info(@PathVariable("id") Long id){
		SeckillSessionEntity seckillSession = seckillSessionService.getById(id);

        return R.ok().put("seckillSession", seckillSession);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("coupon:seckillsession:save")
    public R save(@RequestBody SeckillSessionEntityVo seckillSessionVo){
        SeckillSessionEntity seckillSession = new SeckillSessionEntity();
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        seckillSession.setName(seckillSessionVo.getName());
        seckillSession.setStatus(seckillSessionVo.getStatus());
        seckillSession.setCreateTime(sdf.format(seckillSessionVo.getCreateTime()));
        seckillSession.setEndTime(sdf.format(seckillSessionVo.getEndTime()));
        seckillSession.setStartTime(sdf.format(seckillSessionVo.getStartTime()));
		seckillSessionService.save(seckillSession);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("coupon:seckillsession:update")
    public R update(@RequestBody SeckillSessionEntity seckillSession){
		seckillSessionService.updateById(seckillSession);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("coupon:seckillsession:delete")
    public R delete(@RequestBody Long[] ids){
		seckillSessionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

}
