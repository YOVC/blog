package com.jr.blog.commons;

/**
 * 错误码
 */
public enum ErrorCode {
    SUCCESS(20000,"success", ""),
    NOT_FOUND(40400,"未找到该资源", ""),
    INTERNAL_SERVER_ERROR(50000, "服务器内部错误!", ""),
    SERVER_BUSY(50300,"服务器正忙，请稍后再试!", ""),
    SYSTEM_ERROR(50000,"系统内部异常",""),
    SIGNATURE_ERROR(40101,"token签名不一致",""),
    TOKEN_EXPIRED_ERROR(40102,"token令牌过期",""),
    ALGORITHM_MISMATCH_ERROR(40103,"token算法不匹配",""),
    INVALID_CLAIM(40104,"token的payload失效",""),
    PARAMS_ERROR(40000,"请求参数错误","")
    ;

    private final int code;

    private final String message;

    private final String description;

    ErrorCode(int code, String message, String description) {
        this.code = code;
        this.message = message;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getDescription() {
        return description;
    }
}
