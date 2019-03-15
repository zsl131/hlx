package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.BuffetOrder;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/2 23:27.
 */
public interface IBuffetOrderService extends BaseRepository<BuffetOrder, Integer>, JpaSpecificationExecutor<BuffetOrder> {

    BuffetOrder findByNo(String no);

    @Query("SELECT SUM(commodityCount) FROM BuffetOrder WHERE status in ('2', '4', '5')")
    Integer queryCount();

    //查询折扣次数
    @Query("SELECT COUNT(id) FROM BuffetOrder b WHERE b.discountReason=?1 AND b.discountType='2' AND b.type='4'")
    Integer findFriendCount(String phone);

    //获取已经完成的订单
    @Query("SELECT COUNT(id) FROM BuffetOrder b WHERE b.isSelf='1' AND b.createDay>=?1 AND b.createDay<=?2 AND b.payType in ?3 AND (b.status='4' OR b.status='5')")
    Integer findFinishSelfCount(String startDay, String endDay, String... payType);

    //获取已经完成的订单
    @Query("SELECT COUNT(id) FROM BuffetOrder b WHERE b.isSelf='0' AND b.createDay>=?1 AND b.createDay<=?2 AND b.payType in ?3 AND b.status='2'")
    Integer findFinishOutCount(String startDay, String endDay, String... payType);

    //获取订单数量
    @Query("SELECT COUNT(id) FROM BuffetOrder b WHERE b.createDay>=?1 AND b.createDay<=?2 AND b.isSelf=?3 AND b.status in ?4")
    Integer findCount(String startDay, String endDay, String isSelf, String... status);

    @Query("SELECT SUM(b.totalMoney) FROM BuffetOrder b WHERE b.createDay>=?1 AND b.createDay<=?2 AND b.isSelf=?3 AND b.payType=?4 AND b.status in ?5")
    Double sumMoney(String startDay, String endDay, String isSelf, String payType, String... status);

    //计算时间段内总收入
    @Query("SELECT SUM(totalMoney+discountMoney) FROM BuffetOrder WHERE STATUS IN ('2', '3', '4', '5') AND createTime BETWEEN ?1 AND ?2")
    Float queryTotalMoneyByDay(String startTime, String endTime);

    @Query("SELECT SUM(discountMoney) FROM BuffetOrder WHERE type='6' AND STATUS IN ('2', '3', '4', '5') AND createTime BETWEEN ?1 AND ?2")
    Float queryMoneyByTicket(String startTime, String endTime);

    @Query("SELECT SUM(discountMoney) FROM BuffetOrder WHERE type='3' AND STATUS IN ('2', '3', '4', '5') AND createTime BETWEEN ?1 AND ?2")
    Float queryMoneyByMeiTuan(String startTime, String endTime);

    @Query("SELECT SUM(discountMoney) FROM BuffetOrder WHERE type='9' AND STATUS IN ('2', '3', '4', '5') AND createTime BETWEEN ?1 AND ?2")
    Float queryMoneyByFfan(String startTime, String endTime);

    @Query("SELECT SUM(totalMoney) FROM BuffetOrder WHERE payType=?3 AND STATUS IN ('2', '3', '4', '5') AND createTime BETWEEN ?1 AND ?2")
    Float queryTotalMoneyByPayType(String startTime, String endTime, String payType);

    @Query("SELECT SUM(surplusBond) FROM BuffetOrder WHERE STATUS IN ('2', '3', '4', '5') AND createTime BETWEEN ?1 AND ?2")
    Float queryBondMoney(String startTime, String endTime);

    @Query("SELECT SUM(discountMoney) FROM BuffetOrder WHERE STATUS IN ('2', '3', '4', '5') AND discountType='10' AND createDay=?1")
    Float queryDiscountMoneyByTime(String createDay);

    @Query("SELECT SUM(discountMoney) FROM BuffetOrder WHERE STATUS IN ('2', '3', '4', '5') AND discountType='10' AND createTime BETWEEN ?1 AND ?2")
    Float queryDiscountMoneyByTime(String startTime, String endTime);

