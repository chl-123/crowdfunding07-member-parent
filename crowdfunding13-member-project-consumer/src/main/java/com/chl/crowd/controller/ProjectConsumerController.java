package com.chl.crowd.controller;

import com.chl.crowd.api.MySQLRemoteService;
import com.chl.crowd.config.FileResponseData;
import com.chl.crowd.config.OSSProperties;
import com.chl.crowd.constant.CrowdConstant;
import com.chl.crowd.entity.po.Member;
import com.chl.crowd.entity.vo.MemberConfirmInfoVO;
import com.chl.crowd.entity.vo.MemberLoginVO;
import com.chl.crowd.entity.vo.ProjectVO;
import com.chl.crowd.entity.vo.ReturnVO;
import com.chl.crowd.util.CrowdUtil;
import com.chl.crowd.util.ResultEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class ProjectConsumerController {
    @Autowired
    private OSSProperties ossProperties;
    FileObjectController fileObjectController=new FileObjectController();
    @Autowired
    MySQLRemoteService mySQLRemoteService;
    @RequestMapping(value = "/create/project/information/page.html",method = RequestMethod.POST)
    public String saveProjectBasicInfo(
            // 接收除了上传图片之外的其他普通数据
            ProjectVO projectVO,
            //接收上传的头图
            @RequestBody MultipartFile headerPicture,
            //接收上传的详情的图片
            @RequestBody List<MultipartFile> detailPictureList,
            // 用来将收集了一部分数据的 ProjectVO 对象存入 Session 域
            HttpSession session,
            //用来在当前操作失败后返回上一个表单页面时携带的提示消息
            ModelMap modelMap,
            HttpServletRequest request
    ) throws IOException {

        boolean issuccess;
        //完成头图的上传
        //获取当前headerPicture对象是否为空
        boolean headerPictureEmpty = headerPicture.isEmpty();
        if (headerPictureEmpty) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADER_PIC_EMPTY);
            return "project-launch";
        }

        FileResponseData fileResponseData = fileObjectController.uploadImageSample(headerPicture, request);
        issuccess = fileResponseData.isSuccess();
        if (issuccess) {
            String filePath = fileResponseData.getFilePath();
            System.out.println(filePath);
            projectVO.setHeaderPicturePath(filePath);
        }else {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);
            return "project-launch";
        }
        //上传详情图片
        List<String> detailPicturePathList = new ArrayList<String>();
        if (detailPictureList == null||detailPictureList.size()==0) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
            return "project-launch";
        }
        for (MultipartFile detailPicture: detailPictureList) {
            if (detailPicture.isEmpty()) {
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
                return "project-launch";
            }
            FileResponseData detailUploadResult = fileObjectController.uploadImageSample(detailPicture, request);
            issuccess = detailUploadResult.isSuccess();
            if (issuccess) {
                String detailUploadFilePath = detailUploadResult.getFilePath();
                detailPicturePathList.add(detailUploadFilePath);
            }else {
                // 9.如果上传失败则返回到表单页面并显示错误消息
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,CrowdConstant.MESSAGE_DETAIL_PIC_UPLOAD_FAILED);
                return "project-launch";
            }
        }
        projectVO.setDetailPicturePathList(detailPicturePathList);
        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);
        // 一、 完成头图上传
        // 1.获取当前 headerPicture 对象是否为空
