package com.zslin.basic.service;

import com.zslin.basic.model.UserRole;
import com.zslin.basic.repository.BaseRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by zsl-pc on 2016/9/1.
 */
public interface IUserRoleService extends BaseRepository<UserRole, Integer> {

    @Query("SELECT ur.rid FROM UserRole ur WHERE ur.uid=:userId")
    List<Integer> queryRoleIds(@Param("userId") Integer userId);

    UserRole findByUidAndRid(Integer uid, Integer rid);
}
