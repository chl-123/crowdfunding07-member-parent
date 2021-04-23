package com.chl.crowd.controller;

import com.chl.crowd.entity.vo.ProjectVO;
import com.chl.crowd.service.api.ProjectService;
import com.chl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RestController
public class ProjectProviderController {
    @Autowired
    private ProjectService projectService;
    @RequestMapping("/save/project/vo/remote")
    ResultEntity<String> saveProjectVORemote(
            @RequestBody ProjectVO projectVO,
            @RequestParam("memberId") Integer memberId
    ){
        try {
            projectService.saveProject(projectVO,memberId);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }


    }
}
