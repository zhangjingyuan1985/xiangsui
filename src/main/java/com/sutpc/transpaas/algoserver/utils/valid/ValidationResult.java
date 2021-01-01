package com.sutpc.transpaas.algoserver.utils.valid;

import com.alibaba.fastjson.JSON;
import java.util.Map;

/**
 * 参数验证结果.
 */
public class ValidationResult {

  /**
   * 是否有错误.
   */
  private boolean hasErrors;

  /**.
   * 错误信息
   */
  private Map<String, String> errorMsg;

  public ValidationResult() {
  }

  public boolean isHasErrors() {
    return this.hasErrors;
  }

  public void setHasErrors(boolean hasErrors) {
    this.hasErrors = hasErrors;
  }

  public Map<String, String> getErrorMsg() {
    return this.errorMsg;
  }

  public void setErrorMsg(Map<String, String> errorMsg) {
    this.errorMsg = errorMsg;
  }

  public String getErrorMessage() {
    return String.valueOf(this.getErrorMsg());
  }

  @Override
  public String toString() {
    return JSON.toJSONString(this);
  }

}
