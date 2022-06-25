package com.jr.blog.commons.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class LoginFormDTO implements Serializable {
    private String userName;
    private String password;
}
