package com.furp.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 后端统一返回结果
 */
@Data
public class Result<T> implements Serializable{

    private Integer code; //编码：200成功，其他为失败
    private String msg; //响应信息
    private T data; //数据
    private String timestamp; //时间戳

    public static <T> Result<T> success() {
        Result<T> result = new Result<>();
        result.code = 200;
        result.msg = "success";
        result.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        return result;
    }

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<>();
        result.data = object;
        result.code = 200;
        result.msg = "success";
        result.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = 500;
        result.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        return result;
    }

    public static <T> Result<T> error(Integer code, String msg) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = code;
        result.timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME);
        return result;
    }

}