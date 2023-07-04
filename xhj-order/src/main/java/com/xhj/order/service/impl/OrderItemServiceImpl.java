package com.xhj.order.service.impl;

import com.xhj.order.entity.OrderEntity;
import com.xhj.order.entity.OrderReturnReasonEntity;
import com.rabbitmq.client.Channel;
import com.xhj.order.dao.OrderItemDao;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com..common.utils.PageUtils;
import com..common.utils.Query;

import com.xhj.order.entity.OrderItemEntity;
import com.xhj.order.service.OrderItemService;


@Service("orderItemService")
@RabbitListener(queues = {"hello-java-queue"})
public class OrderItemServiceImpl extends ServiceImpl<OrderItemDao, OrderItemEntity> implements OrderItemService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<OrderItemEntity> page = this.page(
                new Query<OrderItemEntity>().getPage(params),
                new QueryWrapper<OrderItemEntity>()
        );

        return new PageUtils(page);
    }

    @RabbitHandler
    public void recieveMessage(Message message, OrderReturnReasonEntity orderReturnReason,Channel channel){
        byte[] body = message.getBody();
        System.out.println("请求体"+body+" 内容："+orderReturnReason);
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        // long deliveryTag  标签, boolean myltiple 批量, boolean requeue 入队
        try {
            // 手动收货 非批量
            if (deliveryTag %2 == 0){
                channel.basicAck(deliveryTag,false);
                System.out.println("收货 "+deliveryTag);
            }else {
                // 退货
                channel.basicNack(deliveryTag,false,false);
                System.out.println("退货 "+deliveryTag);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RabbitHandler
    public void recieveMessage2(Message message, OrderEntity order, Channel channel){
        byte[] body = message.getBody();
        System.out.println("请求体"+body+" 内容："+order);

    }

}