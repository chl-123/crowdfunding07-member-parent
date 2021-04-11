package com.chl.crowd.service.impl;

import com.chl.crowd.entity.Member;
import com.chl.crowd.entity.MemberExample;
import com.chl.crowd.mapper.MemberMapper;
import com.chl.crowd.service.api.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        List<Member> members = memberMapper.selectByExample(memberExample);
        if (members == null|| members.size()==0) {
            return null;
        }
        
        return  members.get(0);
    }
    /*
    * 添加事务管理
    * */
    @Transactional(propagation = Propagation.REQUIRES_NEW,
                    rollbackFor = Exception.class,
                    readOnly = false
    )
    public void savaMember(Member member) {
        memberMapper.insert(member);
    }
}
