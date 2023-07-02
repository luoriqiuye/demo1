package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;

@RestController
public class MessageController {

    private final RedisTemplate<String, Integer> redisTemplate;

    @Autowired
    public MessageController(RedisTemplate<String, Integer> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @GetMapping("/sendMsg/{id}")
    public String sendMsg(@PathVariable("id") String id) {
        // 模拟发送短信，将收到的短信数量加1
        redisTemplate.opsForValue().increment(id, 1);
        // 设置过期时间为30天
        redisTemplate.expire(id, Duration.ofDays(30));
        return "{\"success\":true}";
    }

    @GetMapping("/record/{id}")
    public MessageRecord getRecord(@PathVariable("id") String id) {
        // 查询某个用户当前收到的短信数量
        Integer count = redisTemplate.opsForValue().get(id);
        if (count != null) {
            return new MessageRecord(true, id, "张三", count);
        } else {
            return new MessageRecord(false, id, "用户不存在", 0);
        }
        //控制每条短信发送间隔
    }
}