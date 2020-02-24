package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Income;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 钟述林 393156105@qq.com on 2017/8/30 8:55.
 */
public interface IIncomeService extends BaseRepository<Income, Integer>, JpaSpecificationExecutor<Income> {

    @Query("SELECT AVG(totalMoney) FROM Income WHERE comeMonth=?1 AND type='1'")
    Double average(String comeMonth);

    //营业额大于某值的天数
    @Query("SELECT COUNT(id) FROM Income WHERE comeMonth=?1 AND totalMoney>=?2 AND type='1'")
    Integer moreThan(String comeMonth, Double value);

    @Query("SELECT SUM(totalMoney) FROM Income WHERE comeMonth=?1")
    Double totalMoney(String comeMonth);

    @Query("SELECT SUM(peopleCount) FROM Income WHERE comeMonth=?1")
    Integer totalPeopleCount(String comeMonth);

    Income findByComeDay(String comeDay);
}
