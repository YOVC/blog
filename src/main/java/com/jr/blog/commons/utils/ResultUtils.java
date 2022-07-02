package com.jr.blog.commons.utils;

import com.jr.blog.commons.result.BaseResponse;
import com.jr.blog.commons.result.ErrorCode;

/**
 *返回工具类
 */
public class ResultUtils {

    /**
     * 成功
     * @param data 返回给前端的数据
     * @param <T>   返回数据的类型
     * @return  BaseResponse
     */
    public static <T> BaseResponse<T> success(T data, String description){
        return new BaseResponse<T>(20000,data,description);
    }

    public static <T> BaseResponse<T> success(String description){
        return new BaseResponse(20000,description);
    }


    /**
     * 失败
     * @param errorCode
     * @return
     */
    public static BaseResponse error(ErrorCode errorCode){
        return new BaseResponse(errorCode);
    }

    public static BaseResponse error(ErrorCode errorCode,String description){
        return new BaseResponse(errorCode,description);
    }


    public static BaseResponse error(int code,String message,String description){
        return new BaseResponse(code,message,description);
    }


}
