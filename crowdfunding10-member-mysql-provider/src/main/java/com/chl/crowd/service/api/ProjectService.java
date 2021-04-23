package com.chl.crowd.service.api;

import com.chl.crowd.entity.vo.ProjectVO;

public interface ProjectService {
    void saveProject(ProjectVO projectVO, Integer memberId);
}
