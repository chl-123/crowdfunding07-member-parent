package com.chl.crowd.controller;

import com.chl.crowd.common.FastDFSClient;
import com.chl.crowd.config.ErrorCode;
import com.chl.crowd.config.FastDFSException;
import com.chl.crowd.config.FileResponseData;
import com.chl.crowd.utils.FileCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

/**
 * 文件接口
 */

@PropertySource("classpath:config.properties")
public class FileObjectController {

    private FastDFSClient fastDFSClient = new FastDFSClient();

    /**
     * 文件服务器地址
     */
    @Value("${file_server_addr}")
    private String fileServerAddr;

    /**
     * FastDFS秘钥
     */
    @Value("${fastdfs.http_secret_key}")
    private String fastDFSHttpSecretKey="FastDFS1234567890";


    public FileResponseData test(){
        return new FileResponseData(true);
    }

    /**
     * 上传文件通用，只上传文件到服务器，不会保存记录到数据库
     *
     * @param file
     * @param request
     * @return 返回文件路径等信息
     */

    public FileResponseData uploadFileSample(MultipartFile file, HttpServletRequest request){
        return uploadSample(file, request);
    }

    /**
     * 只能上传图片，只上传文件到服务器，不会保存记录到数据库. <br>
     * 会检查文件格式是否正确，默认只能上传 ['png', 'gif', 'jpeg', 'jpg'] 几种类型.
     *
     * @param file
     * @param request
     * @return 返回文件路径等信息
     */
    public FileResponseData uploadImageSample( MultipartFile file, HttpServletRequest request){
        // 检查文件类型
        if(!FileCheck.checkImage(file.getOriginalFilename())){
            FileResponseData responseData = new FileResponseData(false);
            responseData.setCode(ErrorCode.FILE_TYPE_ERROR_IMAGE.CODE);
            responseData.setMessage(ErrorCode.FILE_TYPE_ERROR_IMAGE.MESSAGE);
            return responseData;
        }

        return uploadSample(file, request);
    }

    /**
     * 只能上传文档，只上传文件到服务器，不会保存记录到数据库. <br>
     * 会检查文件格式是否正确，默认只能上传 ['pdf', 'ppt', 'xls', 'xlsx', 'pptx', 'doc', 'docx'] 几种类型.
     *
     * @param file
     * @param request
     * @return 返回文件路径等信息
     */

    public FileResponseData uploadDocSample( MultipartFile file, HttpServletRequest request){
        // 检查文件类型
        if(!FileCheck.checkDoc(file.getOriginalFilename())){
            FileResponseData responseData = new FileResponseData(false);
            responseData.setCode(ErrorCode.FILE_TYPE_ERROR_DOC.CODE);
            responseData.setMessage(ErrorCode.FILE_TYPE_ERROR_DOC.MESSAGE);
            return responseData;
        }

        return uploadSample(file, request);
    }

    /**
     * 以附件形式下载文件
     *
     * @param filePath 文件地址
     * @param response
     */
    public void downloadFile(String filePath, HttpServletResponse response) throws FastDFSException {
        try {
            fastDFSClient.downloadFile(filePath, response);
        } catch (FastDFSException e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 获取图片 使用输出流输出字节码，可以使用< img>标签显示图片<br>
     *
     * @param filePath 图片地址
     * @param response
     */
    public void downloadImage(String filePath, HttpServletResponse response) throws FastDFSException {
        try {
            fastDFSClient.downloadFile(filePath, response.getOutputStream());
        } catch (FastDFSException e) {
            e.printStackTrace();
            throw e;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据指定的路径删除服务器文件，适用于没有保存数据库记录的文件
     *
     * @param filePath
     */
    public FileResponseData deleteFile(String filePath, Locale locale) {
        FileResponseData responseData = new FileResponseData();
        try {
            fastDFSClient.deleteFile(filePath);
        } catch (FastDFSException e) {
            e.printStackTrace();
            responseData.setSuccess(false);
            responseData.setCode(e.getCode());
            responseData.setMessage(e.getMessage());
        }
        return responseData;
    }

    /**
     * 获取访问文件的token
     *
     * @param filePath 文件路径
     * @return
     */
    public FileResponseData getToken(String filePath){
        FileResponseData responseData = new FileResponseData();
        // 设置访文件的Http地址. 有时效性.
        String token = FastDFSClient.getToken(filePath, fastDFSHttpSecretKey);
        responseData.setToken(token);
        responseData.setHttpUrl(fileServerAddr+"/"+filePath+"?"+token);

        return responseData;
    }

    /**
     * 上传通用方法，只上传到服务器，不保存记录到数据库
     *
     * @param file
     * @param request
     * @return
     */
    public FileResponseData uploadSample(MultipartFile file, HttpServletRequest request){
        FileResponseData responseData = new FileResponseData();
        try {
            // 上传到服务器
            String filepath = fastDFSClient.uploadFileWithMultipart(file);
            //String filepath=fastDFSClient.uploadFileWithFilepath(file);
            responseData.setFileName(file.getOriginalFilename());

            responseData.setFilePath("http://192.168.60.132:8555/"+filepath);
            responseData.setFileType(FastDFSClient.getFilenameSuffix(file.getOriginalFilename()));
            // 设置访文件的Http地址. 有时效性.
            String token = FastDFSClient.getToken(filepath, fastDFSHttpSecretKey);
            responseData.setToken(token);
            responseData.setHttpUrl(fileServerAddr+"/"+filepath+"?"+token);
        } catch (FastDFSException e) {
            responseData.setSuccess(false);
            responseData.setCode(e.getCode());
            responseData.setMessage(e.getMessage());
        }

        return responseData;
    }
    public FileResponseData uploadSample1(String file/*MultipartFile file*//*, HttpServletRequest request*/){
        FileResponseData responseData = new FileResponseData();
        try {
            // 上传到服务器
            //String filepath = fastDFSClient.uploadFileWithMultipart(file);
            String filepath=fastDFSClient.uploadFileWithFilepath(file);
           /* responseData.setFileName(file.getOriginalFilename());
            responseData.setFilePath(filepath);
            responseData.setFileType(FastDFSClient.getFilenameSuffix(file.getOriginalFilename()));
            // 设置访文件的Http地址. 有时效性.
            String token = FastDFSClient.getToken(filepath, fastDFSHttpSecretKey);
            responseData.setToken(token);
            responseData.setHttpUrl(fileServerAddr+"/"+filepath+"?"+token);*/
        } catch (FastDFSException e) {
            responseData.setSuccess(false);
            responseData.setCode(e.getCode());
            responseData.setMessage(e.getMessage());
        }

        return responseData;
    }

}
