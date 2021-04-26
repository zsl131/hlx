package com.zslin.finance.tools;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import com.zslin.basic.tools.ConfigTools;
import com.zslin.basic.tools.NormalTools;
import com.zslin.finance.dao.IFinanceVoucherDao;
import com.zslin.finance.model.FinanceDetail;
import com.zslin.finance.model.FinanceVoucher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by zsl on 2019/1/5.
 */
@Component
public class PDFTools {

    @Autowired
    private ConfigTools configTools;

    @Autowired
    private IFinanceVoucherDao financeVoucherDao;

    private static int TABLE_HEIGHT = 30; //表格高度
    private static int MAX_DETAIL_LEN = 6; //最多6条数据
    private float chapWidth = 0f, chapHeight = 0f;

    public void buildPDF(OutputStream os, List<FinanceDetail> detailList) {
        //String ticketNo = record.getTicketNo();
        String storeName = buildDepName(detailList); //店铺名称
        String ticketNo = "             "; //暂时不用编号
        try {
            //凭证列表
            List<FinanceVoucher> voucherList = financeVoucherDao.findByIds(buildIds(detailList));

            Rectangle pageSize = new Rectangle(PageSize.A4.getWidth(), PageSize.A4.getHeight());
            pageSize.rotate();
            //设置纸张大小
            Document document = new Document(pageSize);
            //建立一个书写器，与document对象关联
            PdfWriter writer = PdfWriter.getInstance(document, os);
            document.setMargins(40,40,25,25);

            document.open();

            writePage(writer, ticketNo);
//            ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("123456789", getFont(10)), document.left(), document.top()+2, 0);
            //将章节二纵向显示
            document.setPageSize(pageSize);
            chapWidth = PageSize.A4.getWidth();
            chapHeight = PageSize.A4.getHeight();
            Chapter chap = new Chapter(1);
            chap.add(buildHeadParagraph("0", ticketNo)); //标题，默认都为费用报销单
            chap.add(buildBlankP()); //空行
            chap.add(buildNameDateP(NormalTools.curDate("yyyyMMdd"), storeName)); //名称日期
            chap.add(buildBlankP()); //空行
            chap.add(buildTable(detailList, voucherList)); //数据表格
            chap.add(buildBlankP()); //空行
            chap.add(buildBlankP()); //空行
            chap.add(drawLine()); //水平线
            chap.add(buildBlankP()); //空行


//            writePage(writer, document, ticketNo);

            addVoucher(chap, voucherList);

            /*for(FinanceVoucher voucher:voucherList) {
                chap.add(buildImageChap(voucher));
                //writePage(writer, document, ticketNo);
            }*/

            document.add(chap);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 获取所在店铺名称 */
    private String buildDepName(List<FinanceDetail> detailList) {
        try {
            return detailList.get(0).getStoreName();
        } catch (Exception e) {
            return "出错";
        }
    }

    private Integer [] buildIds(List<FinanceDetail> detailList) {
        Integer [] res = new Integer[detailList.size()];
        int index = 0;
        for(FinanceDetail d : detailList) {
            res[index++] = d.getId();
        }
        return res;
    }

    private void writePage(PdfWriter writer, String ticketNo) {
        /*PdfContentByte headAndFootPdfContent = writer.getDirectContent();
        headAndFootPdfContent.saveState();
        headAndFootPdfContent.beginText();
        headAndFootPdfContent.setFontAndSize(getBaseFont(), 8);
//        headAndFootPdfContent.setColor
        //文档页头信息设置  
        float x = document.top(-20);
        float x1 = document.top(-5);
        //页头信息中间  
        headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_LEFT, "编号："+ticketNo, (document.right() + document.left()) / 2, x, 0);
//            headAndFootPdfContent.showTextAligned(Element.ALIGN_CENTER, "123456789", document.left(), document.top()+2, 0);
        headAndFootPdfContent.endText();
        headAndFootPdfContent.restoreState();*/

        writer.setPageEvent(new PdfPageEventHelper() {

            //每一页开头
            @Override
            public void onStartPage(PdfWriter pdfWriter, Document document) {
                /*PdfContentByte headAndFootPdfContent = pdfWriter.getDirectContent();
                headAndFootPdfContent.saveState();
                headAndFootPdfContent.beginText();
                headAndFootPdfContent.setFontAndSize(getBaseFont(), 8);
                //文档页头信息设置  
                float x = document.top(-20);
                //页头信息中间  
                headAndFootPdfContent.showTextAligned(PdfContentByte.ALIGN_LEFT, "编号："+ticketNo, document.left(), x, 0);
//            headAndFootPdfContent.showTextAligned(Element.ALIGN_CENTER, "123456789", document.left(), document.top()+2, 0);
                headAndFootPdfContent.endText();
                headAndFootPdfContent.restoreState();*/
            }

            @Override
            public void onEndPage(PdfWriter pdfWriter, Document document) {

            }
        });
    }

    /** 凭证图片 */
    private Image buildImageChap(FinanceVoucher voucher) {
        try {
            float margin = 80;
            Image image = Image.getInstance(configTools.getFilePath()+voucher.getPicPath());
            float realWidth = image.getWidth(); float realHeight = image.getHeight();
            if(realWidth>realHeight) { //横图，需要旋转
                image.scaleToFit((chapHeight-margin) / 2f, (chapWidth-margin) / 2f);
                image.setRotationDegrees(90); //旋转90度
            } else {
                image.scaleToFit((chapWidth-margin) / 2f, (chapHeight-margin) / 2f);
            }
//            image.setAlignment(Image.MIDDLE);
            //image.setPaddingTop(-100);
            return image;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取凭证条数
    private int buildVoucherLen(List<FinanceVoucher> voucherList, int detailId) {
        int res = 0;
        for(FinanceVoucher v : voucherList) {
            if(detailId == v.getDetailId()) {res ++;}
        }
        return res;
    }

    private void addVoucher(Chapter chap, List<FinanceVoucher> voucherList) {
        float [] widths = {270f,270f}; //总长540
        float totalWidth = 0f;
        for(float w : widths) {totalWidth += w;}
        PdfPTable table =null;
        int index = 0;
        for(FinanceVoucher voucher : voucherList) {
            if(table==null || index%2==0) {
                if(table!=null) {chap.add(table);}
                table = new PdfPTable(widths);
                table.setTotalWidth(totalWidth);
                table.setLockedWidth(true);
                table.setHorizontalAlignment(Element.ALIGN_CENTER);
            }
            PdfPCell pdfCell = new PdfPCell(); //表格的单元格
            pdfCell.addElement(buildImageChap(voucher));
            table.addCell(pdfCell);
            index ++;
        }
        if(table!=null) { //如果表格不为空，则需要添加一次
            if(index%2==1) { //如果是基数时需要增加一个空格，否则无法输出
                PdfPCell pdfCell = new PdfPCell(); //表格的单元格
                pdfCell.addElement(new Chunk(" "));
                table.addCell(pdfCell);
            }
            chap.add(table);
        }
    }

    private PdfPTable buildTable(List<FinanceDetail> detailList, List<FinanceVoucher> voucherList) {
        FinanceDetail someone = detailList.get(0);
        float [] widths = {56f,220,57.5f,52f,52f,28f,49f}; //
        float totalWidth = 0f;
        for(float w : widths) {totalWidth += w;}
        PdfPTable table = new PdfPTable(widths);
        table.setTotalWidth(totalWidth);
        table.setLockedWidth(true);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        String [] header = {"日期", "摘要（简要说明）", "单价(元)", "数量", "金额(元)", "附单(张)", "收货人签字"};
        for(String h : header) {
            PdfPCell pdfCell = new PdfPCell(); //表格的单元格
            pdfCell.setMinimumHeight(TABLE_HEIGHT);
            pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            Paragraph paragraph = new Paragraph(h, getFont(12));
            pdfCell.setPhrase(paragraph);
            table.addCell(pdfCell);
        }
        Integer totalCount = 0;
        Float totalMoney = 0f;
        int len = 0;
        for(FinanceDetail fd : detailList) {
            len ++;
            Float total = fd.getTotalMoney(); //小计金额
            Integer count = buildVoucherLen(voucherList, fd.getId()); //TODO 单据张数，暂时设置为0
            String [] data = {rebuildDate(fd.getCreateDay()), fd.getTitle(), formatMoney(fd.getPrice()), fd.getAmount()+"", formatMoney(fd.getTotalMoney()), count+"", ""};
            totalCount += count; totalMoney += total;

            int index = 0;
            for(String h : data) {
                PdfPCell pdfCell = new PdfPCell(); //表格的单元格
                pdfCell.setMinimumHeight(TABLE_HEIGHT);
                if(index==1) {
                    pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                } else {
                    pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                }
                pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                if(index==6) {
                    table.addCell(buildSimpleSign(fd.getConfirmSign()));
                } else {
                    Paragraph paragraph;
                    if (index == 4) {
                        Font f = getFont(12);
                        f.setStyle("bold");
                        paragraph = new Paragraph(h, f);
                    } else {
                        paragraph = new Paragraph(h, getFont(12));
                    }
                    pdfCell.setPhrase(paragraph);
                    table.addCell(pdfCell);
                }

                index ++;
            }
        }

        for(int i=0;i<(MAX_DETAIL_LEN-len);i++) {
            String [] data = {"", "", "", "", "", "", ""};

            int index = 0;
            for(String h : data) {
                PdfPCell pdfCell = new PdfPCell(); //表格的单元格
                pdfCell.setMinimumHeight(TABLE_HEIGHT);
                if(index==1) {
                    pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                } else {
                    pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
                }
                pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                Paragraph paragraph ;
                if(index == 5) {
                    Font f = getFont(12);
                    f.setStyle("bold");
                    paragraph = new Paragraph(h, f);
                } else {
                    paragraph = new Paragraph(h, getFont(12));
                }
                pdfCell.setPhrase(paragraph);
                table.addCell(pdfCell);
                index ++;
            }
        }

        table.addCell(buildHeji()); //合计
        table.addCell(buildTotalMoney(totalMoney)); //大写金额
        table.addCell(buildTotalMoneyNum(totalMoney)); //小写金额
        table.addCell(buildTotalCount(totalCount)); //总单据张数
        table.addCell(""); //空格，收货人签字占位符
//        table.addCell(buildApply(someone.getUsername())); //申请人
        table.addCell(buildApply()); //申请人
        table.addCell(buildSimpleSign(someone.getUserSignPath())); //申请人签名

        table.addCell(buildSimpleName("审核人：")); //审核人
        table.addCell(buildSimpleSign(someone.getVerifySignPath())); //审核人签名
//
        table.addCell(buildSimpleName("财务：")); //财务
        table.addCell(buildSimpleSign(someone.getVoucherSign(), 2)); //财务人签名

//        table.addCell(buildVerify(someone.getVerifyName())); //审核人
//        table.addCell(buildAdmin(someone.getVoucherName())); //主管
        return table;
    }

    /*private PdfPCell buildAdmin(String adminName) {
        PdfPCell pdfCell = new PdfPCell(); //表格的单元格
        pdfCell.setMinimumHeight(TABLE_HEIGHT);
        pdfCell.setRowspan(1);
        pdfCell.setColspan(3);
        pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Paragraph p = new Paragraph();
        Chunk c = new Chunk("主管：", getFont(12));
        Chunk c2 = new Chunk(adminName, getFont(12));
        p.add(c);
        p.add(c2);
        pdfCell.setPhrase(p);
        return pdfCell;
    }*/

    private PdfPCell buildSimpleName(String name) {
        PdfPCell pdfCell = new PdfPCell(); //表格的单元格
        pdfCell.setMinimumHeight(TABLE_HEIGHT);
        pdfCell.setRowspan(1);
        pdfCell.setColspan(1);
        pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Paragraph p = new Paragraph();
        Chunk c = new Chunk(name, getFont(12));
//        Chunk c2 = new Chunk(applyName, getFont(12));
        p.add(c);
        pdfCell.addElement(p);
        pdfCell.setBorderWidthRight(0);
        return pdfCell;
    }

    private PdfPCell buildApply() {
        return buildSimpleName("申请人：");
    }

    private PdfPCell buildSimpleSign(String signPath) {
        return buildSimpleSign(signPath, 1, 26f);
    }

    private PdfPCell buildSimpleSign(String signPath, int colspan) {
        return buildSimpleSign(signPath, colspan, 26f);
    }

    private PdfPCell buildSimpleSign(String signPath, float imgHeight) {
        return buildSimpleSign(signPath, 1, imgHeight);
    }

    private PdfPCell buildSimpleSign(String signPath, int colspan, float imgHeight) {
        PdfPCell pdfCell = new PdfPCell(); //表格的单元格
        pdfCell.setMinimumHeight(TABLE_HEIGHT);
        pdfCell.setRowspan(1);
        pdfCell.setColspan(colspan);
        pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        try {
            //System.out.println(configTools.getFilePath()+applyName);
            Image img = Image.getInstance(configTools.getFilePath() + signPath);

            img.scaleAbsolute(rebuildRectByHeight(img, imgHeight));
            img.setAlignment(Element.ALIGN_LEFT);
            img.setBorder(0);
            //p.add(img);
            pdfCell.setBorderWidthLeft(0);

            pdfCell.addElement(img);
        } catch (Exception e) {
            e.printStackTrace();
            Paragraph p = new Paragraph();
            Chunk c = new Chunk("签名异常", getFont(12));
            p.add(c);
            pdfCell.setPhrase(p);
        }
        return pdfCell;
    }

    /** 指定height，缩放 width*/
    private Rectangle rebuildRectByHeight(Image img, Float height) {
        Float w = img.getWidth(); Float h = img.getHeight();
        return new Rectangle(w*height/h, height);
    }

    /** 指定width，缩放height */
    private Rectangle rebuildRectByWidth(Image img, Float width) {
        Float w = img.getWidth(); Float h = img.getHeight();
        return new Rectangle(h*width/w, width);
    }

    /*private PdfPCell buildVerify(String verifyName) {
        verifyName = verifyName==null?"":verifyName;
        PdfPCell pdfCell = new PdfPCell(); //表格的单元格
        pdfCell.setMinimumHeight(TABLE_HEIGHT);
        pdfCell.setRowspan(1);
        pdfCell.setColspan(2);
        pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Paragraph p = new Paragraph();
        Chunk c = new Chunk("审核人：", getFont(12));
        Chunk c2 = new Chunk(verifyName, getFont(12));
        p.add(c);
        p.add(c2);
        pdfCell.setPhrase(p);
        return pdfCell;
    }*/

    private PdfPCell buildTotalCount(int count) {
        PdfPCell pdfCell = new PdfPCell(); //表格的单元格
        pdfCell.setMinimumHeight(TABLE_HEIGHT);
        pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Paragraph paragraph = new Paragraph(count+"", getFont(12));
        pdfCell.setPhrase(paragraph);
        return pdfCell;
    }

    private PdfPCell buildTotalMoneyNum(float totalMoney) {
        PdfPCell pdfCell = new PdfPCell(); //表格的单元格
        pdfCell.setMinimumHeight(TABLE_HEIGHT);
        pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Font f = getFont(12);
        f.setStyle("bold");
        Paragraph paragraph = new Paragraph(formatMoney(totalMoney), f);
        pdfCell.setPhrase(paragraph);
        return pdfCell;
    }

    private PdfPCell buildTotalMoney(float totalMoney) {
        totalMoney = Float.parseFloat(formatMoney(totalMoney)); //转换成2位小数
        PdfPCell pdfCell = new PdfPCell(); //表格的单元格
        pdfCell.setRowspan(1);
        pdfCell.setColspan(3);
        pdfCell.setMinimumHeight(TABLE_HEIGHT);
        pdfCell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Paragraph p = new Paragraph();
        Chunk c = new Chunk("（大写）", getFont(12));
        Chunk c2 = new Chunk(MoneyTools.digitUppercase(totalMoney), getFont(12));
        p.add(c);
        p.add(c2);
        pdfCell.setPhrase(p);
        return pdfCell;
    }

    private PdfPCell buildHeji() {
        PdfPCell pdfCell = new PdfPCell(); //表格的单元格
        pdfCell.setMinimumHeight(TABLE_HEIGHT);
        pdfCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        pdfCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        Paragraph paragraph = new Paragraph("合计", getFont(12));
        pdfCell.setPhrase(paragraph);
        return pdfCell;
    }

    private String formatMoney(float d) {
        DecimalFormat df = new DecimalFormat("#.00");
        return df.format(d);
    }

    private String rebuildDate(String recordDate) {
        if(recordDate.contains("-")) {recordDate = recordDate.replaceAll("-", "");}
        StringBuffer sb = new StringBuffer();
        sb.append(recordDate.substring(0, 4)).append(".").append(recordDate.substring(4, 6)).
                append(".").append(recordDate.substring(6, 8));
        return sb.toString();
    }

    private Paragraph drawLine() {
        Paragraph p = new Paragraph();
        p.setAlignment(Element.ALIGN_LEFT);
        StringBuffer sb = new StringBuffer();
        for(int i=0;i<=113;i++) {
            sb.append("-");
        }
        Chunk c = new Chunk(sb.toString(), getFont(12));
        p.add(c);
        return p;
    }

    private Paragraph buildBlankP() {
        Paragraph p = new Paragraph();
        p.setAlignment(Element.ALIGN_LEFT);
        Chunk c = new Chunk("  ", getFont(12));
        p.add(c);
        return p;
    }

    private Paragraph buildNameDateP(String recordDate, String depName) {
        Paragraph p = new Paragraph();
        p.setAlignment(Element.ALIGN_LEFT);
        Chunk c = new Chunk("公司名称：问渠餐饮 ", getFont(12));
        Chunk cb = new Chunk("                     部门："+depName+"                               ", getFont(12));
        Chunk cd = new Chunk(buildDate(recordDate), getFont(12));
        p.add(c);
        p.add(cb);
        p.add(cd);
        return p;
    }

    private String buildDate(String recordDate) {
        StringBuffer sb = new StringBuffer();
        String spe = "    ";
        sb.append(recordDate.substring(0, 4)).append(spe).append("年")
                .append(spe).append(recordDate.substring(4, 6)).append(spe).append("月")
                .append(spe).append(recordDate.substring(6, 8)).append(spe).append("日");
        return sb.toString();
    }

    private Paragraph buildHeadParagraph(String titleFlag, String ticketNo) {
        Paragraph titleP = new Paragraph();
        titleP.setAlignment(Element.ALIGN_RIGHT);
        Font titleFont = new Font(getBaseFont(), 24);
//            titleFont.setStyle("bold");
        titleFont.setStyle("underline");
        String titleStr = "1".equals(titleFlag)?"  收 益 入 帐 单  ":"  费 用 报 销 单  ";
        Chunk titleC = new Chunk(titleStr, titleFont);
        Chunk black = new Chunk("                    ", getFont(16));
        Chunk ticketC = new Chunk("编号：", getFont(12));
        Font f2 = getFont(16);
        f2.setStyle("underline");
        Chunk ticketC2 = new Chunk("  "+ticketNo+"  ", f2);

        Chunk ticketC3 = new Chunk("号", getFont(12));

        titleP.add(titleC);
        titleP.add(black);
        titleP.add(ticketC);
        titleP.add(ticketC2);
        titleP.add(ticketC3);
        return titleP;
    }

    private BaseFont getBaseFont(){
        try {
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            return bfChinese;
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Font getFont(int size) {
        try {
            BaseFont bfChinese = getBaseFont();
            Font font = new Font(bfChinese, size);
            return font;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
