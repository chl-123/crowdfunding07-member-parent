package com.chl.crowd.service.impl;

import com.chl.crowd.entity.po.MemberConfirmInfoPO;
import com.chl.crowd.entity.po.MemberLaunchInfoPO;
import com.chl.crowd.entity.po.ProjectPO;
import com.chl.crowd.entity.po.RewordPO;
import com.chl.crowd.entity.vo.*;
import com.chl.crowd.mapper.*;
import com.chl.crowd.service.api.ProjectService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Transactional(readOnly = true)
@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectPOMapper projectPOMapper;

    @Autowired
    private ProjectItemPicPOMapper projectItemPicPOMapper;

    @Autowired
    private MemberLaunchInfoPOMapper memberLaunchInfoPOMapper;

    @Autowired
    private MemberConfirmInfoPOMapper memberConfirmInfoPOMapper;

    @Autowired
    private RewordPOMapper rewordPOMapper;

    @Transactional(readOnly = false,propagation = Propagation.REQUIRES_NEW,rollbackFor = Exception.class)
    public void saveProject(ProjectVO projectVO, Integer memberId) {
         //保存ProjectPO对象
        //创建ProjectPO对象
        ProjectPO projectPO=new ProjectPO();
        //复制projectVO中的属性到projectPO中
        BeanUtils.copyProperties(projectVO,projectPO);

        projectPO.setMemberid(memberId);
        //生成创建时间
        String createdate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        projectPO.setCreatedate(createdate);
        //status 设置成0，表示项目即将开始
        projectPO.setStatus(0);
        //保存projectPO
        //需要设置xml中相关属性<insert id="insertSelective" useGeneratedKeys="true" keyProperty="id" parameterType="com.chl.crowd.entity.po.ProjectPO" >
        //useGeneratedKeys="true" keyProperty="id
        projectPOMapper.insertSelective(projectPO);
        //从projectPO对象中获取自增的主键


        Integer projectId = projectPO.getId();


        //保存项目、分类的关联信息
        //从ProjectVO对象中获取typeIdList
        List<Integer> typeIdList = projectVO.getTypeIdList();
        projectPOMapper.insertTypeRelationship(typeIdList,projectId);

        //保存项目、标签关联信息

        List<Integer> tagIdList = projectVO.getTagIdList();
        projectPOMapper.insertTagRelationship(tagIdList,projectId);

        //保存项目中详情图片的路径信息
        List<String> detailPicturePathList = projectVO.getDetailPicturePathList();
        projectItemPicPOMapper.insertDetailPicturePathList(projectId,detailPicturePathList);

        //保存项目发起人信息
        MemberLauchInfoVO memberLauchInfoVO = projectVO.getMemberLauchInfoVO();
        MemberLaunchInfoPO memberLaunchInfoPO = new MemberLaunchInfoPO();
        BeanUtils.copyProperties(memberLauchInfoVO,memberLaunchInfoPO);
        memberLaunchInfoPO.setMemberid(memberId);
        memberLaunchInfoPOMapper.insert(memberLaunchInfoPO);

        //保存回报信息
        List<ReturnVO> returnVOList = projectVO.getReturnVOList();
        List<RewordPO> rewordPOList=new ArrayList<RewordPO>();
        for (ReturnVO  returnVO: returnVOList) {
            RewordPO rewordPO = new RewordPO();
            BeanUtils.copyProperties(returnVO,rewordPO);
            rewordPOList.add(rewordPO);
        }
        rewordPOMapper.insertRewordPOBatch(projectId,rewordPOList);


        //保存项目确认信息
        MemberConfirmInfoVO memberConfirmInfoVO = projectVO.getMemberConfirmInfoVO();
        MemberConfirmInfoPO memberConfirmInfoPO = new MemberConfirmInfoPO();
        BeanUtils.copyProperties(memberConfirmInfoVO,memberConfirmInfoPO);
        memberConfirmInfoPO.setMemberid(memberId);
        memberConfirmInfoPOMapper.insert(memberConfirmInfoPO);

    }

    public List<PortalTypeVO> getPortalTypeVO() {
        return projectPOMapper.selectPortalTypeVOList();
    }

    public DetailProjectVO getDetailProjectVO(Integer projectId) {
        // 查询获得对象
        DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);
        //根据status确定statusText
        Integer status = detailProjectVO.getStatus();
        switch (status){
            case 0:
                detailProjectVO.setStatusText("审核中");
                break;
            case 1:
                detailProjectVO.setStatusText("众筹中");
                break;
            case 2:
                detailProjectVO.setStatusText("众筹成功");
                break;
            case 3:
                detailProjectVO.setStatusText("已关闭");
                break;

        }
        //根据deployDate计算lastDay
        String deployDate = detailProjectVO.getDeployDate();
        //获取当前日期
        Date CurrentDate = new Date();
        //把众筹日期解析成Date类型
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date deployParseDate = format.parse(deployDate);
            //获取当前日期的时间戳
            long currentDateTimeStamp = CurrentDate.getTime();
            //获取众筹日期的时间戳
            long deployDateTimeStamp = deployParseDate.getTime();
            long pastDays = (currentDateTimeStamp - deployDateTimeStamp)/1000/60/60/24;
            //使用总的众筹天数减去已经过去的时间
            Integer totalDays = detailProjectVO.getDay();
            Integer lastDays =(int) (totalDays - pastDays);
            detailProjectVO.setLastDay(lastDays);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return detailProjectVO;
    }
}
