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
    User searchByUserName(@Param("userName") String userName,@Param("password")String password);
}
