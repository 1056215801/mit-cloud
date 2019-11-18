package com.mit.workflow.utils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @Description 输入输出流工具类
 */
public class IOUtils {

    /**
     *
     * @param in InputStream
     * @param response HttpServletResponse
     * @throws IOException
     */
    public static void transferInputStream2Res(InputStream in, HttpServletResponse response) throws IOException {
        OutputStream out = response.getOutputStream();
        byte[] buf = new byte[1024];
        int length = 0;
        try {
            while ((length = in.read(buf)) != -1) {
                out.write(buf, 0, length);
            }
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }
}
