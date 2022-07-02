package com.jr.blog.commons.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.Map;

/**
 * JWT工具类
 * @author JR
 */
public class JWTUtils {
    /**
     * 盐值
     */
    private static final String SALT = "blog_cmd_xlj";

    /**
     * 生成token
     * @param userId 用户id
     * @return token
     */
    public static String getToken(Integer userId){
        //1.builder创建
        JWTCreator.Builder builder = JWT.create();
        //2.将用户信息保存到token的payLoad中
        builder.withClaim("id",userId);
        //3.生成签名，返回token
        return builder.sign(Algorithm.HMAC256(SALT));
    }

    /**
     * 验证token
     * @param token 从前端获取的token
     * @return  DecodedJWT
     */
    public static DecodedJWT verifyToken(String token){
       return JWT.require(Algorithm.HMAC256(SALT)).build().verify(token);
    }

}
