package com.sutpc.transpaas.algoserver.constant;

/**
 * 算法运行状态.
 */
public enum AlgoServerStatus {

  FREE(0, "负载率低"),

  FULL(1, "满负载");

  private int code;

  private String desc;

  AlgoServerStatus(int code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  /**
   * getByCode.
   *
   * @param code 代码
   * @return AlgoServerStatus
   */
  public static AlgoServerStatus getByCode(int code) {
    for (AlgoServerStatus algoServerStatus : values()) {
      if (algoServerStatus.getCode() == code) {
        return algoServerStatus;
      }
    }
    return null;
  }

  public int getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }
}
