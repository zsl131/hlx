package com.zslin.web.model;

import javax.persistence.*;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/30 14:32.
 * 游戏奖品
 */
@Entity
@Table(name = "t_game_prize")
public class GamePrize {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /** 游戏名称，用于分批次显示 */
    private String title;

    /** 批次号，一般用日期 */
    @Column(name = "batch_no")
    private String batchNo;

    /** 状态；0-未兑奖；1-已兑奖 */
    private String status;

    /** 中奖人ID */
    @Column(name = "account_id")
    private Integer accountId;

    /** 中奖人昵称 */
    private String nickname;

    /** 中奖人头像 */
    private String headimg;

    /** openid */
    private String openid;

    /** 中奖时间 */
    @Column(name = "winning_time")
    private String winningTime;

    /** 中奖时间，用于排序 */
    @Column(name = "winner_long")
    private Long winnerLong;

    /** 中奖码，唯一 */
    private String code;

    /** 中奖等级，只是显示 */
    private String level;

    /** 中奖名称，用于显示 */
    @Column(name = "prize_name")
    private String prizeName;

    /** 中奖类型，与Prize中的id一致 */
    @Column(name = "prize_id")
    private Integer prizeId;

    /** 礼品类型，与Prize中的type一致 */
    @Column(name = "prize_type")
    private String prizeType;

    public String getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }

    public Integer getPrizeId() {
        return prizeId;
    }

    public void setPrizeId(Integer prizeId) {
        this.prizeId = prizeId;
    }

    public Long getWinnerLong() {
        return winnerLong;
    }

    public void setWinnerLong(Long winnerLong) {
        this.winnerLong = winnerLong;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getWinningTime() {
        return winningTime;
    }

    public void setWinningTime(String winningTime) {
        this.winningTime = winningTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }
}
