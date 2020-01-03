package com.mit.community.hardware;

import java.io.UnsupportedEncodingException;

/**
 * @Author qishengjun
 * @Date Created in 9:40 2019/12/10
 * @Company: mitesofor </p>
 * @Description:~
 */
public class ClientDemo {

    public static String DLL_PATH;
    static{
        String path=(ClientDemo.class.getResource("/").getPath()).replaceAll("%20", " ").substring(1).replace("/", "\\");
        try {
            DLL_PATH= java.net.URLDecoder.decode(path,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

}
}