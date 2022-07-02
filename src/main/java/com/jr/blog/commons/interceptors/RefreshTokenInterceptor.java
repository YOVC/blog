package com.jr.blog.commons.interceptors;

import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.jr.blog.commons.constant.RedisConstants;
import com.jr.blog.commons.dto.SafeUser;
import com.jr.blog.commons.result.ErrorCode;
import com.jr.blog.commons.utils.JWTUtils;
import com.jr.blog.commons.utils.UserHolder;
import com.jr.blog.exception.BusinessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 刷新Redis中数据有效期
 */
public class RefreshTokenInterceptor implements HandlerInterceptor {
    private final StringRedisTemplate stringRedisTemplate;

    public RefreshTokenInterceptor(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.从请求头中获取token
        String token = request.getHeader("authorization");
        if(token == null || token.isEmpty()){
            throw new BusinessException(40105,"无token,用户未登陆","");
        }
        //2.验证token
        DecodedJWT decodedJWT;
        try{
            decodedJWT = JWTUtils.verifyToken(token);
        }catch (SignatureVerificationException e){
            throw new BusinessException(ErrorCode.SIGNATURE_ERROR);
        }catch (AlgorithmMismatchException e){
            throw new BusinessException(ErrorCode.ALGORITHM_MISMATCH_ERROR);
        }catch (InvalidClaimException e){
            throw new BusinessException(ErrorCode.INVALID_CLAIM);
        }
        //3.从JWT中获取用户id
        Integer id = decodedJWT.getClaim("id").asInt();
        //4.从redis中获取数据
        String key = RedisConstants.LOGIN_USER_KEY + id;
        String userJson = stringRedisTemplate.opsForValue().get(key);
        if(userJson == null || userJson.isEmpty()){
            //若Redis中没有数据，则token过期
            return true;
        }

        //5.将json数据转成SafeUser对象
        Gson gson = new Gson();
        SafeUser safeUser = gson.fromJson(userJson, SafeUser.class);
        //6. 将用户信息保存到ThreadLocal中
        UserHolder.saveUser(safeUser);
        //7.刷新token有效期
        stringRedisTemplate.expire(key,RedisConstants.LONG_USER_TTL, TimeUnit.DAYS);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }
}
