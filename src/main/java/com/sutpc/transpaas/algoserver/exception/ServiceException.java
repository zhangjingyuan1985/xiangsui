package com.sutpc.transpaas.algoserver.exception;

public class ServiceException extends RuntimeException {

  protected String code;
  protected String message;

  public ServiceException(String message) {
    super(message);
    this.message = message;
  }

  public ServiceException(String code, String message) {
    super("[" + code + "]" + message);
    this.code = code;
    this.message = message;
  }

  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}