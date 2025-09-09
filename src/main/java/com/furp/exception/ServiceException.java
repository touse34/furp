package com.furp.exception;

import lombok.Getter;

public class ServiceException extends RuntimeException {

  /**
   * 错误码
   */
  @Getter
  private Integer code;


  public ServiceException(String message) {
    super(message);
    this.code = 500; // 提供一个默认的错误码
  }

  public ServiceException(String message, Integer code) {
    super(message);
    this.code = code;
  }
}
