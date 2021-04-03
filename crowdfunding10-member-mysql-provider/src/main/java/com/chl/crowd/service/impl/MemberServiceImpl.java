package com.chl.crowd.service.impl;

import com.chl.crowd.entity.Member;
import com.chl.crowd.entity.MemberExample;
import com.chl.crowd.mapper.MemberMapper;
import com.chl.crowd.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberMapper memberMapper;

    public Member getMemberByLoginAcct(String loginacct) {
        //创建Example对象
        MemberExample memberExample=new MemberExample();
        //创建Criteria对象
        MemberExample.Criteria criteria=memberExample.createCriteria();
        //封装查询条件
        criteria.andLoginacctEqualTo(loginacct);

        return  memberMapper.selectByExample(memberExample).get(0);
    }
}
