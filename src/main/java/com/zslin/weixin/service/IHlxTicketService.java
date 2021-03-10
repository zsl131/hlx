package com.zslin.weixin.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.weixin.model.HlxTicket;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IHlxTicketService  extends BaseRepository<HlxTicket, Integer>, JpaSpecificationExecutor<HlxTicket> {

    List<HlxTicket> findByAccountId(Integer accountId);

    List<HlxTicket> findByOpenid(String openid);

    HlxTicket findByTicketNo(String ticketNo);

    /** 总数量 */
    @Query("SELECT COUNT(h.id) FROM HlxTicket h WHERE h.storeSn=?1")
    Integer queryAll(String storeSn);

    /** 总数量 */
    @Query("SELECT COUNT(h.id) FROM HlxTicket h WHERE h.storeSn=?1 AND h.status=?2 ")
    Integer queryAll(String storeSn, String status);

    /** 总数量 */
    @Query("SELECT COUNT(h.id) FROM HlxTicket h WHERE h.storeSn=?1 AND h.createDay=?2 ")
    Integer queryByDay(String storeSn, String createDay);

    /** 总数量 */
    @Query("SELECT COUNT(h.id) FROM HlxTicket h WHERE h.storeSn=?1 AND h.createDay=?2 AND h.status=?3 ")
    Integer queryByDay(String storeSn, String createDay, String status);

    /** 核销数量 */
    @Query("SELECT COUNT(h.id) FROM HlxTicket h WHERE h.storeSn=?1 AND h.useDay=?2")
    Integer queryWriteOffCount(String storeSn, String useDay);

    /** 获取卡券张数 */
    @Query("SELECT COUNT(h.id) FROM HlxTicket h WHERE h.openid=?1 AND h.status=?2")
    Integer queryCount(String openid, String status);

    /** 获取类型的卡券 */
    List<HlxTicket> findByOpenidAndStatus(String openid, String status);
}
