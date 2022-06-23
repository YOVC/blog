package com.jr.blog.commons;

import com.jr.blog.commons.dto.SafeUser;

/**
 * 保存用户信息，减少数据库查询
 * @author JR
 */
public class UserHolder {

    private static final ThreadLocal<SafeUser> threadLocal = new ThreadLocal<>();

    /**
     * 保存用户
     * @param safeUser 脱敏用户
     */
    public static void saveUser(SafeUser safeUser){
        threadLocal.set(safeUser);
    }

    /**
     * 获得用户
     * @return 脱敏用户
     */
    public static SafeUser getUser(){
        return threadLocal.get();
    }

    /**
     * 移除用户
     */
    public static void removeUser(){
        threadLocal.remove();
    }

}
