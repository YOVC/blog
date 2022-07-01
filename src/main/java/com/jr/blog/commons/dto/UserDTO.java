package com.jr.blog.commons.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UserDTO implements Serializable {
    /**
     * 用户昵称
     */
    private String nickName;


    /**
     * 个性签名
     */
    private String signature;

}
