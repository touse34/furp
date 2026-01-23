package com.furp.exception;

import cn.dev33.satoken.exception.NotLoginException;
import com.furp.entity.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public Result handleServiceException(ServiceException e, HttpServletRequest request)
    {
        log.error(e.getMessage(), e);
        Integer code = e.getCode();
        return code!=null ? Result.error(code, e.getMessage()) : Result.error(e.getMessage());
    }


    @ExceptionHandler(NotLoginException.class)
    public Result handleNotLoginException(NotLoginException e) {
        log.error(e.getMessage(), e);
        return Result.error(401,"未登录");
    }


    /**
     * 捕獲所有未被處理的未知異常
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception e, HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        // 記錄嚴重錯誤日誌
        log.error("請求地址 '{}', 發生未知系統異常.", requestURI, e);
        // 向前端返回一個統一的、模糊的錯誤訊息，避免暴露內部細節
        return Result.error(500, "系統內部異常，請聯繫管理員");
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public Result handleAccountNotFoundException(AccountNotFoundException e) {
        // e.getMessage() 即Service 里抛出时写的 "用户名不存在"
        return Result.error(e.getMessage());
    }

}
