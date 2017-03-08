package com.zslin.web.service;

import com.zslin.basic.repository.BaseRepository;
import com.zslin.web.model.Price;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.Query;

/**
 * Created by 钟述林 393156105@qq.com on 2017/3/7 9:45.
 */
public interface IPriceService extends BaseRepository<Price, Integer> {

    @Query("SELECT breakfastPrice FROM Price")
    Float findBreakfastPrice();

    @Query("SELECT dinnerPrice FROM Price")
    Float findDinnerPrice();

    @Query("FROM Price ")
    @Cacheable(cacheNames = "price")
    Price loadOne();

    @Override
    @CacheEvict(cacheNames = "price", allEntries = true)
    <S extends Price> S save(S s);
}
