package com.chl.crowd.api;

import com.chl.crowd.entity.Member;
import com.chl.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("chl-crowd-mysql")
public interface MySQLRemoteService {
    @RequestMapping("/get/member/by/loginacct/remote")
    ResultEntity<Member> getMemberByLoginAcctRemote(@RequestParam("loginacct")String loginacct);
}
