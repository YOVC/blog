package com.jr.blog.controller;

import cn.hutool.core.util.StrUtil;
import com.jr.blog.commons.result.BaseResponse;
import com.jr.blog.commons.utils.ResultUtils;
import com.jr.blog.commons.utils.UserHolder;
import com.jr.blog.commons.dto.LoginFormDTO;
import com.jr.blog.commons.dto.RegisterFormDTO;
import com.jr.blog.commons.dto.SafeUser;
import com.jr.blog.commons.dto.UserDTO;
import com.jr.blog.exception.BusinessException;
import com.jr.blog.service.IUserService;
import org.apache.ibatis.jdbc.Null;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import static com.jr.blog.commons.result.ErrorCode.PARAMS_ERROR;

@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;

    @PostMapping("/login")
    public BaseResponse<String > Login(@RequestBody LoginFormDTO loginFormDTO){
        //1.验证参数格式是否正确
        String userName = loginFormDTO.getUserName();
        String password = loginFormDTO.getPassword();
        if (StrUtil.hasBlank(userName,password)){
            throw new BusinessException(PARAMS_ERROR,"账户或密码为空");
        }
        String token = userService.login(userName,password);
        return ResultUtils.success(token,"登陆成功");
    }

    @PostMapping("/register")
    public BaseResponse<Null> register(@RequestBody RegisterFormDTO registerFormDTO){
        //1.验证参数格式是否正确
        String userName = registerFormDTO.getUserName();
        String password = registerFormDTO.getPassword();
        String checkPassword = registerFormDTO.getCheckPassword();
        if(StrUtil.hasBlank(userName,password,checkPassword)){
            throw new BusinessException(PARAMS_ERROR,"账号或者密码为空");
        }
        userService.register(userName,password,checkPassword);
        return ResultUtils.success("注册成功");
    }

    /**
     * 获取当前用户登录信息
     * @return 用户信息
     */
    @GetMapping("/me")
    public BaseResponse<SafeUser> me(){
        return ResultUtils.success(UserHolder.getUser(),"获取成功");
    }


    /**
     * 修改个性签名和昵称
     * @param userDTO 修改信息
     * @return 成功与否
     */
    @PostMapping("/updateUserInfo")
    public BaseResponse<SafeUser> updateUserInfo(@RequestBody UserDTO userDTO){
        String nickName = userDTO.getNickName();
        String signature = userDTO.getSignature();
        if(StrUtil.isAllBlank(nickName,signature)){
            throw new BusinessException(PARAMS_ERROR,"修改失败，参数均为空");
        }
        userService.updateUserInfo(nickName,signature);
        return ResultUtils.success("修改成功");
    }





}
