package com.jr.blog.commons.dto;

import lombok.Data;
import java.io.Serializable;

@Data
public class RegisterFormDTO implements Serializable {
    /**
     * 账号
     */
    private String userName;

    /**
     * 密码
     */
    private String password;

    /**
     * 校验密码
     */
    private String checkPassword;
}
