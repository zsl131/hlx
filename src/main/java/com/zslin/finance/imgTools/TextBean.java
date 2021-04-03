package com.zslin.finance.imgTools;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import sun.font.FontDesignMetrics;

import java.awt.*;

@Getter
@Setter
@ToString
public class TextBean {

    //字体
    private Font font;

    //颜色
    private Color color;

    //文字
    private String text;

    //距左距离
    private int left;

    //距上距离
    private int top;

    //最大宽度
    private int max_width;

    //上下行间距
    private int line_height;


    public TextBean(Font font, Color color, String text, int left, int top) {
        this.font = font;
        this.color = color;
        this.text = text;
        this.left = left;
        this.top = top;
    }

    public TextBean(Font font, Color color, String text, int left, int top, int max_width, int line_height) {
        this.font = font;
        this.color = color;
        this.text = text;
        this.left = left;
        this.top = top;
        this.max_width = max_width;
        this.line_height = line_height;
    }

    public TextBean(Color color, String text, int left, int top) {
        this.text = text;
        this.color = color;
        this.left = left;
        this.top = top;
    }


    //得到最终的高度
    public int getMaxHeight() {
        //初始高度
        int inity = 0;

        FontDesignMetrics metrics = FontDesignMetrics.getMetrics(font);
        //获取字符高度
        int strHeight = metrics.getHeight();
        //得到文字的长度
        int max_length = FontUtil.getStringLength(font, getText());
        //判断是否控制了文字长度
        if (getMax_width() > 0 && max_length > getMax_width()) {


            //循环得到字符串的长度
            String newStr = getText();
            //如果不是最后的长度
            while (!FontUtil.isMaxStr(font, newStr, getMax_width())) {
                String istr = FontUtil.strSplitMaxLenth(font, newStr, getMax_width());
                //完成后，进行初始化
                newStr = newStr.replaceFirst(istr, "");

                //高度再次重构
                inity += strHeight + getLine_height();
            }
            //最后补一次
            if (newStr.length() > 0) {
                inity += strHeight;
            }
        }
        return inity;
    }
}
