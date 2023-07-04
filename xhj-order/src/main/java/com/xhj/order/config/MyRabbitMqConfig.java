package com.xhj.order.config;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

/**
 * @Author: xhj
 * @Date: 2023/06/01/16:03
 * @Description:
 */
@Configuration
public class MyRabbitMqConfig {

    @Autowired
    RabbitTemplate rabbitTemplate;

    /**
     * 设置rabbit序列化
     * @return
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @PostConstruct  // MyRabbitMqConfig对象创建完之后，执行这个方法
    public void initRabbitTemplate(){
        // 设置确认回调
        rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
            /**
             *
             * @param correlationData 当前消息唯一关联的数据，（消息唯一的id）
             * @param ack 消息是否成功
             * @param cause 失败的原因
             */
            @Override
            public void confirm(CorrelationData correlationData, boolean ack, String cause) {
                System.out.println("correlationData: "+correlationData+" ack: "+ ack
                +"cause: "+cause);
            }
        });
        // 设置消息抵达队列的回调
        rabbitTemplate.setReturnCallback(new RabbitTemplate.ReturnCallback() {
            /**
             * 只要消息没有投递给指定的队列，就会触发这个回调
             * @param message 投递失败的详细消息
             * @param replyCode 回复的状态码
             * @param replyText 回复的本本内容
             * @param exchange 当时这个消息发给哪个交换机
             * @param routingkey 用的是哪个路由键
             */
            @Override
            public void returnedMessage(Message message, int replyCode, String replyText, String exchange,
                                        String routingkey) {
                System.out.println("message: "+message+" replyCode: "+replyCode+" replyText: "+replyText+" exchange: "+exchange+
                        " routingkey: "+routingkey);
            }
        });
    }

}
