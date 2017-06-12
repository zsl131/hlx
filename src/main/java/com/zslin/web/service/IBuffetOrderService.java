package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.BuffetOrder;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/4/2 23:27.
 */
public interface IBuffetOrderService extends BaseRepository<BuffetOrder, Integer>, JpaSpecificationExecutor<BuffetOrder> {

    BuffetOrder findByNo(String no);

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

    @Query("SELECT SUM(totalMoney) FROM BuffetOrder WHERE payType=?3 AND STATUS IN ('2', '3', '4', '5') AND createTime BETWEEN ?1 AND ?2")
    Float queryTotalMoneyByPayType(String startTime, String endTime, String payType);

    @Query("SELECT SUM(surplusBond) FROM BuffetOrder WHERE STATUS IN ('2', '3', '4', '5') AND createTime BETWEEN ?1 AND ?2")
    Float queryBondMoney(String startTime, String endTime);

    @Query("FROM BuffetOrder WHERE type='6' AND STATUS IN ('2', '3', '4', '5') AND createTime BETWEEN ?1 AND ?2")
    List<BuffetOrder> findByTicket(String startTime, String endTime);

    @Query("FROM BuffetOrder WHERE type='3' AND STATUS IN ('2', '3', '4', '5') AND createTime BETWEEN ?1 AND ?2")
    List<BuffetOrder> findByMeiTuan(String startTime, String endTime);
}
