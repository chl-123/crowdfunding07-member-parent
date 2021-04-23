package com.chl.crowd;

import com.chl.crowd.controller.FileObjectController;


public class FastdfsApplication  {

    public static void main(String[] args) {
        FileObjectController fileObjectController=new FileObjectController();

        System.out.println(fileObjectController.uploadSample1("C:\\Users\\chl\\Desktop\\FastDFS\\test1.png"));
    }

}
