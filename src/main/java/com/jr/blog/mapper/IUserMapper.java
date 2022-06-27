package com.jr.blog.mapper;

import com.jr.blog.entity.User;
import org.apache.ibatis.annotations.Param;

public interface IUserMapper {
    /**
     * 根据用户账号和密码查询用户
     * @param userName 账号
     * @param password 密码
     * @return 用户信息
     */
    User search(@Param("userName") String userName,@Param("password")String password);

    /**
     * 根据账号查询用户
     * @param userName 账号
     * @return 用户信息
     */
    User searchByUserName(@Param("userName") String userName);

    /**
     * 保存用户
     * @param user 用户信息
     * @return 是否成功 1-成功  0-失败
     */
    int saveUser(@Param("user") User user);
}
