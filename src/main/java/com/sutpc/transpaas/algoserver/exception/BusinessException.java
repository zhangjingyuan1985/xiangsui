package com.sutpc.transpaas.algoserver.exception;

import com.sutpc.transpaas.algoserver.utils.ResponseService;
import lombok.extern.slf4j.Slf4j;

/**
 * @author yangx
 */
@Slf4j
public class BusinessException extends ServiceException {

  public BusinessException(String code, String message) {
    super(code, message);
  }

  public BusinessException(String message) {
    super(message);
  }

  public BusinessException(ResponseService responseService) {
    super(responseService.getResponseCode(), responseService.getResponseMessage());
  }

  public BusinessException(ResponseService responseService, Object... args) {
    super(String.format(responseService.getResponseMessage(), args));
  }

}
