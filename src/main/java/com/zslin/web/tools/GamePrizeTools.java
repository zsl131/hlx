package com.zslin.web.tools;

import com.zslin.web.model.Account;
import com.zslin.web.model.GamePrize;
import com.zslin.web.model.Prize;
import com.zslin.web.service.IAccountService;
import com.zslin.web.service.IGamePrizeService;
import com.zslin.web.service.IPrizeService;
import com.zslin.wx.dbtools.OwnPrizeTools;
import com.zslin.wx.dbtools.ScoreAdditionalDto;
import com.zslin.wx.dbtools.ScoreTools;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/30 15:08.
 * 游戏中奖名单工具类
 */
@Component
public class GamePrizeTools {

    @Autowired
    private IGamePrizeService gamePrizeService;

    @Autowired
    private IPrizeService prizeService;

    @Autowired
    private ScoreTools scoreTools;

    @Autowired
    private OwnPrizeTools ownPrizeTools;

    @Autowired
    private IAccountService accountService;

    public void processMessage(String openid, String code) {
        GamePrize gp = gamePrizeService.findByCode(code);
        Account a = accountService.findByOpenid(openid);
        if(gp==null) {
            gp = new GamePrize();
            gp.setStatus("0");
            gp.setAccountId(a.getId());
            gp.setCode(code);
            gp.setHeadimg(a.getHeadimgurl());
            gp.setNickname(a.getNickname());
            gp.setOpenid(openid);
            gamePrizeService.save(gp);
        } else {
            if(gp.getOpenid()==null || "".equals(gp.getOpenid())) {
                gp.setAccountId(a.getId());
                gp.setHeadimg(a.getHeadimgurl());
                gp.setNickname(a.getNickname());
                gp.setOpenid(openid);
                gamePrizeService.save(gp);
            }
            if("0".equals(gp.getStatus()) && gp.getPrizeId()!=null && gp.getPrizeId()>0) { //如果未兑奖
                Prize prizeObj = prizeService.findOne(gp.getPrizeId());
                if(prizeObj!=null) {
                    if("1".equals(prizeObj.getType())) { //如果是积分
                        scoreTools.processScoreByAmount(openid, prizeObj.getWorth(), "玩游戏："+gp.getLevel(),
                                new ScoreAdditionalDto("游戏名称", gp.getTitle()),
                                new ScoreAdditionalDto("游戏批次", gp.getBatchNo()));
                    } else {
                        ownPrizeTools.processPrize(openid, prizeObj, "玩游戏："+gp.getLevel(),
                                new ScoreAdditionalDto("游戏名称", gp.getTitle()),
                                new ScoreAdditionalDto("游戏批次", gp.getBatchNo()));
                    }
                    gp.setStatus("1");
                    gamePrizeService.save(gp);
                }
            }
        }
    }

    public void processExcel(String title, String batchNo, InputStream is) {
        try {
            HSSFWorkbook book = new HSSFWorkbook(is);
            HSSFSheet sheet = book.getSheetAt(0);
            for(int i=0; i<sheet.getLastRowNum()+1; i++) {
                HSSFRow row = sheet.getRow(i);
                String code = row.getCell(0).getStringCellValue(); //第一个单元格，中奖码
                String level = row.getCell(1).getStringCellValue(); //奖品等级
                String prize = row.getCell(2).getStringCellValue(); //奖品名称，格式应该为：1:5个积分
                String winningTime = row.getCell(7).getStringCellValue(); //中奖时间
                processSinglePrize(title, batchNo, code, level, prize, winningTime);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理单个中奖信息
     * @param code 中奖码
     * @param level 等级
     * @param prize 名称
     * @param winningTime 时间
     */
    private void processSinglePrize(String title, String batchNo, String code, String level, String prize, String winningTime) {
        prize = prize.replace("：", ":"); //先替换冒号
        GamePrize gp = gamePrizeService.findByCode(code);
        if(prize.indexOf(":")<=0) {return;}
        String [] prize_array = prize.split(":");
        Integer prizeId = Integer.parseInt(prize_array[0]); //礼物ID
        Prize prizeObj = prizeService.findOne(prizeId);
        if(prizeObj==null) {return;}
        if(gp==null) {
            gp = new GamePrize();
            gp.setCode(code);
            gp.setStatus("0");
        } else {
            String openid = gp.getOpenid(); String status = gp.getStatus();
            if(openid!=null && !"".equals(openid) && !"1".equals(status)) { //如果有openid，并且未兑奖
                if(prizeObj!=null) {
                    if("1".equals(prizeObj.getType())) { //如果是积分
                        scoreTools.processScoreByAmount(openid, prizeObj.getWorth(), "玩游戏："+level,
                                new ScoreAdditionalDto("游戏名称", title),
                                new ScoreAdditionalDto("游戏批次", batchNo));
                    } else {
                        ownPrizeTools.processPrize(openid, prizeObj, "玩游戏："+level,
                                new ScoreAdditionalDto("游戏名称", title),
                                new ScoreAdditionalDto("游戏批次", batchNo));
                    }
                    gp.setStatus("1");
                }
            }
        }
        gp.setLevel(level);
        gp.setPrizeId(prizeId);
        gp.setPrizeType(prizeObj.getType());
        gp.setPrizeName(prize_array[1]);
        gp.setWinningTime(winningTime);
        gp.setWinnerLong(str2Long(winningTime));
        gp.setTitle(title);
        gp.setBatchNo(batchNo);
        gamePrizeService.save(gp); //保存
    }

    private Long str2Long(String winningTime) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            return sdf.parse(winningTime).getTime();
        } catch (ParseException e) {
            return 0l;
        }
    }
}
