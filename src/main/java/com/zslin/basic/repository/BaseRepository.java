package com.zslin.basic.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 钟述林 393156105@qq.com on 2017/1/6 14:44.
 */
@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {

    List<T> listBySql(String sql, Object... args);

    List<T> listByHql(String hql, Object... args);

    void updateBySql(String sql, Object... args);

    void updateByHql(String hql, Object... args);

    Object queryByHql(String hql, Object... args);
}
