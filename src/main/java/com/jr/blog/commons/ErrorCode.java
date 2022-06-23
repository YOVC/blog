package com.jr.blog.commons;

/**
 * 错误码
 */
public enum ErrorCode {
    SUCCESS(20000,"success", ""),
    NOT_FOUND(40400,"为找到该资源", ""),
    INTERNAL_SERVER_ERROR(50000, "服务器内部错误!", ""),
    SERVER_BUSY(50300,"服务器正忙，请稍后再试!", ""),
    SYSTEM_ERROR(50000,"系统内部异常","");

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
