package com.jr.blog.commons.interceptors;


import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.jr.blog.commons.result.ErrorCode;
import com.jr.blog.commons.utils.JWTUtils;
import com.jr.blog.commons.utils.UserHolder;
import com.jr.blog.commons.constant.RedisConstants;
import com.jr.blog.commons.dto.SafeUser;
import com.jr.blog.exception.BusinessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登陆拦截器
 */
public class LoginInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (UserHolder.getUser() == null){
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED_ERROR);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }


}
