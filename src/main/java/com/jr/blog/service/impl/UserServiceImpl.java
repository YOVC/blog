package com.jr.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.jr.blog.commons.JWTUtils;
import com.jr.blog.commons.dto.SafeUser;
import com.jr.blog.entity.User;
import com.jr.blog.exception.BusinessException;
import com.jr.blog.mapper.IUserMapper;
import com.jr.blog.service.IUserService;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;


import static com.jr.blog.commons.ErrorCode.NULL_DATA;
import static com.jr.blog.commons.ErrorCode.PARAMS_ERROR;

@Service
public class UserServiceImpl implements IUserService {
    @Resource
    private IUserMapper userMapper;

    /**
     * 盐值 混淆密码
     */
    private static final String SALT = "www.yyds.com";

    @Override
    public String login(String userName, String password) {
        //1.验证参数格式是否正确
        if (StrUtil.hasBlank(userName,password)){
            throw new BusinessException(PARAMS_ERROR,"账户或密码为空");
        }
        if (StrUtil.hasBlank(userName,password)){
            throw new BusinessException(PARAMS_ERROR,"账户或密码为空");
        }
        //TODO 正则表达式判断用户账户
        if (userName.length() < 8){
            throw new BusinessException(PARAMS_ERROR,"账号长度过短");
        }
        //TODO 正则表达式判断用户密码
        if(password.length() < 8){
            throw new BusinessException(PARAMS_ERROR,"密码长度过短");
        }

        //2.将用户输入密码进行加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes(StandardCharsets.UTF_8));
        //3.从数据库中查询用户
        User user = userMapper.search(userName,encryptPassword);
        if(user == null){
            throw new BusinessException(NULL_DATA,"用户不存在，账号或密码错误");
        }

        //4.生成token
        //4.1用户信息脱敏
        SafeUser safeUser = BeanUtil.copyProperties(user, SafeUser.class);
        //4.2 用户对象转map
        Map<String,String> userMap = new HashMap<>(8);
        userMap.put("userId",safeUser.getUserId().toString());
        userMap.put("userName", safeUser.getUserName());
        userMap.put("nickName", safeUser.getNickName());
        userMap.put("icon",safeUser.getIcon());
        userMap.put("signature", safeUser.getSignature());
        userMap.put("status", safeUser.getStatus().toString());
        userMap.put("isDelete",safeUser.getIsDelete().toString());
        userMap.put("role",safeUser.getRole().toString());

        return JWTUtils.getToken(userMap);
    }

    @Override
    public void register(String userName, String password, String checkPassword) {
        //1.验证参数格式是否正确
        if (StrUtil.hasBlank(userName,password)){
            throw new BusinessException(PARAMS_ERROR,"账户或密码为空");
        }
        if (StrUtil.hasBlank(userName,password)){
            throw new BusinessException(PARAMS_ERROR,"账户或密码为空");
        }
        //TODO 正则表达式判断用户账户
        if (userName.length() < 8){
            throw new BusinessException(PARAMS_ERROR,"账号长度过短");
        }
        //TODO 正则表达式判断用户密码
        if(password.length() < 8){
            throw new BusinessException(PARAMS_ERROR,"密码长度过短");
        }

        if(!password.equals(checkPassword)){
           throw new BusinessException(PARAMS_ERROR,"密码与校验密码不一致");
        }

        //2.根据账号查询用户，禁止账号重复
        User user = userMapper.searchByUserName(userName);
        if(user != null){
            throw new BusinessException(40002,"账号已存在","");
        }

        //3.密码加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes(StandardCharsets.UTF_8));

        //4.保存用户信息
        User initUser = new User();
        initUser.setUserName(userName);
        initUser.setPassword(encryptPassword);
        int result = userMapper.saveUser(initUser);
        if (result == 0) {
            throw new BusinessException(40003, "用户注册失败", "");
        }
    }


}
