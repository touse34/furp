package com.furp.entity; // 或 com.furp.common

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

/**
 * 后端统一返回结果 (优化版)
 * 这个类是不可变的，并且提供了更灵活的工厂方法。
 */
@Getter // 只生成 getter，不生成 setter，保证不可变性
@JsonInclude(JsonInclude.Include.NON_NULL) // JSON序列化时，忽略所有为 null 的字段
public class Result<T> implements Serializable {

    private final Integer code;
    private final String msg;
    private final T data;
    private final String timestamp;

    // 构造函数设为私有，强制使用静态工厂方法创建实例
    private Result(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
        // 【优化】使用 Instant.now().toString() 生成标准的UTC时间 (ISO 8601格式)
        this.timestamp = Instant.now().toString();
    }

    // --- 成功时的工厂方法 ---

    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }

    // 【新增】您需要的方法
    public static <T> Result<T> success(String msg) {
        return new Result<>(200, msg, null);
    }

    public static <T> Result<T> success(String msg, T data) {
        return new Result<>(200, msg, data);
    }

    // --- 失败时的工厂方法 ---

    public static <T> Result<T> error(String msg) {
        // 【优化】对于简单的错误消息，使用 400 (Bad Request) 作为默认错误码可能更通用
        return new Result<>(400, msg, null);
    }

    public static <T> Result<T> error(Integer code, String msg) {
        return new Result<>(code, msg, null);
    }
}