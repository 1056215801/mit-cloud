package com.mit.community.util;

import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;

import gui.ava.html.Html2Image;
import util.Base64Util;

public class ImgCompass {

    public static String convertLocalImageToBase64Byte(String imageUrl, Integer sizeLimit) throws IOException {
        sizeLimit = sizeLimit * 1024;
        ByteArrayInputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String data;
        try {
            File file = new File(imageUrl);
            byte[] context = Files.readAllBytes(file.toPath());
            inputStream = new ByteArrayInputStream(context);
            BufferedImage image = ImageIO.read(inputStream);
            ImageIO.write(image, "jpg", outputStream);

            int imageSize = context.length;
            int type = image.getType();
            int height = image.getHeight();
            int width = image.getWidth();

            BufferedImage tempImage;
            //判断文件大小是否大于size,循环压缩，直到大小小于给定的值
            while (imageSize > sizeLimit) {
                //将图片长宽压缩到原来的90%
                height = new Double(height * 0.9).intValue();
                width = new Double(width * 0.9).intValue();
                tempImage = new BufferedImage(width, height, type);
                // 绘制缩小后的图
                tempImage.getGraphics().drawImage(image, 0, 0, width, height, null);
                //重新计算图片大小
                outputStream.reset();
                ImageIO.write(tempImage, "jpg", outputStream);
                imageSize = outputStream.toByteArray().length;
            }
            data = Base64Util.encode(outputStream.toByteArray());
        } catch (Exception e) {
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;

    }
    public static String convertFlieImageToBase64Byte(File file, Integer sizeLimit) throws IOException{
        sizeLimit = sizeLimit * 1024;
        ByteArrayInputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String data;
        try {
            byte[] context = Files.readAllBytes(file.toPath());
            inputStream = new ByteArrayInputStream(context);
            BufferedImage image = ImageIO.read(inputStream);
            ImageIO.write(image, "jpg", outputStream);

            int imageSize = context.length;
            int type = image.getType();
            int height = image.getHeight();
            int width = image.getWidth();

            BufferedImage tempImage;
            //判断文件大小是否大于size,循环压缩，直到大小小于给定的值
            while (imageSize > sizeLimit) {
                //将图片长宽压缩到原来的90%
                height = new Double(height * 0.9).intValue();
                width = new Double(width * 0.9).intValue();
                tempImage = new BufferedImage(width, height, type);
                // 绘制缩小后的图
                tempImage.getGraphics().drawImage(image, 0, 0, width, height, null);
                //重新计算图片大小
                outputStream.reset();
                ImageIO.write(tempImage, "jpg", outputStream);
                imageSize = outputStream.toByteArray().length;
            }
            while (imageSize<102400){
                //将图片长宽扩大到原来的1.1
                height = new Double(height * 1.1).intValue();
                width = new Double(width * 1.1).intValue();
                tempImage = new BufferedImage(width, height, type);
                // 绘制缩小后的图
                tempImage.getGraphics().drawImage(image, 0, 0, width, height, null);
                //重新计算图片大小
                outputStream.reset();
                ImageIO.write(tempImage, "jpg", outputStream);
                imageSize = outputStream.toByteArray().length;
            }
            data = Base64Util.encode(outputStream.toByteArray());
        } catch (Exception e) {
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;

    }
    public static String convertFlieImageToBase64(File file, Integer sizeLimit) throws IOException{
        sizeLimit = sizeLimit * 1024;
        ByteArrayInputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String data;
        try {
            byte[] context = Files.readAllBytes(file.toPath());
            inputStream = new ByteArrayInputStream(context);
            BufferedImage image = ImageIO.read(inputStream);
            ImageIO.write(image, "jpg", outputStream);

            data = Base64Util.encode(outputStream.toByteArray());
        } catch (Exception e) {
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;

    }
    public static String imageUrlTobase64Byte(String imageUrl,Integer sizeLimit){
        sizeLimit = sizeLimit * 1024;
        ByteArrayInputStream inputStream = null;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String data="";
        try {
            byte[] context = getFileStream(imageUrl);
            inputStream = new ByteArrayInputStream(context);
            BufferedImage image = ImageIO.read(inputStream);
            ImageIO.write(image, "jpg", outputStream);
            data = Base64Util.encode(outputStream.toByteArray());
            int imageSize = context.length;
            int type = image.getType();
            int height = image.getHeight();
            int width = image.getWidth();
            BufferedImage tempImage;
            //判断文件大小是否大于size,循环压缩，直到大小小于给定的值
            while (imageSize > sizeLimit) {
                //将图片长宽压缩到原来的90%
                height = new Double(height * 0.9).intValue();
                width = new Double(width * 0.9).intValue();
                tempImage = new BufferedImage(width, height, type);
                // 绘制缩小后的图
                tempImage.getGraphics().drawImage(image, 0, 0, width, height, null);
                //重新计算图片大小
                outputStream.reset();
                ImageIO.write(tempImage, "jpg", outputStream);
                imageSize = outputStream.toByteArray().length;
            }
            while (imageSize<102400){
                //将图片长宽扩大到原来的1.1
                height = new Double(height * 1.1).intValue();
                width = new Double(width * 1.1).intValue();
                tempImage = new BufferedImage(width, height, type);
                // 绘制缩小后的图
                tempImage.getGraphics().drawImage(image, 0, 0, width, height, null);
                //重新计算图片大小
                outputStream.reset();
                ImageIO.write(tempImage, "jpg", outputStream);
                imageSize = outputStream.toByteArray().length;
            }
        } catch (Exception e) {

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }
    public static String convertImageToBase64Byte(String imageUrl, Integer sizeLimit) throws IOException {
        sizeLimit = sizeLimit * 1024;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String data;
        try {
            //从远程读取图片
            URL url = new URL(imageUrl);
            Html2Image html2Image = Html2Image.fromURL(url);
            BufferedImage image = html2Image.getImageRenderer().setImageType("jpg").getBufferedImage();
            ImageIO.write(image, "jpg", outputStream);
            int imageSize = outputStream.toByteArray().length;
            int type = image.getType();
            int height = image.getHeight();
            int width = image.getWidth();
            BufferedImage tempImage;
            //判断文件大小是否大于size,循环压缩，直到大小小于给定的值
            while (imageSize > sizeLimit) {
                //将图片长宽压缩到原来的90%
                height = new Double(height * 0.9).intValue();
                width = new Double(width * 0.9).intValue();
                tempImage = new BufferedImage(width, height, type);
                // 绘制缩小后的图
                tempImage.getGraphics().drawImage(image, 0, 0, width, height, null);
                //重新计算图片大小
                outputStream.reset();
                ImageIO.write(tempImage, "jpg", outputStream);
                imageSize = outputStream.toByteArray().length;
            }
            data = Base64Util.encode(outputStream.toByteArray());
        } catch (Exception e) {
            throw e;
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    public static byte[] convertImageToByteArray(String imageUrl, Integer sizeLimit) throws IOException {
        //默认上限为500k
        if (sizeLimit == null) {
            sizeLimit = 500;
        }
        sizeLimit = sizeLimit * 1024;
        String base64Image;
        DataInputStream dataInputStream = null;
        ByteArrayOutputStream outputStream = null;
        ByteArrayInputStream inputStream = null;
        byte[] data;
        try {
            //从远程读取图片
            URL url = new URL(imageUrl);
            dataInputStream = new DataInputStream(url.openStream());
            outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            byte[] context = outputStream.toByteArray();

            //将图片数据还原为图片
            inputStream = new ByteArrayInputStream(context);
            BufferedImage image = ImageIO.read(inputStream);

            int imageSize = context.length;
            int type = image.getType();
            int height = image.getHeight();
            int width = image.getWidth();
            BufferedImage tempImage;
            //判断文件大小是否大于size,循环压缩，直到大小小于给定的值
            while (imageSize > sizeLimit) {
                //将图片长宽压缩到原来的90%
                height = new Double(height * 0.9).intValue();
                width = new Double(width * 0.9).intValue();
                tempImage = new BufferedImage(width, height, type);
                // 绘制缩小后的图
                tempImage.getGraphics().drawImage(image, 0, 0, width, height, null);
                //重新计算图片大小
                outputStream.reset();
                ImageIO.write(tempImage, "jpg", outputStream);
                imageSize = outputStream.toByteArray().length;
            }
            //将图片转化为base64并返回
             data = outputStream.toByteArray();
            //此处一定要使用org.apache.tomcat.util.codec.binary.Base64，防止再linux上出现换行等特殊符号
            //base64Image = Base64.encodeBase64String(data);
        } catch (Exception e) {
            //抛出异常
            throw e;
        } finally {
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return data;
    }

    public static int showUrlLens(String imageUrl, Integer sizeLimit) throws IOException {
        //默认上限为500k
        if (sizeLimit == null) {
            sizeLimit = 500;
        }
        sizeLimit = sizeLimit * 1024;
        String base64Image;
        DataInputStream dataInputStream = null;
        ByteArrayOutputStream outputStream = null;
        ByteArrayInputStream inputStream = null;
        int imageSize=0;
        byte[] data;
        try {
            //从远程读取图片
            URL url = new URL(imageUrl);
            dataInputStream = new DataInputStream(url.openStream());
            outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            byte[] context = outputStream.toByteArray();

            //将图片数据还原为图片
            inputStream = new ByteArrayInputStream(context);
            BufferedImage image = ImageIO.read(inputStream);
             imageSize = context.length;
        } catch (Exception e) {
            //抛出异常
            throw e;
        } finally {
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return imageSize;
    }
    public  int showLens(String imageUrl, Integer sizeLimit) throws IOException {
        //默认上限为500k
        if (sizeLimit == null) {
            sizeLimit = 500;
        }
        sizeLimit = sizeLimit * 1024;
        String base64Image;
        DataInputStream dataInputStream = null;
        ByteArrayOutputStream outputStream = null;
        ByteArrayInputStream inputStream = null;
        int imageSize=0;
        byte[] data;
        try {
            //从远程读取图片
            URL url = new URL(imageUrl);
            dataInputStream = new DataInputStream(url.openStream());
            outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            byte[] context = outputStream.toByteArray();

            //将图片数据还原为图片
            inputStream = new ByteArrayInputStream(context);
            BufferedImage image = ImageIO.read(inputStream);
            imageSize = context.length;
        } catch (Exception e) {
            //抛出异常
            throw e;
        } finally {
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return imageSize;
    }

    public static byte[]  showUrlBtyes(String imageUrl) throws IOException {

        String base64Image;
        DataInputStream dataInputStream = null;
        ByteArrayOutputStream outputStream = null;
        ByteArrayInputStream inputStream = null;
        byte[] data;
        byte[] context;
        try {
            //从远程读取图片
            URL url = new URL(imageUrl);
            dataInputStream = new DataInputStream(url.openStream());
            outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[2048];
            int length;
            while ((length = dataInputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            context = outputStream.toByteArray();


        } catch (Exception e) {
            //抛出异常
            throw e;
        } finally {
            if (dataInputStream != null) {
                try {
                    dataInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return context;
    }
    //计算base64图片的字节数(单位:字节)
    //传入的图片base64是去掉头部的data:image/png;base64,字符串
    public static Integer imageSize(String imageBase64Str){

        //1.找到等号，把等号也去掉(=用来填充base64字符串长度用)
        Integer equalIndex= imageBase64Str.indexOf("=");
        if(imageBase64Str.indexOf("=")>0) {
            imageBase64Str=imageBase64Str.substring(0, equalIndex);
        }
        //2.原来的字符流大小，单位为字节
        Integer strLength=imageBase64Str.length();
        System.out.println("imageBase64Str Length = "+strLength);
        //3.计算后得到的文件流大小，单位为字节
        Integer size=strLength-(strLength/8)*2;
        return size;
    }

    /**
     * byte(字节)根据长度转成kb(千字节)和mb(兆字节)
     *
     * @param bytes
     * @return
     */
    public static float bytesToKB(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 1, BigDecimal.ROUND_DOWN).floatValue();
        if (returnValue > 1) {
            return returnValue;
        }

        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 1, BigDecimal.ROUND_DOWN).floatValue();
        return returnValue;
    }
    public static byte[] getFileStream(String url)throws Exception{
        try {
            URL httpUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection)httpUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5 * 1000);
            InputStream inStream = conn.getInputStream();//通过输入流获取图片数据
            byte[] btImg = readInputStream(inStream);//得到图片的二进制数据
            return btImg;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[] readInputStream(InputStream inStream) throws Exception{
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        return outStream.toByteArray();
    }
}


