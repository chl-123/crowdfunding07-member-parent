package com.chl.crowd.api;

import com.chl.crowd.entity.po.Member;

import com.chl.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("chl-crowd-mysql")
public interface MySQLRemoteService {
    @RequestMapping("/get/member/by/loginacct/remote")
    ResultEntity<Member> getMemberByLoginAcctRemote(@RequestParam("loginacct")String loginacct);
    @RequestMapping("/save/member/remote")
    ResultEntity<String> saveMember(@RequestBody Member member);
}