    @Query("SELECT SUM(discountMoney) FROM BuffetOrder WHERE STATUS IN ('2', '3', '4', '5') AND discountType='12' AND createTime BETWEEN ?1 AND ?2")
    Float queryDiscountDayMoneyByTime(String startTime, String endTime);

    @Query("FROM BuffetOrder WHERE type='6' AND STATUS IN ('2', '3', '4', '5') AND createTime BETWEEN ?1 AND ?2")
    List<BuffetOrder> findByTicket(String startTime, String endTime);

    @Query("FROM BuffetOrder WHERE type='3' AND STATUS IN ('2', '3', '4', '5') AND createTime BETWEEN ?1 AND ?2")
    List<BuffetOrder> findByMeiTuan(String startTime, String endTime);

    @Query("FROM BuffetOrder WHERE type='9' AND STATUS IN ('2', '3', '4', '5') AND createTime BETWEEN ?1 AND ?2")
    List<BuffetOrder> findByFfan(String startTime, String endTime);

    //获取从会员账户扣款的金额
    @Query("SELECT SUM(discountMoney) FROM BuffetOrder WHERE type='5' AND STATUS IN ('2', '3', '4', '5') AND createTime BETWEEN ?1 AND ?2")
    Float queryMemberDiscount(String startTime, String endTime);

    //获取使用积分抵扣的金额
    @Query("SELECT SUM(discountMoney) FROM BuffetOrder WHERE type='11' AND STATUS IN ('2', '3', '4', '5') AND createTime BETWEEN ?1 AND ?2")
    Float queryScoreDiscount(String startTime, String endTime);

    //获取会员订单中所补金额
    @Query("SELECT SUM(totalMoney) FROM BuffetOrder WHERE type='5' AND payType=?3 AND STATUS IN ('2', '3', '4', '5') AND createTime BETWEEN ?1 AND ?2")
    Float queryMemberRepair(String startTime, String endTime, String payType);

    //获取不同支付方式的押金金额
    @Query("SELECT SUM(surplusBond+backBond) FROM BuffetOrder WHERE bondPayType=?3 AND STATUS IN ('2', '3', '4', '5') AND createTime BETWEEN ?1 AND ?2")
    Float queryBondByType(String startTime, String endTime, String bondPayType);

    //获取已退还的押金，都用现金退
    @Query("SELECT SUM(backBond) FROM BuffetOrder WHERE STATUS IN ('2', '3', '4', '5') AND createTime BETWEEN ?1 AND ?2")
    Float queryReturnedBond(String startTime, String endTime);

    @Query("SELECT SUM(surplusBond) FROM BuffetOrder WHERE status=?3 AND createTime BETWEEN ?1 AND ?2")
    Float queryBondByStatus(String startTime, String endTime, String status);

    @Query("SELECT SUM(commodityCount) FROM BuffetOrder WHERE createDay LIKE ?1 AND status NOT IN ('-1', '-2')")
    Double sumByMonth(String month);

    @Query("SELECT SUM(commodityCount) FROM BuffetOrder WHERE createDay = ?1 AND type=?2 AND status NOT IN ('-1', '-2')")
    Integer sumByDay(String day, String type);

    @Query("FROM BuffetOrder WHERE type='3' AND STATUS IN ('2', '3', '4', '5') AND createDay=?1")
    List<BuffetOrder> findMeiTuanByDay(String day);

    @Query("SELECT SUM(commodityCount) FROM BuffetOrder WHERE createDay = ?1 AND status NOT IN ('-1', '-2')")
    Integer sumByDay(String day);

    @Query("SELECT SUM(discountMoney) FROM BuffetOrder WHERE discountType=?1 AND createDay LIKE ?2 AND status NOT IN ('-1', '-2')")
    Double sumDiscountMoney(String discountType, String month);

    @Query("UPDATE BuffetOrder SET hasTakeOff=?1 WHERE no=?2")
    @Modifying
    @Transactional
    void updateHasTakeOff(String flag, String orderNo);

    /** 获取会员订单且未扣款的订单 */
    @Query("FROM BuffetOrder WHERE hasTakeOff='0' AND type='5'")
    List<BuffetOrder> findNoTakeOff();
}
