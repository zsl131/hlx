package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.dto.IncomeDto;
import com.zslin.web.model.Income;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/8/30 8:55.
 */
public interface IIncomeService extends BaseRepository<Income, Integer>, JpaSpecificationExecutor<Income> {

    @Query("SELECT AVG(totalMoney) FROM Income WHERE storeSn=?1 AND comeMonth=?2 AND type='1'")
    Double average(String storeSn, String comeMonth);

    //营业额大于某值的天数
    @Query("SELECT COUNT(id) FROM Income WHERE storeSn=?1 AND comeMonth=?2 AND totalMoney>=?3 AND type='1'")
    Integer moreThan(String storeSn, String comeMonth, Double value);

    @Query("SELECT ROUND(SUM(totalMoney), 2) FROM Income WHERE storeSn=?1 AND comeMonth=?2")
    Double totalMoney(String storeSn, String comeMonth);

    @Query("SELECT new com.zslin.web.dto.IncomeDto(ROUND(SUM(i.totalMoney), 2), COUNT(i.id)) FROM Income i WHERE storeSn=?1 AND comeMonth=?2")
    IncomeDto queryByMonth(String storeSn, String comeMonth);

    @Query("SELECT SUM(peopleCount) FROM Income WHERE storeSn=?1 AND comeMonth=?2")
    Integer totalPeopleCount(String storeSn, String comeMonth);

    @Query("SELECT SUM(deskCount) FROM Income WHERE storeSn=?1 AND comeMonth=?2")
    Integer totalDeskCount(String storeSn, String comeMonth);

    @Query("FROM Income WHERE storeSn=?1 AND comeDay=?2 AND type=?3")
    Income findByComeDay(String storeSn, String comeDay, String type);

    @Query("FROM Income WHERE storeSn=?1 AND comeDay=?2 ")
    List<Income> findByComeDay(String storeSn, String comeDay);

    @Query("FROM Income i WHERE i.storeSn=?1 AND i.comeMonth=?2")
    List<Income> findByMonth(String storeSn, String comeMonth);

    @Query("UPDATE Income i SET i.ticketPath=?1 WHERE i.id=?2")
    @Modifying
    @Transactional
    void updateTicketPath(String path, Integer id);
}
