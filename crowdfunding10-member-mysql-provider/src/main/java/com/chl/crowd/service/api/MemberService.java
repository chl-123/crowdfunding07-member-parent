package com.chl.crowd.service.api;

import com.chl.crowd.entity.po.Member;


public interface MemberService {

    Member getMemberByLoginAcct(String loginacct);

    void savaMember(Member member);
}
