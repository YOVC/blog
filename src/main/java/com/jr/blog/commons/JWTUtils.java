package com.jr.blog.commons;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Calendar;
import java.util.GregorianCalendar;
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
     * @param map 需要保存的用户信息
     * @return token
     */
    public static String getToken(Map<String,String> map){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE,7);         //默认令牌过期时间为7天
        JWTCreator.Builder builder = JWT.create();
        map.forEach(builder::withClaim);          //将用户信息保存到token的payLoad中
        builder.withExpiresAt(calendar.getTime());//设置Token有效期
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