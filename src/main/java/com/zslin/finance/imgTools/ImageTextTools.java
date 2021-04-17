package com.zslin.finance.imgTools;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * 图片文字处理
 */
public class ImageTextTools {

    public static void writeText(String path, String text) throws Exception {
        File file = new File(path);
        BufferedImage image = ImageIO.read(file);
        Graphics2D graphics2D = image.createGraphics();

//        graphics2D.setFont(font);

        Font font = new Font("微软雅黑",Font.BOLD,18);
        FontRenderContext frc = graphics2D.getFontRenderContext();


        graphics2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        TextLayout tl = new TextLayout(text, font, frc);

        graphics2D.setColor(new Color(255, 255, 255));
        graphics2D.setFont(font);
        graphics2D.drawString(text, 10, 30);

        //FontMetrics fm = sun.font.FontDesignMetrics.getMetrics(font);

        Shape sha = tl.getOutline(AffineTransform.getTranslateInstance(10,30));

        //graphics2D.drawString(text, 10, 100);

        graphics2D.setColor(Color.BLACK);
        graphics2D.draw(sha);
        // 释放此图形的上下文以及它使用的所有系统资源。
        graphics2D.dispose();
        //将绘制的图像生成至输出流
        /*JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(new FileOutputStream(file));
        encoder.encode(image);*/
        ImageIO.write(image, buildImageType(path), file);
    }

    /** 获取图片类型 */
    public static String buildImageType(String path) {
        try {
            return path.substring(path.lastIndexOf(".")+1);
        } catch (Exception e) {
            return "";
        }
    }
}
