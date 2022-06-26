package com.jr.blog.commons;


import cn.hutool.core.bean.BeanUtil;
import cn.hutool.jwt.JWT;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.auth0.jwt.exceptions.InvalidClaimException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.jr.blog.commons.dto.SafeUser;
import com.jr.blog.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //1.从请求头中获取token
        String token = request.getHeader("authorization");
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
        Map<String, Claim> claims = decodedJWT.getClaims();
        SafeUser safeUser = payLoadToUser(claims);
        //4. 将用户信息保存到ThreadLocal中
        UserHolder.saveUser(safeUser);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        UserHolder.removeUser();
    }


    private SafeUser payLoadToUser(Map<String,Claim> map){
        try {
            SafeUser safeUser = SafeUser.class.newInstance();
            safeUser.setUserId(map.get("userId").asInt());
            safeUser.setNickName(map.get("userName").toString());
            safeUser.setNickName(map.get("nickName").toString());
            safeUser.setSignature(map.get("signature").toString());
            safeUser.setRole(map.get("role").asInt());
            safeUser.setStatus(map.get("status").asInt());
            safeUser.setIcon(map.get("Icon").toString());
            safeUser.setIsDelete(map.get("isDelete").asInt());
            return safeUser;
        } catch (Exception e) {
          throw new BusinessException(50000,"反射创建对象错误","");
        }

    }
}
