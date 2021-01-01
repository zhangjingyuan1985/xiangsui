package com.sutpc.transpaas.algoserver.utils;


import com.sutpc.transpaas.algoserver.constant.ReturnCodeEnum;
import java.util.Objects;
import lombok.Data;

/**
 *.
 *
 * @author yangx .
 * @param <T>
 */
@Data
public class ResponseResult<T> {

  private String code = "500";
  private String msg = "";
  private String msgKey;
  private T data;

  /**
   * 当前时间戳.
   */
  private long time = System.currentTimeMillis();

  public ResponseResult() {
    this.code = ReturnCodeEnum.SUCCESS.getCode();
    this.msg = "success";
  }

  /**
   * .
   *
   * @return
   */
  public static ResponseResult ok() {
    ResponseResult responseResult = new ResponseResult();
    responseResult.setCode(ReturnCodeEnum.SUCCESS.getCode());
    return responseResult;
  }

  /**
   * .
   *
   * @param data
   * @return
   */
  public static ResponseResult ok(Object data) {
    ResponseResult r = new ResponseResult();
    r.setData(data);
    return r;
  }

  /**
   * .
   *
   * @param data
   * @param msg
   * @return
   */
  public static ResponseResult ok(Object data, String msg) {
    ResponseResult r = new ResponseResult();
    r.setData(data);
    r.setMsg(msg);
    return r;
  }

  public ResponseResult<T> success() {
    this.setCode(ReturnCodeEnum.SUCCESS.getCode());
    return this;
  }

  /**
   * 返回成功默认方法
   * @param data
   * @return
   */
  public ResponseResult<T> success(T data) {
    this.setCode(ReturnCodeEnum.SUCCESS.getCode());
    this.setData(data);
    return this;
  }

  /**
   * 返回成功的Boolean标志
   * @return
   */
  public boolean isSuccess() {
    return Objects.equals(this.code, ReturnCodeEnum.SUCCESS.getCode());
  }

  /**
   * .
   *
   * @param msg
   * @return
   */
  public static ResponseResult error(String msg) {
    ResponseResult r = new ResponseResult();
    r.setCode("500");
    r.setMsg(msg);
    return r;
  }

  /**
   * .
   *
   * @param data
   * @param msg
   * @return
   */
  public static ResponseResult error(Object data, String msg) {
    ResponseResult r = new ResponseResult();
    r.setCode("500");
    r.setData(data);
    r.setMsg(msg);
    return r;
  }

  public static ResponseResult error(String code, Object data, String msg) {
    ResponseResult r = new ResponseResult();
    r.setCode(code);
    r.setData(data);
    r.setMsg(msg);
    return r;
  }

  public ResponseResult fail() {
    this.setCode(ReturnCodeEnum.FAILED.getCode());
    this.setMsg(msg);
    return this;
  }


  /**
   * 返回失败默认方法，code=500.
   *
   * @param msg
   * @return
   */
  public ResponseResult<T> fail(String msg) {
    this.setCode(ReturnCodeEnum.FAILED.getCode());
    this.setMsg(msg);
    return this;
  }
}
