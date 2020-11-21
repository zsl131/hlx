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
    @Query("SELECT COUNT(h.id) FROM HlxTicket h")
    Integer queryAll();

    /** 总数量 */
    @Query("SELECT COUNT(h.id) FROM HlxTicket h WHERE h.status=?1 ")
    Integer queryAll(String status);

    /** 总数量 */
    @Query("SELECT COUNT(h.id) FROM HlxTicket h WHERE h.createDay=?1 ")
    Integer queryByDay(String createDay);

    /** 总数量 */
    @Query("SELECT COUNT(h.id) FROM HlxTicket h WHERE h.createDay=?1 AND h.status=?1 ")
    Integer queryByDay(String createDay, String status);

    /** 核销数量 */
    @Query("SELECT COUNT(h.id) FROM HlxTicket h WHERE h.useDay=?1")
    Integer queryWriteOffCount(String useDay);

    /** 获取卡券张数 */
    @Query("SELECT COUNT(h.id) FROM HlxTicket h WHERE h.openid=?1 AND h.status=?2")
    Integer queryCount(String openid, String status);

    /** 获取类型的卡券 */
    List<HlxTicket> findByOpenidAndStatus(String openid, String status);
}
