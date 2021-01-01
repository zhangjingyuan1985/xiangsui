package com.sutpc.transpaas.algoserver.constant;

/**
 * 算法服务器类型.
 */
public enum AlgoServerType {

  BZ_PC("BZ_PC", "普通服务器"),

  JMG_PC("JMG_PC", "加密狗算法服务器");

  private String code;

  private String desc;

  AlgoServerType(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public static AlgoServerType getByCode(String code) {
    for (AlgoServerType algoServerType : values()) {
      if (algoServerType.getCode() == code) {
        return algoServerType;
      }
    }
    return null;
  }

  public String getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

}
