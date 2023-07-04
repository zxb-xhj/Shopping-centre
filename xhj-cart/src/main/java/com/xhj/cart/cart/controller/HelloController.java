package com.xhj.cart.cart.controller;

import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

/**
 * @Author: xhj
 * @Date: 2023/06/20/20:00
 * @Description:
 */
@RestController
public class HelloController {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    StringRedisTemplate redisTemplate;

    @GetMapping("/lock")
    public String lock() throws InterruptedException {
        RLock lock = redissonClient.getLock("my-lock");
        lock.lock();
        Thread.sleep(30000);
        String s = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("my",s);
        lock.unlock();
        return s;
    }

    @GetMapping("/write")
    public String write() throws InterruptedException {
        RReadWriteLock lock = redissonClient.getReadWriteLock("my-wlock");
        RLock rLock = lock.writeLock();
        rLock.lock();
        Thread.sleep(30000);
        String s = UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("my-w",s);
        rLock.unlock();
        return s;
    }

    @GetMapping("/read")
    public String read() throws InterruptedException {
        RReadWriteLock lock = redissonClient.getReadWriteLock("my-wlock");
        RLock rLock = lock.readLock();
        rLock.lock();
        String s = redisTemplate.opsForValue().get("my-w");
        rLock.unlock();
        return s;
    }

    /**
     * 闭锁
     * @return
     * @throws InterruptedException
     */
    @GetMapping("/lockDoor")
    public String lockDoor() throws InterruptedException {
        RCountDownLatch dool = redissonClient.getCountDownLatch("dool");
        dool.trySetCount(5);
        dool.wait(); // 等待闭锁都完成
        return "放假了，锁门..";
    }

    @GetMapping("/gogogo/{id}")
    public String gogogo(@PathVariable("id")Integer id) {
        RCountDownLatch dool = redissonClient.getCountDownLatch("dool");
        dool.countDown(); // 计数减一
        return id+"班走完了。。";
    }

    /**
     * 信号量
     * 可以用作分布式限流
     * @return
     */
    @GetMapping("/park")
    public String park() throws InterruptedException {
        RSemaphore park = redissonClient.getSemaphore("park");
//        park.acquire(); // 获取一个信号量 占位
        boolean b = park.tryAcquire(); // 占位
        return "ok"+b;
    }
    /**
     * 信号量
     * @return
     */
    @GetMapping("/go")
    public String go() throws InterruptedException {
        RSemaphore park = redissonClient.getSemaphore("park");
        park.release(); // 释放一个
        return "ok";
    }

}
