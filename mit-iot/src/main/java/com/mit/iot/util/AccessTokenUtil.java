package com.mit.iot.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.security.MessageDigest;
import java.util.Map;

public class AccessTokenUtil {
    static char[] hc = "0123456789abcdef".toCharArray();

    public static String getClientSecret(String appKey ,String grant_type, String redirect_uri , String code, String appSecret) throws Exception{
        String str = appKey + grant_type + redirect_uri + code + appSecret;
        return MD5(str);
    }


    public static String MD5(String param) throws Exception{
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.reset();
        md.update(param.getBytes("utf-8"));
        byte[] d = md.digest();
        StringBuilder r = new StringBuilder(d.length*2);
        for (byte b : d) {
            r.append(hc[(b >> 4) & 0xF]);
            r.append(hc[(b & 0xF)]);
        }
        return r.toString();
    }

    /**
     * 发送POST请求
     *
     * @param url
     *            目的地址
     * @param parameters
     *            请求参数，Map类型。
     * @return 远程响应结果
     */
    public static String sendPost(String url, Map<String,Object> parameters) throws Exception{
        String result = "";// 返回的结果
        BufferedReader in = null;// 读取响应输入流
        PrintWriter out = null;
        StringBuffer sb = new StringBuffer();// 处理请求参数
        String params = "";// 编码之后的参数
        try {
            // 编码请求参数
            if (parameters.size() == 1) {
                for (String name : parameters.keySet()) {
                    sb.append(name).append("=").append(
                            parameters.get(name));
                }
                params = sb.toString();
            } else if(parameters.size() > 1){
                for (String name : parameters.keySet()) {
                    sb.append(name).append("=").append(
                            parameters.get(name)).append("&");
                }
                String temp_params = sb.toString();
                params = temp_params.substring(0, temp_params.length() - 1);
            }
            //params = URLEncoder.encode(params, "utf-8");
            // 创建URL对象
            //System.out.println("=====================发送上传参数请求请求");
            java.net.URL connURL = new java.net.URL(url);
            // 打开URL连接
            java.net.HttpURLConnection httpConn = (java.net.HttpURLConnection) connURL
                    .openConnection();
            // 设置通用属性
            httpConn.setRequestProperty("Content-type","application/x-www-form-urlencoded");
            httpConn.setRequestProperty("Accept", "*/*");
            httpConn.setRequestProperty("Connection", "Keep-Alive");
            httpConn.setRequestProperty("User-Agent",
                    "Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.1)");
            // 设置POST方式
            httpConn.setDoInput(true);
            httpConn.setDoOutput(true);
            // 获取HttpURLConnection对象对应的输出流
            out = new PrintWriter(httpConn.getOutputStream());
            // 发送请求参数
            out.write(params);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应，设置编码方式
            in = new BufferedReader(new InputStreamReader(httpConn
                    .getInputStream(), "UTF-8"));
            String line;
            // 读取返回的内容
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
}
