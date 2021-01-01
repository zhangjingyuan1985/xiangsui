package com.sutpc.transpaas.algoserver.utils.valid;

import com.sutpc.transpaas.algoserver.constant.ReturnCodeEnum;
import com.sutpc.transpaas.algoserver.exception.BusinessException;
import com.sutpc.transpaas.algoserver.utils.ResponseResult;
import java.util.Map;
import org.springframework.util.StringUtils;

/**
 * 参数校验
 */
public abstract class ValidUtil {

  /**
   * 进行参数校验<br/> 1.如果有参数校验错误，则返回包装后的Response 2. 如果没有错误，则返回null
   *
   * @param t
   * @param <T>
   * @return
   */
  public static <T> ResponseResult valid(T t) {
    ValidationResult result = ValidationUtils.validateEntity(t);
    return wrapper(result);
  }

  public static <T> ResponseResult valid(T t, Class... groupClass) {
    ValidationResult result = ValidationUtils.validateEntity(t, groupClass);
    return wrapper(result);
  }

  /**
   * 校验请求参数 非法的请求参数，直接抛出BusinessException
   *
   * @param t
   * @param groupClass
   * @param <T>
   */
  public static <T> void checkRequestParams(T t, Class... groupClass) {
    ValidationResult validationResult = ValidationUtils.validateEntity(t, groupClass);
    if (validationResult.isHasErrors()) {
      StringBuilder sb = new StringBuilder();
      sb.append("[");
      for (Map.Entry<String, String> entry : validationResult.getErrorMsg().entrySet()) {
        sb.append(entry.getValue()).append(";");
      }
      sb.deleteCharAt(sb.lastIndexOf(";")).append("]");
      throw new BusinessException(ReturnCodeEnum.INVALID_PARAM_ERROR.getCode(), sb.toString());
    }
  }

  /**
   * 包装校验错误成Response<br/> 如果没有错误，返回null
   *
   * @param result
   * @return
   */
  public static ResponseResult wrapper(ValidationResult result) {
    if (result == null || !result.isHasErrors() || result.getErrorMsg() == null
        || result.getErrorMsg().size() == 0) {
      return null;
    }
    ResponseResult response = new ResponseResult().fail();
    StringBuilder sb = new StringBuilder();
    sb.append(response.getMsg());
    sb.append("[");
    for (Map.Entry<String, String> entry : result.getErrorMsg().entrySet()) {
      sb.append(entry.getKey());
      sb.append(",");
      sb.append(entry.getValue());
      sb.append(";");
    }
    sb.deleteCharAt(sb.lastIndexOf(";")).append("]");
    response.setMsg(sb.toString());
    return response;
  }

  /**
   * 校验不能为空<br/> 当为空时，抛出IllegalArgumentException
   *
   * @param value
   * @param message
   */
  public static void notEmpty(String value, String message) {
    if (StringUtils.isEmpty(value)) {
      throw new IllegalArgumentException(message);
    }
  }

  public static void notNumber(Object value, String field) {
    if (value == null) {
      throw new IllegalArgumentException(field + "不能为空，必须为数字");
    }
    if (value instanceof String) {
      try {
        Double.parseDouble(value + "");
        return;
      } catch (Exception e) {
        throw new IllegalArgumentException(field + "必须为数字");
      }
    }
    if (!(value instanceof Number)) {
      throw new IllegalArgumentException(field + "必须为数字");
    }
  }
}
