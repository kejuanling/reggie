package com.itheima.reggie.common;

/**
 * 自定义业务异常
 * 用于处理可预知的业务逻辑异常（如：状态不允许删除、数据不存在等）
 */
public class CustomException extends RuntimeException {

    // 错误码（可选，便于前端对不同错误做不同处理）
    private Integer code;

    public CustomException(String message) {
        super(message);
    }

    // 带错误码的构造方法
    public CustomException(String message, Integer code) {
        super(message);
        this.code = code;
    }

    public Integer getCode() {
        return code;
    }
}