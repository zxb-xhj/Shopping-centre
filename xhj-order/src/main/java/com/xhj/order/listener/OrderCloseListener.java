package com.xhj.order.listener;

import com..common.to.mq.OrderTo;
import com.xhj.order.entity.OrderEntity;
import com.xhj.order.service.OrderService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @Author: xhj
 * @Date: 2023/06/07/19:03
 * @Description:
 */
@Service
@RabbitListener(queues = "order.release.order.queue")
public class OrderCloseListener {

    @Autowired
    OrderService orderService;

    @RabbitHandler
    public void listener(OrderEntity order, Channel channel, Message message) throws IOException {
        System.out.println("收到过期的订单消息，准备关闭订单"+order.getOrderSn());
        // 修改订单状态
        orderService.closeOrder(order);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
    @RabbitHandler
    public void listener(OrderTo orderTo, Channel channel, Message message) throws IOException {
        System.out.println("收到过期的订单消息，准备关闭订单"+orderTo.getOrderSn());
        // 修改订单状态
//        orderService.closeOrder(order);
        channel.basicAck(message.getMessageProperties().getDeliveryTag(),false);
    }
}
