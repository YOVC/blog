package com.jr.blog.controller;

import cn.hutool.core.util.StrUtil;
import com.jr.blog.commons.BaseResponse;
import com.jr.blog.commons.ResultUtils;
import com.jr.blog.commons.dto.LoginFormDTO;
import com.jr.blog.exception.BusinessException;
import com.jr.blog.service.IUserService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static com.jr.blog.commons.ErrorCode.PARAMS_ERROR;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;

    public BaseResponse<String> Login(@RequestBody LoginFormDTO loginFormDTO){
        //1.验证参数格式是否正确
        String username = loginFormDTO.getUserName();
        String password = loginFormDTO.getPassword();
        if (StrUtil.hasBlank(username,password)){
            throw new BusinessException(PARAMS_ERROR,"账户或密码为空");
        }
        //TODO 正则表达式判断用户账户
        if (username.length() < 8){
            throw new BusinessException(PARAMS_ERROR,"账号长度过短");
        }
        //TODO 正则表达式判断用户密码
        if(password.length() < 8){
            throw new BusinessException(PARAMS_ERROR,"密码长度过短");
        }

        String token = userService.login(username,password);
        return ResultUtils.success(token);
    }


}
