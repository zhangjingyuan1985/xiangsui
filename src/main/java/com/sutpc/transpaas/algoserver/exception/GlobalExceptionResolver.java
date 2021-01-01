package com.sutpc.transpaas.algoserver.exception;

import com.sutpc.transpaas.algoserver.utils.ResponseResult;
import java.util.List;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * 异常捕获统一处理.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionResolver {

  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public ResponseResult<String> jsonErrorHandler(HttpServletRequest request, Exception ex)
      throws Exception {
    ResponseResult<String> result = new ResponseResult<>();
    result.setCode("500");

    String errorMessage = "";
    String errorKey = "";
    if (ex instanceof HttpRequestMethodNotSupportedException) {
      log.error(request.getServletPath());
      errorMessage = request.getMethod() + "方法不支持";
      errorKey = "Method Not Supported";
    } else if (ex instanceof MethodArgumentTypeMismatchException) {
      log.error(ex.getMessage(), ex);
      errorMessage = "参数转换异常";
      errorKey = "Type Mismatch";
    } else if (ex instanceof BusinessException) {
      errorMessage = ex.getMessage();
      errorKey = ((BusinessException) ex).getCode();
    } else if (ex instanceof ServiceException) {
      errorMessage = ex.getMessage();
      errorKey = ((ServiceException) ex).getCode();
    } else {
      log.error("未能捕获的异常类型：", ex);
      errorMessage = getMessage(request, ex.getMessage(), null);
      errorKey = "-1";
    }
    result.setMsg(errorMessage);
    result.setMsgKey(errorKey);
    return result;
  }

  public String getMessage(HttpServletRequest request, String key, List<Object> params) {
    WebApplicationContext ac = RequestContextUtils.findWebApplicationContext(request);
    Object[] args = {};
    if (params != null) {
      args = params.toArray();
    }
    String msg = null;
    try {
      msg = ac.getMessage(key, args, Locale.CHINA);
    } catch (Exception e) {
      msg = key;
    }
    return msg;
  }
}
