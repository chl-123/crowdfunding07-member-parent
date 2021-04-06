package com.chl.crowd.util;

import com.aliyun.api.gateway.demo.util.HttpUtils;
import com.chl.crowd.constant.CrowdConstant;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;


import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
  public static ResultEntity<String> sendMessage(String phoneNumber,String code){
      String host = "https://gyyyx2.market.alicloudapi.com";
      String path = "/sms/smsSendLong";
      String method = "POST";
      String appcode = "f3c717a423ca4ec3abd25e3d805e7b67";
      Map<String, String> headers = new HashMap<String, String>();
      //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
      headers.put("Authorization", "APPCODE " + appcode);
      Map<String, String> querys = new HashMap<String, String>();
      querys.put("mobile", phoneNumber);
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
}
