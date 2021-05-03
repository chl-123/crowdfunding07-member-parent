package com.chl.crowd.service.api;

import com.chl.crowd.entity.vo.DetailProjectVO;
import com.chl.crowd.entity.vo.PortalTypeVO;
import com.chl.crowd.entity.vo.ProjectVO;

import java.util.List;

public interface ProjectService {
    void saveProject(ProjectVO projectVO, Integer memberId);
    List<PortalTypeVO> getPortalTypeVO();
    DetailProjectVO getDetailProjectVO(Integer projectId);
}
