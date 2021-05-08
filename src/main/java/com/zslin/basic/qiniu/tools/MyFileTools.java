package com.zslin.basic.qiniu.tools;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * 需要在仓库目录执行：mvn install:install-file -Dfile=jave-1.0.2.jar -DgroupId=jave -DartifactId=jave -Dversion=1.0.2 -Dpackaging=jar -DgeneratePom=true
 * Created by zsl on 2018/9/12.
 */
public class MyFileTools {

    /**
     * 判断文件是否为图片文件
     * @param fileName
     * @return
     */
    public static Boolean isImageFile(String fileName) {
        return isSpeFile(fileName, ".jpg", ".jpeg", ".png", ".gif", ".bmp");
    }

    /**
     * 判断文件是否为视频文件
     * @param fileName
     * @return
     */
    public static Boolean isVideoFile(String fileName) {
        return isSpeFile(fileName, ".mp4", ".avi");
    }

    /**
     * 判断文件是否为PPT文件
     * @param fileName
     * @return
     */
    public static Boolean isPPTFile(String fileName) {
        return isSpeFile(fileName, ".ppt", ".pptx");
    }

    /**
     * 判断文件是否为Word文件
     * @param fileName
     * @return
     */
    public static Boolean isWordFile(String fileName) {
        return isSpeFile(fileName, ".doc", ".docx");
    }

    /**
     * 判断文件是否为PDF文件
     * @param fileName
     * @return
     */
    public static Boolean isPDFFile(String fileName) {
        return isSpeFile(fileName, ".pdf");
    }

    /**
     * 判断文件是否为指定文件
     * @param fileName
     * @return
     */
    public static Boolean isSpeFile(String fileName, String... targetFileTypes) {
        if(fileName==null || targetFileTypes==null || targetFileTypes.length<=0) {return false;}
        fileName = fileName.toLowerCase();
        for(String type : targetFileTypes) {
            if(fileName.endsWith(type)) {return true;}
        }
        return false;
    }

    public static String getFileType(String fileName) {
        if(fileName!=null && fileName.indexOf(".")>=0) {
            return fileName.substring(fileName.lastIndexOf("."), fileName.length());
        }
        return "";
    }

    /** 判断是否是远程文件 */
    public static boolean isRemoteFile(String url) {
        url.toLowerCase();
        return (url.startsWith("http://") || url.startsWith("https://"));
    }

    /** 通过url获取文件名称 */
    public static String getFileName(String url) {
        if(url.contains("/")) {
            return url.substring(url.lastIndexOf("/") + 1);
        } else {return url;}
    }

    public static String getFileEngType(String fileName) {
        if(isImageFile(fileName)) {return "image";}
        else if(isVideoFile(fileName)) {return "video";}
        else if(isPDFFile(fileName)) {return "pdf";}
        else if(isPPTFile(fileName)) {return "ppt";}
        else if(isWordFile(fileName)) {return "word";}
        else {return "other";}
    }

    /** 获取文件的MD5 */
    public static String getFileMd5(File file) {
        String value = "";
        try {
            FileInputStream fis = new FileInputStream(file);
            value = DigestUtils.md5Hex(fis);
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*FileInputStream in = null;
        try {
            in = new FileInputStream(file);
            MappedByteBuffer byteBuffer = in.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteBuffer);
            BigInteger bi = new BigInteger(1, md5.digest());
            value = bi.toString(16);
            byteBuffer.clear();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }*/
        return value;
    }
}
