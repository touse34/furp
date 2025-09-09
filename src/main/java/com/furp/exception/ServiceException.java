package com.furp.exception;

import lombok.Getter;

public class ServiceException extends RuntimeException {

  /**
   * 错误码
   */
  @Getter
  private Integer code;

  /**
   * 错误提示
   */
  private String message;



  public ServiceException(String message)
  {
    this.message = message;
  }

  public ServiceException(String message, Integer code)
  {
    this.message = message;
    this.code = code;
  }

  public ServiceException setMessage(String message)
  {
    this.message = message;
    return this;
  }

}
