package com.sutpc.transpaas.algoserver.constant;


import com.sutpc.transpaas.algoserver.utils.ResponseService;

/**
 *
 */
public enum ReturnCodeEnum implements ResponseService {

  SUCCESS("200", "请求成功"),
  FAILED("500", "系统繁忙，请稍后重试"),
  PASSWORD_ERROR("-2", "密码错误"),
  AES_ENCODE_ERROR("-3", "AES加密失败"),
  MD5_ENCODE_ERROR("-4", "MD5加密失败"),
  IDCARD_NULL_FAIL("-2009", "身份证号不合法"),
  INVALID_PARAM_ERROR("-1004", "非法的请求参数"),
  TOKEN_URL_REP("600", "方法重复定义"),
  TOKEN_ERROR("-999", "TOKEN失效");

  private String code;
  private String msg;

  private ReturnCodeEnum(String code, String msg) {
    this.code = code;
    this.msg = msg;
  }

  public String getCode() {
    return this.code;
  }

  public String getMsg() {
    return this.msg;
  }

  @Override
  public String getResponseCode() {
    return this.code;
  }

  @Override
  public String getResponseMessage() {
    return this.msg;
  }

  public boolean equals(String code) {
    return this.code.equals(code);
  }

}
