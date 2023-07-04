package com.xhj.order;


import com.xhj.order.entity.OrderEntity;
import com.xhj.order.entity.OrderReturnReasonEntity;
import com.xhj.order.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
class XhjOrderApplicationTests {

    @Autowired
    OrderService omsOrderService;
    @Autowired
    AmqpAdmin amqpAdmin;
    @Autowired
    RabbitTemplate rabbitTemplate;

    @Test
    void contextLoads() {
        List<OrderEntity> list = omsOrderService.list(new QueryWrapper<OrderEntity>().eq("id", 1L));
        list.forEach((order)->{
            System.out.println(order);
        });

    }


    /**
     * 创建交换机
     */
    @Test
    void createExchane(){
        // name:交换机名称  durable：是否持久化  autoDelete：是否自动删除  arguments：数据
        //DirectExchange(String name, boolean durable, boolean autoDelete, Map<String, Object> arguments
        DirectExchange directExchange = new DirectExchange("hello-java-exchange",true,false);
        amqpAdmin.declareExchange(directExchange);
        log.info("hello-java-exchange交换机创建成功");
    }

    /**
     * 创建队列
     */
    @Test
    void createQueue(){
        // name:名称  durable：是否持久化 exclusive:排他  autoDelete：是否自动删除  arguments：数据
        //Queue(String name, boolean durable, boolean exclusive, boolean autoDelete, @Nullable Map<String, Object> arguments)
        Queue queue = new Queue("hello-java-queue",true,false,false);
        amqpAdmin.declareQueue(queue);
        log.info("hello-java-queue队列创建成功");
    }

    /**
     * 创建绑定
     */
    @Test
    void createBinding(){
        // destination:目的地队列/交换机  destinationType：目的地类型  exchange:交换机  routingKey：路由键  arguments：数据
        //Binding(String destination, Binding.DestinationType destinationType, String exchange, String routingKey, @Nullable Map<String, Object> arguments
        Binding binding = new Binding("hello-java-queue", Binding.DestinationType.QUEUE,
                "hello-java-exchange","hello.java",null);
        amqpAdmin.declareBinding(binding);
        log.info("hello.java绑定创建成功");
    }

    /**
     * 发送消息
     */
    @Test
    void sendMessage(){
        OrderReturnReasonEntity entity = new OrderReturnReasonEntity();
        entity.setId(1L);
        entity.setName("xxxx");
        entity.setSort(1);
        entity.setStatus(1);
        entity.setCreateTime(new Date());
        rabbitTemplate.convertAndSend("hello-java-exchange","hello.java",entity);
    }
}


