package com.chl.crowd.controller;

import com.chl.crowd.api.MySQLRemoteService;
import com.chl.crowd.constant.CrowdConstant;
import com.chl.crowd.entity.vo.PortalTypeVO;
import com.chl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class PortalController {
    @Autowired
    private MySQLRemoteService mySQLRemoteService;
    @RequestMapping("/")
    public String showPortalPage(Model model){
        //调用MySQLRemoteService提供的方法查询首页要显示的数据
        ResultEntity<List<PortalTypeVO>> resultEntity = mySQLRemoteService.getPortalTypeProjectDataRemote();
        //检查查询结果
        String result = resultEntity.getResult();
        if (ResultEntity.SUCCESS.equals(result)) {
            //获取查询结果
            List<PortalTypeVO> list = resultEntity.getData();
            model.addAttribute(CrowdConstant.ATTR_NAME_PORTAL_DATA,list);
        }
        return "portal";
    }

}
