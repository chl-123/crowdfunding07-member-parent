package com.chl.crowd.controller;

import com.chl.crowd.entity.Member;
import com.chl.crowd.service.api.MemberService;
import com.chl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberProviderController {
    @Autowired
    private MemberService memberService;

    @RequestMapping("/get/member/by/loginacct/remote")
    public ResultEntity<Member> getMemberByLoginAcctRemote(@RequestParam("loginacct")String loginacct){
        try {
            //调用本地Service完成查询
            Member member=memberService.getMemberByLoginAcct(loginacct);
            return ResultEntity.successWithData(member);
        }catch (Exception e){
            //如果有异常返回异常信息
            return ResultEntity.failed(e.getMessage());
        }
    }
}