/*
        boolean headerPictureIsEmpty = headerPicture.isEmpty();
        if (headerPictureIsEmpty) {
            // 2.如果没有上传头图则返回到表单页面并显示错误消息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,
                    CrowdConstant.MESSAGE_HEADER_PIC_EMPTY);
            return "project-launch";
        }
        System.out.println(1);
        // 3.如果用户确实上传了有内容的文件， 则执行上传
        ResultEntity<String> uploadHeaderPicResultEntity = CrowdUtil.uploadFileToOss(
                ossProperties.getEndPoint(),
                ossProperties.getAccessKeyId(),
                ossProperties.getAccessKeySecret(),
                headerPicture.getInputStream(),
                ossProperties.getBucketName(),
                ossProperties.getBucketDomain(),
                headerPicture.getOriginalFilename());
        System.out.println(2);
        String result = uploadHeaderPicResultEntity.getResult();
        System.out.println(3);
        // 4.判断头图是否上传成功
        if (ResultEntity.SUCCESS.equals(result)) {
            // 5.如果成功则从返回的数据中获取图片访问路径
            String headerPicturePath = uploadHeaderPicResultEntity.getData();
            // 6.存入 ProjectVO 对象中
            projectVO.setHeaderPicturePath(headerPicturePath);
        } else {
            // 7.如果上传失败则返回到表单页面并显示错误消息
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,
                    CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);
            return "project-launch";
        }
        System.out.println(4);
        //二、上传详情图片
        // 1.创建一个用来存放详情图片路径的集合
        List<String> detailPicturePathList = new ArrayList<String>();
        // 2.检查 detailPictureList 是否有效
        if (detailPictureList == null || detailPictureList.size() == 0) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,
                    CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
            return "project-launch";
        }
        //3. 遍历 detailPictureList 集合
        for (MultipartFile detailPicture : detailPictureList) {
            // 4.当前 detailPicture 是否为空
            if (detailPicture.isEmpty()) {
                // 5.检测到详情图片中单个文件为空也是回去显示错误消息
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,
                        CrowdConstant.MESSAGE_DETAIL_PIC_EMPTY);
                return "project-launch";
            }
            //6. 执行上传
            ResultEntity<String> detailUploadResultEntity = CrowdUtil.uploadFileToOss(
                    ossProperties.getEndPoint(),
                    ossProperties.getAccessKeyId(),
                    ossProperties.getAccessKeySecret(),
                    detailPicture.getInputStream(),
                    ossProperties.getBucketName(),
                    ossProperties.getBucketDomain(),
                    detailPicture.getOriginalFilename());
            // 7.检查上传结果
            String detailUploadResult = detailUploadResultEntity.getResult();
            if (ResultEntity.SUCCESS.equals(detailUploadResult)) {
                String detailPicturePath = detailUploadResultEntity.getData();
                // 8.收集刚刚上传的图片的访问路径
                detailPicturePathList.add(detailPicturePath);
            } else {
                 // 9.如果上传失败则返回到表单页面并显示错误消息
                modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,
                        CrowdConstant.MESSAGE_DETAIL_PIC_UPLOAD_FAILED);
                return "project-launch";
            }
        }
        System.out.println(5);
        //10. 将存放了详情图片访问路径的集合存入 ProjectVO 中
        projectVO.setDetailPicturePathList(detailPicturePathList);
        // 三、 后续操作
        // 1.将 ProjectVO 对象存入 Session 域
        session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT, projectVO);*/
        // 2.以完整的访问路径前往下一个收集回报信息的页面

        return "redirect:http://localhost:80/project/return/info/page.html";
    }
    @ResponseBody
    @RequestMapping("/create/upload/return/picture.json")
    public ResultEntity<String> uploadReturnPicture(
            @RequestBody MultipartFile returnPicture,
            HttpServletRequest request
    ){
        boolean issuccess;
        //完成头图的上传
        //获取当前headerPicture对象是否为空
        boolean returnPictureEmpty = returnPicture.isEmpty();
        try {
            if (returnPictureEmpty) {
                return ResultEntity.failed(CrowdConstant.MESSAGE_HEADER_PIC_UPLOAD_FAILED);
            }
            FileResponseData fileResponseData = fileObjectController.uploadImageSample(returnPicture, request);
            issuccess = fileResponseData.isSuccess();
            if (issuccess) {
                String filePath = fileResponseData.getFilePath();
                System.out.println(filePath);
                return ResultEntity.successWithData(filePath);
            }else {

                return ResultEntity.failed(fileResponseData.getMessage());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }

    }
    @ResponseBody
    @RequestMapping("/create/save/return.json")
    public ResultEntity<String> saveReturnInfo(
            ReturnVO returnVO,
            HttpSession session

    ){
        try {
            //从session域中读取之前缓存的ProjectVO对象
            ProjectVO projectVO=(ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
            //判断projectVO是否为Null
            if (projectVO == null) {
                return ResultEntity.failed(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT_MISSING);
            }
            //从projectVO对象中获取存储回报信息的集合
            List<ReturnVO> returnVOList = projectVO.getReturnVOList();
            //判断returnVOList集合是否有效
            if (returnVOList == null||returnVOList.size()==0) {
                //创建集合对象对returnVOList进行初始化
                returnVOList = new ArrayList<ReturnVO>();
                projectVO.setReturnVOList(returnVOList);
            }
            //将收集的表单数据的returnVO对象存入集合
            returnVOList.add(returnVO);
            // 8.把数据有变化的 ProjectVO 对象重新存入 Session 域， 以确保新的数据最终能够存入 Redis
            session.setAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT,projectVO);
            return ResultEntity.successWithoutData();
        } catch (Exception e) {
            e.printStackTrace();
            return ResultEntity.failed(e.getMessage());
        }
    }
    @RequestMapping(value = "/create/confirm/page.html")
    public String saveConfirm(
            ModelMap modelMap,
            HttpSession session,
            MemberConfirmInfoVO memberConfirmInfoVO
    ){
        //从session域中读取之前缓存的ProjectVO对象
        ProjectVO projectVO=(ProjectVO) session.getAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
        System.out.println(projectVO.getHeaderPicturePath());
        System.out.println(projectVO.getProjectDescription());

        System.out.println(memberConfirmInfoVO.getCardnum());
        //判断projectVO是否为Null
        if (projectVO == null) {
            throw  new RuntimeException(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT_MISSING);
        }
        //将确认信息设置到projectVO对象中
        projectVO.setMemberConfirmInfoVO(memberConfirmInfoVO);
        //从session域读取当前登录的用户
        MemberLoginVO memberLoginVO = (MemberLoginVO) session.getAttribute(CrowdConstant.ATTR_NAME_LOGIN_MEMBER);
        Integer memberId = memberLoginVO.getId();
        //调用远程方法保存projectVO对象
        ResultEntity<String> saveResultEntity=mySQLRemoteService.saveProjectVORemote(projectVO,memberId);
        //判断远程的保存操作是否成功
        String result = saveResultEntity.getResult();
        if (ResultEntity.FAILED.equals(result) ) {
            modelMap.addAttribute(CrowdConstant.ATTR_NAME_MESSAGE,saveResultEntity.getMessage());
            return "project-confirm";
        }
        //将临时的ProjectVO对象从Session域中移除
        session.removeAttribute(CrowdConstant.ATTR_NAME_TEMPLE_PROJECT);
        // 8.如果远程保存成功则跳转到最终完成页面
        return "redirect:http://localhost:80/project/create/finish/page.html";


    }


}
