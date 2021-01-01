package com.sutpc.transpaas.algoserver.constant;

/**
 * 算法执行状态(初始化：0，运行中：1，运行成功：8，运行失败：9).
 */
public enum AlgoRunStatus {

  INIT(0, "初始化"),

  RUNING(1, "执行中"),

  HOLD(2, "无算法执行资源"),

  SUCCESS(8, "运行成功结束"),

  FAIL(9, "运行失败");

  private int code;

  private String desc;

  AlgoRunStatus(int code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  /**
   * getByCode.
   *
   * @param code 代码
   * @return AlgoRunStatus
   */
  public static AlgoRunStatus getByCode(int code) {
    for (AlgoRunStatus algoRunStatus : values()) {
      if (algoRunStatus.getCode() == code) {
        return algoRunStatus;
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
