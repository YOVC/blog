package com.jr.blog.commons;

import lombok.Data;

/**
 * 通用返回类
 * @param <T> 返回数据的类型
 * @author JR
 */
@Data
public class BaseResponse<T> {
    /**
     * 状态码
     */
    private int code;

    /**
     * 返回数据
     */
    private T data;

    /**
     * 返回信息
     */
    private String message;

    /**
     * 详细描述
     */
    private String description;

    public BaseResponse(int code, T data, String message, String description) {
        this.code = code;
        this.data = data;
        this.message = message;
        this.description = description;
    }

    public BaseResponse(int code, T data) {
        this(code,data,"","");
    }

    public BaseResponse(int code, String message, String description) {
        this(code,null,message,description);
    }

    public BaseResponse(int code, T data, String description) {
        this(code,data,"",description);
    }

    public BaseResponse(ErrorCode errorCode){
        this(errorCode.getCode(),null,errorCode.getMessage(),errorCode.getDescription());
    }

}
