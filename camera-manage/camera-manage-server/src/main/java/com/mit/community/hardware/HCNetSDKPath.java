package com.mit.community.hardware;

import java.io.UnsupportedEncodingException;

/**
 * @Author qishengjun
 * @Date Created in 11:12 2019/12/16
 * @Company: mitesofor </p>
 * @Description:~
 */
public class HCNetSDKPath {

    public static String DLL_PATH;
    static {
        String path = (HCNetSDKPath.class.getResource("/").getPath()).
                replaceAll("%20", " ").substring(1).replace("bin", "SDK").replace("/","\\");
        System.out.println(path);
        try {
            DLL_PATH = java.net.URLDecoder.decode(path, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
