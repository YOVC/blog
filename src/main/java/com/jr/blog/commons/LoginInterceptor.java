package com.jr.blog.commons;


import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.jr.blog.commons.dto.SafeUser;
import com.jr.blog.exception.BusinessException;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {
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
        }catch (TokenExpiredException e){
            throw new BusinessException(ErrorCode.TOKEN_EXPIRED_ERROR);
        }catch (AlgorithmMismatchException e){
            throw new BusinessException(ErrorCode.ALGORITHM_MISMATCH_ERROR);
        }catch (InvalidClaimException e){
            throw new BusinessException(ErrorCode.INVALID_CLAIM);
        }
        //3. 将payload转换为SafeUser对象
        Claim cacheUser = decodedJWT.getClaim("SafeUser");
        Gson gson = new Gson();
        SafeUser safeUser = gson.fromJson(cacheUser.asString(), SafeUser.class);
        //4. 将用户信息保存到ThreadLocal中
        UserHolder.saveUser(safeUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }


}
