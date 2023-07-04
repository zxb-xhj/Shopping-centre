package com.xhj.order.web;

import com.xhj.order.entity.OrderEntity;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.UUID;

/**
 * @Author: xhj
 * @Date: 2023/06/03/11:14
 * @Description:
 */
@Controller
public class HelloController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @ResponseBody
    @GetMapping("/test/orderMq")
    public String listener(){
        OrderEntity order = new OrderEntity();
        order.setOrderSn(UUID.randomUUID().toString());
        order.setCreateTime(new Date());
        rabbitTemplate.convertAndSend("order-event-exchange","order.create.order",order);
        return "ok";
    }

    @GetMapping("/{page}.html")
    public String listPage(@PathVariable("page") String page){
        return page;
    }
}
