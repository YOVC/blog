package com.jr.blog.service;

public interface IUserService {
    /**
     * 用户登录
     * @param username  账号
     * @param password  密码
     * @return token
     */
    String login(String username, String password);

    /**
     * 用户注册
     * @param userName 账号
     * @param password 密码
     * @param checkPassword 校验密码
     */
    void register(String userName, String password, String checkPassword);
}
