package com.auto.u2a.polit.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 统一API响应格式
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    
    private int code;
    private String message;
    private T data;
    private LocalDateTime timestamp;
    

    
    // 手动添加getCode()和getMessage()方法
    public int getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
    
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<T>(200, "操作成功", data, LocalDateTime.now());
    }
    
    public static <T> ApiResponse<T> success(String message) {
        return new ApiResponse<T>(200, message, null, LocalDateTime.now());
    }
    
    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<T>(200, message, data, LocalDateTime.now());
    }
    
    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<T>(code, message, null, LocalDateTime.now());
    }
    
    public static <T> ApiResponse<T> error(int code, String message, T data) {
        return new ApiResponse<T>(code, message, data, LocalDateTime.now());
    }
    
    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<T>(500, message, null, LocalDateTime.now());
    }
}