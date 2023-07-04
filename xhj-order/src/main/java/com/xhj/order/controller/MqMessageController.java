package com.xhj.order.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.UUID;


import com.xhj.order.entity.OrderEntity;
import com.xhj.order.entity.OrderReturnReasonEntity;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.xhj.order.entity.MqMessageEntity;
import com.xhj.order.service.MqMessageService;
import com.common.utils.PageUtils;
import com.common.utils.R;



/**
 * 
 *
 * @author xhj
 * @email 2642728859@qq.com
 * @date 2023-02-09 18:40:08
 */
@RestController
@RequestMapping("order/mqmessage")
public class MqMessageController {
    @Autowired
    private MqMessageService mqMessageService;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @GetMapping(value = "/sendMq")
    public String sendMq(@RequestParam(value = "num",defaultValue = "10")Integer num){
        for (int i = 0; i < num; i++) {
            if (i%2==0){
                OrderReturnReasonEntity entity = new OrderReturnReasonEntity();
                entity.setId(1L);
                entity.setName("xxxx");
                entity.setSort(1);
                entity.setStatus(1);
                entity.setCreateTime(new Date());
                rabbitTemplate.convertAndSend("hello-java-exchange","hello.java",entity,new CorrelationData(UUID.randomUUID().toString()));
            }else{
                OrderEntity order = new OrderEntity();
                order.setId(UUID.randomUUID().getLeastSignificantBits());
                rabbitTemplate.convertAndSend("hello-java-exchange","hello1.java",order,new CorrelationData(UUID.randomUUID().toString()));
            }
        }
        return "ok";
    }

    /**
     * 列表
     */
    @RequestMapping("/list")
    //@RequiresPermissions("order:mqmessage:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = mqMessageService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{messageId}")
    //@RequiresPermissions("order:mqmessage:info")
    public R info(@PathVariable("messageId") String messageId){
		MqMessageEntity mqMessage = mqMessageService.getById(messageId);

        return R.ok().put("mqMessage", mqMessage);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    //@RequiresPermissions("order:mqmessage:save")
    public R save(@RequestBody MqMessageEntity mqMessage){
		mqMessageService.save(mqMessage);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    //@RequiresPermissions("order:mqmessage:update")
    public R update(@RequestBody MqMessageEntity mqMessage){
		mqMessageService.updateById(mqMessage);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    //@RequiresPermissions("order:mqmessage:delete")
    public R delete(@RequestBody String[] messageIds){
		mqMessageService.removeByIds(Arrays.asList(messageIds));

        return R.ok();
    }

}
