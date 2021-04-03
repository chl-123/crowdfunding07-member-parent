package com.chl.crowd.controller;

import com.chl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;
@RestController
public class RedisController {
    @Autowired
    private StringRedisTemplate redisTemplate;
    @RequestMapping("/set/redis/keyvalue/remote")
    ResultEntity<String> setRedisKeyValueRemote(
            @RequestParam("key") String key,
            @RequestParam("value")String value
    ){
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.append(key, value);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    };
    @RequestMapping("/set/redis/keyvalue/remote/withtimeout")
    ResultEntity<String> setRedisKeyValueRemoteWithTimeOut(
            @RequestParam("key") String key,
            @RequestParam("value")String value,
            @RequestParam("time")long time,
            @RequestParam("timeUnit") TimeUnit timeUnit
    ){
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            operations.set(key, value, time, timeUnit);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
    };
    @RequestMapping("/get/redis/by/stringkey/remote")
    ResultEntity<String> getRedisByStringKeyRemote(
            @RequestParam("key") String key
    ){
        try {
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            String value = operations.get(key);
            return ResultEntity.successWithData(value);
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }
    };

    @RequestMapping("/remove/redis/by/stringkey/remote")
    ResultEntity<String> removeRedisByStringKeyRemote(
            @RequestParam("key") String key
    ){
        try {
            redisTemplate.delete(key);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            return ResultEntity.failed(e.getMessage());
        }

    };

}
