package com.chl.crowd.service.api;

import com.chl.crowd.entity.Member;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


public interface MemberService {

    Member getMemberByLoginAcct(String loginacct);

    void savaMember(Member member);
}
