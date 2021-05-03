package com.chl.crowd.api;

import com.chl.crowd.entity.po.Member;

import com.chl.crowd.entity.vo.DetailProjectVO;
import com.chl.crowd.entity.vo.PortalTypeVO;
import com.chl.crowd.entity.vo.ProjectVO;
import com.chl.crowd.util.ResultEntity;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("chl-crowd-mysql")
public interface MySQLRemoteService {
    @RequestMapping("/get/member/by/loginacct/remote")
    ResultEntity<Member> getMemberByLoginAcctRemote(@RequestParam("loginacct")String loginacct);
    @RequestMapping("/save/member/remote")
    ResultEntity<String> saveMember(@RequestBody Member member);
    @RequestMapping("/save/project/vo/remote")
    ResultEntity<String> saveProjectVORemote(@RequestBody ProjectVO projectVO, @RequestParam("memberId") Integer memberId);
    @RequestMapping("/get/portalType/project/date/remote")
    public ResultEntity<List<PortalTypeVO>> getPortalTypeProjectDataRemote();

    @RequestMapping("/get/project/detail/remote/{projectId}")
     ResultEntity<DetailProjectVO> getDetailProjectVORemote(@PathVariable("projectId")Integer projectId);
}
