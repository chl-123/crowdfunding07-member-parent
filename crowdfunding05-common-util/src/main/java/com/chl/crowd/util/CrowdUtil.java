package com.chl.crowd.util;

import com.aliyun.api.gateway.demo.util.HttpUtils;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.common.comm.ResponseMessage;
import com.aliyun.oss.model.PutObjectResult;
import com.chl.crowd.constant.CrowdConstant;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.csource.common.MyException;
import org.csource.fastdfs.*;


import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 尚筹网项目通用工具方法类
 *
 * @author 吴彦祖
 */
public class CrowdUtil {

  /**
   * 对明文字符串进行MD5加密
   *
   * @param source 传入的明文字符串
   * @return 加密结果
   */
  public static String md5(String source) {

    // 1.判断source是否有效
    if (source == null || source.length() == 0) {

      // 2.如果不是有效的字符串抛出异常
      throw new RuntimeException(CrowdConstant.MESSAGE_STRING_INVALIDATE);
    }

    try {
      // 3.获取MessageDigest对象
      String algorithm = "md5";

      MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

      // 4.获取明文字符串对应的字节数组
      byte[] input = source.getBytes();

      // 5.执行加密
      byte[] output = messageDigest.digest(input);

      // 6.创建BigInteger对象
      int signum = 1;
      BigInteger bigInteger = new BigInteger(signum, output);

      // 7.按照16进制将bigInteger的值转换为字符串
      int radix = 16;
      String encoded = bigInteger.toString(radix).toUpperCase();

      return encoded;

    } catch (NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return null;
  }

  /**
   * 判断当前请求是否为Ajax请求
   *
   * @param request 请求对象
   * @return true：当前请求是Ajax请求 false：当前请求不是Ajax请求
   */
  /*  public static boolean judgeRequestType(HttpServletRequest request) {

    // 1.获取请求消息头
    String acceptHeader = request.getHeader("Accept");
    String xRequestHeader = request.getHeader("X-Requested-With");

    // 2.判断
    return (acceptHeader != null && acceptHeader.contains("application/json"))
        || (xRequestHeader != null && xRequestHeader.equals("XMLHttpRequest"));
  }*/
  public static boolean judgeRequestType(HttpServletRequest request) {
    // 1.获取请求消息头
    String acceptHeader = request.getHeader("Accept");
    String xRequestHeader = request.getHeader("X-Requested-With");
    // 2.判断
    return (acceptHeader != null && acceptHeader.contains("application/json"))
        || (xRequestHeader != null && xRequestHeader.equals("XMLHttpRequest"));
  }

  public static String createDate(String DateFormat) {
    Date date = new Date();
    SimpleDateFormat format = new SimpleDateFormat(DateFormat);
    String createTime = format.format(date);
    return createTime;
  }
  public static ResultEntity<String> sendMessage(String method,String appcode,String phoneNumber){
      String host = "https://gyyyx2.market.alicloudapi.com";
      String path = "/sms/smsSendLong";
//      String method = "POST";
//      String appcode = "f3c717a423ca4ec3abd25e3d805e7b67";
      Map<String, String> headers = new HashMap<String, String>();
      //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
      headers.put("Authorization", "APPCODE " + appcode);
      Map<String, String> querys = new HashMap<String, String>();
      querys.put("mobile", phoneNumber);
      //生成随机验证码
      String code=createCode();
      querys.put("param", "**code**:"+code+",**minute**:5");
      querys.put("smsSignId", "2e65b1bb3d054466b82f0c9d125465e2");
      querys.put("templateId", "908e94ccf08b4476ba6c876d13f084ad");
      Map<String, String> bodys = new HashMap<String, String>();


      try {
        /**
         * 重要提示如下:
         * HttpUtils请从
         * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
         * 下载
         *
         * 相应的依赖请参照
         * https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
         */
        HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
        StatusLine statusLine = response.getStatusLine();
        String reasonPhrase = statusLine.getReasonPhrase();
          int statusCode = statusLine.getStatusCode();
        if (statusCode == 200) {
            return ResultEntity.successWithData(code);
        }

       return ResultEntity.failed(reasonPhrase);
        //获取response的body
        //System.out.println(EntityUtils.toString(response.getEntity()));
      } catch (Exception e) {
        e.printStackTrace();
        return ResultEntity.failed(e.getMessage());
      }
  }
  public  static String createCode(){
      StringBuilder builder=new StringBuilder();
      for (int i = 0; i < 4; i++) {
          int random=(int) (Math.random()*10);
          builder.append(random);
      }
      String code=builder.toString();
      return code;

  }
  public  static void upload(String filePath,String file_ext_name) throws IOException, MyException {
      ClientGlobal.init("config/fdfs_client.conf");

      ClientGlobal.initByProperties("config/fastdfs-client.properties");
      //3、创建一个TrackerClient对象
      TrackerClient trackerClient = new TrackerClient();

      //4、创建一个TrackerServer对象。
      TrackerServer trackerServer = trackerClient.getConnection();
      //5、声明一个StorageServer对象，null。
      StorageServer storageServer = null;
      //6、获得StorageClient对象。
      StorageClient storageClient = new StorageClient(trackerServer, storageServer);

      //7、直接调用StorageClient对象方法上传文件即可。
      String[] result = storageClient.upload_file("C:\\Users\\chl\\Desktop\\FastDFS\\test1.png", "png", null);
      //注意你的fastdfs产生的地址是否有端口号,有的话这里也需要添加

/*
*
* 用户登录名称 chl0521@1398599597977363.onaliyun.com
AccessKey ID LTAI5tGdEw6o5uFsxDbsP68r
AccessKey Secret llPHTgpWBkLrIjquhd14Kuy7oVBS25
* */

      StringBuilder sb = new 	StringBuilder("http://192.168.60.132:8555/");
      sb.append(result[0]).append("/").append(result[1]);
  }

    public static ResultEntity<String> uploadFileToOss(
            String endpoint,
            String accessKeyId,
            String accessKeySecret,
            InputStream inputStream,
            String bucketName,
            String bucketDomain,
            String originalName) {
        // 创建 OSSClient 实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
        // 生成上传文件的目录
        String folderName = new SimpleDateFormat("yyyyMMdd").format(new Date());
        // 生成上传文件在 OSS 服务器上保存时的文件名
        // 原始文件名： beautfulgirl.jpg
        // 生成文件名： wer234234efwer235346457dfswet346235.jpg
        // 使用 UUID 生成文件主体名称
        String fileMainName = UUID.randomUUID().toString().replace("-", "");
        // 从原始文件名中获取文件扩展名
        String extensionName = originalName.substring(originalName.lastIndexOf("."));
        // 使用目录、 文件主体名称、 文件扩展名称拼接得到对象名称
        String objectName = folderName + "/" + fileMainName + extensionName;
        try {
        // 调用 OSS 客户端对象的方法上传文件并获取响应结果数据
            PutObjectResult putObjectResult = ossClient.putObject(bucketName, objectName,
                    inputStream);
        // 从响应结果中获取具体响应消息
            ResponseMessage responseMessage = putObjectResult.getResponse();
        // 根据响应状态码判断请求是否成功
            if (responseMessage == null) {
        // 拼接访问刚刚上传的文件的路径
                String ossFileAccessPath = bucketDomain + "/" + objectName;
        // 当前方法返回成功
                return ResultEntity.successWithData(ossFileAccessPath);
            } else {
        // 获取响应状态码
                int statusCode = responseMessage.getStatusCode();
                // 如果请求没有成功， 获取错误消息
                String errorMessage = responseMessage.getErrorResponseAsString();
        // 当前方法返回失败
                return ResultEntity.failed(" 当 前 响 应 状 态 码 =" + statusCode + " 错 误 消 息 =" + errorMessage);
            }
        } catch (Exception e) {
            e.printStackTrace();
        // 当前方法返回失败
            return ResultEntity.failed(e.getMessage());
        } finally {
            if (ossClient != null) {
        // 关闭 OSSClient。
                ossClient.shutdown();
            }
        }
    }
}
