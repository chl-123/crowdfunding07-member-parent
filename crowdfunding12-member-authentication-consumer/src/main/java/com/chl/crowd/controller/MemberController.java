package com.chl.crowd.controller;

import com.chl.crowd.api.RedisRemoteService;
import com.chl.crowd.config.ShortMessageConfig;
import com.chl.crowd.constant.CrowdConstant;
import com.chl.crowd.util.CrowdUtil;
import com.chl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.concurrent.TimeUnit;

@Controller
public class MemberController {
    @Autowired
    ShortMessageConfig shortMessageConfig;
    @Autowired
    private RedisRemoteService redisRemoteService;
    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendShortMessage(@RequestParam("phoneNum")String phoneNum){
        //发送信息

        ResultEntity<String> sendMessageResultEntity = CrowdUtil.sendMessage(
                shortMessageConfig.getMethod(),
                shortMessageConfig.getAppcode(),
                phoneNum);
        //判断短信发送结果
        if(ResultEntity.SUCCESS.equals(sendMessageResultEntity.getResult())){
            //获取验证码
            String code = sendMessageResultEntity.getData();
            //拼接key
            String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
            //调用远程Redis接口，将验证码存入Redis中
            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeOut(key, code, 5, TimeUnit.MINUTES);
            if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult()) ) {
                return ResultEntity.successWithoutData();
            }else {
                return saveCodeResultEntity;
            }
        }else {
           return sendMessageResultEntity;
        }
    }

}
