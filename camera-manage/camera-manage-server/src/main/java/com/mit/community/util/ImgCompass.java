package com.mit.community.util;

import org.apache.commons.codec.binary.Base64;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
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
}
