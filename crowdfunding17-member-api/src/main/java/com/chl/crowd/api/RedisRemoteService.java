package com.chl.crowd.api;

import com.chl.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.concurrent.TimeUnit;

@FeignClient("chl-crowd-redis")
public interface RedisRemoteService {
    @RequestMapping("/set/redis/keyvalue/remote")
    ResultEntity<String> setRedisKeyValueRemote(
            @RequestParam("key") String key,
            @RequestParam("value")String value
    );
    @RequestMapping("/set/redis/keyvalue/remote/withtimeout")
    ResultEntity<String> setRedisKeyValueRemoteWithTimeOut(
            @RequestParam("key") String key,
            @RequestParam("value")String value,
            @RequestParam("time")long time,
            @RequestParam("timeUnit")TimeUnit timeUnit
    );
    @RequestMapping("/get/redis/by/stringkey/remote")
    ResultEntity<String> getRedisByStringKeyRemote(@RequestParam("key") String key);

    @RequestMapping("/remove/redis/by/stringkey/remote")
    ResultEntity<String> removeRedisByStringKeyRemote(@RequestParam("key") String key);
}
