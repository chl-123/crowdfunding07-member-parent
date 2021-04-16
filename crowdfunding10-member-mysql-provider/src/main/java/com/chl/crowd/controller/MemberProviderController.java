package com.chl.crowd.controller;

import com.chl.crowd.constant.CrowdConstant;
import com.chl.crowd.entity.po.Member;
import com.chl.crowd.service.api.MemberService;
import com.chl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.RequestBody;
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
    @RequestMapping("/save/member/remote")
    /*
    @RequestBody如果不加，这通过consumer调用时会出问题
    * */
    public ResultEntity<String> saveMember(@RequestBody Member member){

        try {
            memberService.savaMember(member);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            //如果抛键值重复异常，则返回账号已被使用的提示信息
            if (e instanceof DuplicateKeyException) {
                return ResultEntity.failed(CrowdConstant.MESSAGE_LOGIN_ACCT_ALREADY_IN_USE);
            }
            return ResultEntity.failed(e.getMessage());
        }
    }
}
