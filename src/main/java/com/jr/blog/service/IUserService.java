package com.jr.blog.service;

public interface IUserService {
    /**
     * 用户登录
     * @param username  账号
     * @param password  密码
     * @return tken
     */
    String login(String username, String password);
}
