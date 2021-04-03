package com.zslin.finance.imgTools;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import net.coobird.thumbnailator.Thumbnails;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片拼接
 * https://blog.csdn.net/qq_21235239/article/details/99416808
 */
public class ImgJoinUtil {
    public static File createColorImg(final int width, final int height, final Color color, List<BufferedImgBean> imgBeanList, List<TextBean> textBeanList) {
        return createColorImg(width, height, color, null, imgBeanList, textBeanList);
    }

    /** 判断是否为空 */
    private static boolean isNotEmptyCollection(List list) {
        return (list != null && list.size()>0);
    }

    private static boolean isNotEmpty(String str) {
        return (str!=null && !"".equals(str));
    }

    public static File createColorImg(String outPath, String ... imgUrls) throws Exception {
        List<BufferedImgBean> imgList = new ArrayList<>();
        //TODO 图片的尺寸为393x547
        int i=1;
        int left=0,top=0;
        for(String img : imgUrls) {
            if(i==1) {
                left = 2; top = 2;
            } else if(i==2) {
                left = 397; top = 2;
            } else if(i==3) {left = 2; top = 561;}
            else if(i==4) {left = 397; top = 561;}
//            File file = new File(img);
            String resize = rebuild(img);
            Thumbnails.of(resize).forceSize(394, 558).toFile(resize);
            imgList.add(new BufferedImgBean(resize, left, top));
            i++;
        }
        System.out.println(imgList);
        return createColorImg(794, 1123, new Color(255, 255, 255), outPath, imgList, null);
    }

    private static String rebuild(String str1) {
        String suffix = str1.substring(str1.lastIndexOf(".")+1);
        String res = str1.substring(0, str1.lastIndexOf("."))+"-rebuild."+suffix;

        try {
            BufferedImage image = ImageIO.read(new File(str1));
            int width = image.getWidth();
            int height = image.getHeight();
            if(width>height) { //如果宽大于高，则需要旋转
                BufferedImage newImage = RotateImageTools.rotate(image, 90);
                ImageIO.write(newImage, suffix, new File(res));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }

    /**
     * @param width        图片宽度
     * @param height 图片高度
     * @param color 背景颜色
     * @param outPath 输出路径，可为空
     * @param imgBeanList 图片列表
     * @param textBeanList 文字列表
     * @return
     */
    public static File createColorImg(final int width, final int height, final Color color, String outPath, List<BufferedImgBean> imgBeanList, List<TextBean> textBeanList) {
        if (width < 1 || height < 1) {
            return null;
        }


        File file = null;
        FileOutputStream out = null;
        try {
            //构造一个类型为预定义图像类型之一的 BufferedImage。 宽度为第一只的宽度，高度为各个图片高度之和
            BufferedImage bufferedImage = createColorImg(width, height, color);
            //创建输出流
            if (isNotEmpty(outPath)) {
                file = new File(outPath);
            } else {
                file = File.createTempFile("joimImg", UniqId.getUid() + ".jpg");
            }
            out = new FileOutputStream(file);

            //进行图片的处理
            if (isNotEmptyCollection(imgBeanList)) {
                Graphics graphics = bufferedImage.createGraphics();
                for (BufferedImgBean imgBean : imgBeanList) {
                    if (null == imgBean.getFile()) {
                        continue;
                    }
                    BufferedImage image = ImageIO.read(imgBean.getRealFile());
                    int img_height = imgBean.getImg_height();
                    int img_width = imgBean.getImg_width();
                    if (img_height < 1) {
                        img_height = image.getHeight();
                    }
                    if (img_width < 1) {
                        img_width = image.getWidth();
                    }
                    graphics.drawImage(image, imgBean.getLeft(), imgBean.getTop(), img_width, img_height, null);
                }

                // 释放此图形的上下文以及它使用的所有系统资源。
                graphics.dispose();
            }

            //进行文字的处理
            if (isNotEmptyCollection(textBeanList)) {
                for (TextBean textBean : textBeanList) {
                    if (!isNotEmpty(textBean.getText())) {
                        continue;
                    }
                    //字体
                    Graphics2D graphics2D = bufferedImage.createGraphics();
                    Font font = null;
                    if (null != textBean.getFont()) {
                        font = textBean.getFont();
                    } else {
                        font = FontUtil.getDefaultFont();
                    }
                    graphics2D.setFont(font);

                    graphics2D.setColor(textBean.getColor());
                    graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    //获取字符高度
                    int strHeight = graphics2D.getFontMetrics().getHeight();

                    //得到文字的长度
                    int max_length = FontUtil.getStringLength(font, textBean.getText());
                    //判断是否控制了文字长度
                    if (textBean.getMax_width() > 0 && max_length > textBean.getMax_width()) {

                        //初始高度
                        int inity = textBean.getTop();
                        //循环得到字符串的长度
                        String newStr = textBean.getText();


                        //如果不是最后的长度
                        while (!FontUtil.isMaxStr(font, newStr, textBean.getMax_width())) {
                            String istr = FontUtil.strSplitMaxLenth(font, newStr, textBean.getMax_width());
                            //直接绘制
                            graphics2D.drawString(istr, textBean.getLeft(), inity);
                            //完成后，进行初始化
                            newStr = newStr.replaceFirst(istr, "");

                            //高度再次重构
                            inity += strHeight + textBean.getLine_height();
                        }
                        //最后补一次
                        if (newStr.length() > 0) {
                            graphics2D.drawString(newStr, textBean.getLeft(), inity);
                        }

                    } else {
                        graphics2D.drawString(textBean.getText(), textBean.getLeft(), textBean.getTop());
                    }
                    // 释放此图形的上下文以及它使用的所有系统资源。
                    graphics2D.dispose();
                }
            }


            //将绘制的图像生成至输出流
            JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
            encoder.encode(bufferedImage);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                //关闭输出流
                if (null != out) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    //创建一张底图
    public static BufferedImage createColorImg(int width, int height, Color color) {
        //创建一个图片缓冲区
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_BGR);
        //获取图片处理对象
        Graphics graphics = bufferedImage.getGraphics();
        //填充背景色
        graphics.setColor(color);
        graphics.fillRect(0, 0, width, height);

        return bufferedImage;
    }

}
