package com.chl.crowd.controller;

import com.chl.crowd.api.MySQLRemoteService;
import com.chl.crowd.api.RedisRemoteService;
import com.chl.crowd.config.ShortMessageConfig;
import com.chl.crowd.constant.CrowdConstant;
import com.chl.crowd.entity.po.Member;

import com.chl.crowd.entity.vo.MemberLoginVO;
import com.chl.crowd.entity.vo.MemberVO;
import com.chl.crowd.util.CrowdUtil;
import com.chl.crowd.util.ResultEntity;
import com.google.common.base.Objects;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.concurrent.TimeUnit;

@Controller
public class

MemberController {
    @Autowired
    ShortMessageConfig shortMessageConfig;
    @Autowired
    private RedisRemoteService redisRemoteService;
    @Autowired
    private MySQLRemoteService mySQLRemoteService;

    @ResponseBody
    @RequestMapping("/auth/member/send/short/message.json")
    public ResultEntity<String> sendShortMessage(@RequestParam(value = "phoneNum") String phoneNum) {
        //发送信息


        ResultEntity<String> sendMessageResultEntity = CrowdUtil.sendMessage(
                shortMessageConfig.getMethod(),
                shortMessageConfig.getAppcode(),
                phoneNum);
        //判断短信发送结果
        if (ResultEntity.SUCCESS.equals(sendMessageResultEntity.getResult())) {
            //获取验证码
            String code = sendMessageResultEntity.getData();
            //拼接key
            String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;
            //调用远程Redis接口，将验证码存入Redis中
            ResultEntity<String> saveCodeResultEntity = redisRemoteService.setRedisKeyValueRemoteWithTimeOut(key, code, 5, TimeUnit.MINUTES);
            if (ResultEntity.SUCCESS.equals(saveCodeResultEntity.getResult())) {
                return ResultEntity.successWithoutData();
            } else {
                return saveCodeResultEntity;
            }
        } else {
            return sendMessageResultEntity;
        }
    }

    @RequestMapping("/auth/do/member/register.html")
    public String doMemberRegister(/*@RequestBody */MemberVO memberVO, ModelMap modelMap) {
        //获取用户输入的手机号
        String phoneNum = memberVO.getPhoneNum();

        //拼redis中存储的验证码的key
        String key = CrowdConstant.REDIS_CODE_PREFIX + phoneNum;

        //从Redis中读取key对应的value
        ResultEntity<String> redisResultEntity = redisRemoteService.getRedisByStringKeyRemote(key);
        //查询是否有效
        String result = redisResultEntity.getResult();
        //如果无效显示错误消息
        if (ResultEntity.FAILED.equals(result)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, redisResultEntity.getMessage());
            return "member-register";
        }
        //判断redis中验证码是否存在
        String redisCode = redisResultEntity.getData();
        if (redisCode == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
            return "member-register";
        }
        //如果redis能够查询到value则比较表单验证码和redis验证码
        String formCode = memberVO.getCode();
        if (!Objects.equal(formCode, redisCode)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_CODE_NOT_EXISTS);
            return "member-register";
        }
        //如果验证码一致就从Redis中删除
        redisRemoteService.removeRedisByStringKeyRemote(key);
        //执行密码加密
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String userpswdBeforeEncode = memberVO.getUserpswd();
        String userpswdAfterEncode = passwordEncoder.encode(userpswdBeforeEncode);
        memberVO.setUserpswd(userpswdAfterEncode);
        //执行保存
        //创建空的Member对象
        Member member = new Member();
        //执行复制
        BeanUtils.copyProperties(memberVO, member);
        //调用远程方法
        ResultEntity<String> saveMemberResultEntity = mySQLRemoteService.saveMember(member);
        if (ResultEntity.FAILED.equals(saveMemberResultEntity.getResult())) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, saveMemberResultEntity.getMessage());
            return "member-register";
        }

        return "redirect:http://localhost:80/auth/member/to/login/page.html";
    }
/*
* 执行登录
* */
    @RequestMapping("/auth/do/member/login.html")
    public String doMemberLogin(
            @RequestParam("loginacct") String loginacct,
            @RequestParam("userpswd") String userpswd,
            ModelMap modelMap,
            HttpSession session
    ) {

        //调用远程接口查询用户是否存在
        ResultEntity<Member> memberByLoginAcctEntity = mySQLRemoteService.getMemberByLoginAcctRemote(loginacct);
        //判断是否成功
        if (ResultEntity.FAILED.equals(memberByLoginAcctEntity.getResult())) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, memberByLoginAcctEntity.getMessage());
            return "member-login";
        }



        Member member = memberByLoginAcctEntity.getData();
        if (member == null) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE, CrowdConstant.MESSAGE_LOGIN_FAILED);
            return "member-login";
        }
        String memberUserpswd = member.getUserpswd();
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        if (!bCryptPasswordEncoder.matches(userpswd, memberUserpswd)) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_LOGIN_PassWordFAILED);
            return "member-login";
        }
        //创建MemberLoginVO对象存入Session域中
        MemberLoginVO memberLoginVO=new MemberLoginVO(member.getId(),member.getUsername(),member.getEmail());
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER,memberLoginVO);

        return "redirect:http://localhost:80/auth/member/to/center/page.html";


    }
    /*
    * 执行退出登录
    * */
    @RequestMapping("/auth/member/do/loginout.html")
    public String doLoginout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }


}
