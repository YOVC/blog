package com.jr.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.google.gson.Gson;
import com.jr.blog.commons.utils.JWTUtils;
import com.jr.blog.commons.utils.UserHolder;
import com.jr.blog.commons.constant.RedisConstants;
import com.jr.blog.commons.dto.SafeUser;
import com.jr.blog.entity.User;
import com.jr.blog.exception.BusinessException;
import com.jr.blog.mapper.IUserMapper;
import com.jr.blog.service.IUserService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static com.jr.blog.commons.result.ErrorCode.NULL_DATA;
import static com.jr.blog.commons.result.ErrorCode.PARAMS_ERROR;

@Service
public class UserServiceImpl implements IUserService {
    @Resource
    private IUserMapper userMapper;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

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

        //4.将用户信息保存到Redis中
        //4.1用户信息脱敏
        SafeUser safeUser = BeanUtil.copyProperties(user, SafeUser.class);
        //4.2 用户对象转为json
        Gson gson = new Gson();
        String userJson = gson.toJson(safeUser);
        //4.3保存并设置有效期为7天
        String key = RedisConstants.LOGIN_USER_KEY + safeUser.getUserId();
        stringRedisTemplate.opsForValue().set(key,userJson);
        stringRedisTemplate.expire(key, RedisConstants.LONG_USER_TTL, TimeUnit.DAYS);
        return JWTUtils.getToken(safeUser.getUserId());
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

    @Override
    public void updateUserInfo(String nickName, String signature) {
        //1.获取当前登陆用户id
        SafeUser safeUser = UserHolder.getUser();
        Integer userId =safeUser.getUserId();
        //2.修改用户信息
        userMapper.updateUserInfo(userId,nickName,signature);
        //3.修改redis和UserHolder中信息
        if (nickName!=null && !nickName.isEmpty()){
            safeUser.setNickName(nickName);
        }
        if (signature!=null && !signature.isEmpty()){
            safeUser.setSignature(signature);
        }
        UserHolder.saveUser(safeUser);
        String key = RedisConstants.LOGIN_USER_KEY + userId;
        Gson gson = new Gson();
        stringRedisTemplate.opsForValue().set(key,gson.toJson(safeUser));
    }


}
