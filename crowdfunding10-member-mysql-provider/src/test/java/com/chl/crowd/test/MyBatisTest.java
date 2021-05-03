package com.chl.crowd.test;

import com.chl.crowd.entity.vo.DetailProjectVO;
import com.chl.crowd.entity.vo.DetailReturnVO;
import com.chl.crowd.entity.vo.PortalProjectVO;
import com.chl.crowd.entity.vo.PortalTypeVO;
import com.chl.crowd.mapper.ProjectPOMapper;
import org.junit.Test;
import org.slf4j.Logger;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyBatisTest {
    @Autowired
    private ProjectPOMapper projectPOMapper;
     Logger logger= LoggerFactory.getLogger(MyBatisTest.class);
     @Test
    public  void test(){
         /*List<PortalTypeVO> portalTypeVOList = projectPOMapper.selectPortalTypeVOList();
         for (PortalTypeVO portalTypeVO: portalTypeVOList
              ) {
             String name = portalTypeVO.getName();
             String remark = portalTypeVO.getRemark();
             logger.info(name+remark);
             System.out.println(name+remark);
             List<PortalProjectVO> portalProjectVOList = portalTypeVO.getPortalProjectVOList();
             for (PortalProjectVO portalProjectVO: portalProjectVOList
                  ) {
                 System.out.println(portalProjectVO);
                 logger.info(portalProjectVO.toString());
             }
         }*/
         Integer projectId = 9;

         DetailProjectVO detailProjectVO = projectPOMapper.selectDetailProjectVO(projectId);

         logger.info(detailProjectVO.getProjectId() + "");
         logger.info(detailProjectVO.getProjectName());
         logger.info(detailProjectVO.getProjectDesc());
         logger.info(detailProjectVO.getFollowerCount() + "");
         logger.info(detailProjectVO.getStatus() + "");
         logger.info(detailProjectVO.getMoney() + "");
         logger.info(detailProjectVO.getSupportMoney() + "");
         logger.info(detailProjectVO.getPercentage() + "");
         logger.info(detailProjectVO.getDeployDate() + "");
         logger.info(detailProjectVO.getSupporterCount() + "");
         logger.info(detailProjectVO.getHeaderPicturePath());

         List<String> detailPicturePathList = detailProjectVO.getDetailPicturePathList();
         for (String path : detailPicturePathList) {
             logger.info("detail path="+path);
         }

         List<DetailReturnVO> detailReturnVOList = detailProjectVO.getDetailReturnVOList();
         for (DetailReturnVO detailReturnVO : detailReturnVOList) {
             logger.info(detailReturnVO.getReturnId() + "");
             logger.info(detailReturnVO.getSupportMoney() + "");
             logger.info(detailReturnVO.getSignalPurchase() + "");
             logger.info(detailReturnVO.getPurchase() + "");
             logger.info(detailReturnVO.getSupproterCount() + "");
             logger.info(detailReturnVO.getFreight() + "");
             logger.info(detailReturnVO.getReturnDate() + "");
             logger.info(detailReturnVO.getContent() + "");
             logger.info(detailReturnVO.getFreight() + "");
         }
     }


}
