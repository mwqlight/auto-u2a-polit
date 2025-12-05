package com.auto.u2a.polit.exception;

import lombok.Getter;

/**
 * 业务异常类
 */
@Getter
public class BusinessException extends RuntimeException {
    
    private final int code;
    
    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
    
    public BusinessException(String message) {
        super(message);
        this.code = 10000; // 默认业务错误码
    }
    
    public BusinessException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
}