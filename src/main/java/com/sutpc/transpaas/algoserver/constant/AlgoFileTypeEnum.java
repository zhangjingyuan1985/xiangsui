package com.sutpc.transpaas.algoserver.constant;

/**
 * 算法文件类型.
 */
public enum AlgoFileTypeEnum {

  JSON("json", "JSON文件"),

  CSV("csv", "CSV文件"),

  OMX("omx", "OMX矩阵文件"),

  TXT("txt", "txt文件");


  private String code;

  private String desc;

  AlgoFileTypeEnum(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  /**
   * getByCode.
   *
   * @param code 代码
   * @return AlgoFileTypeEnum
   */
  public static AlgoFileTypeEnum getByCode(String code) {
    for (AlgoFileTypeEnum algoFileTypeEnum : values()) {
      if (algoFileTypeEnum.getCode() == code) {
        return algoFileTypeEnum;
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
