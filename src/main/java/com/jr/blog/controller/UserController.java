package com.jr.blog.controller;

import com.jr.blog.commons.BaseResponse;
import com.jr.blog.commons.ResultUtils;
import com.jr.blog.commons.dto.LoginFormDTO;
import com.jr.blog.service.IUserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;

    public BaseResponse Login(@RequestBody LoginFormDTO loginFormDTO){
        return ResultUtils.success(null);
    }


}
