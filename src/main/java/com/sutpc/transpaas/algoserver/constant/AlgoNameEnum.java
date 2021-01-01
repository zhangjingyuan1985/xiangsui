package com.sutpc.transpaas.algoserver.constant;

import lombok.Getter;

/**
 * 算法名称（执行模块）.
 */
@Getter
public enum AlgoNameEnum {

  INIT("INIT", "INIT",
      "8080", AlgoServerType.BZ_PC, "工程初始化"),

  TRIP_GENERATION("TRIP_GENERATION", "tour_generate_main",
      "8000", AlgoServerType.BZ_PC, "出行生成"),

  UTILITY_CALCULATION("UTILITY_CALCULATION", "utility_calculation_main",
      "8010", AlgoServerType.BZ_PC, "效用计算"),

  DESTINATION_SELECTION("DESTINATION_SELECTION", "destination_choice_main",
      "8020", AlgoServerType.BZ_PC, "目的地选择"),

  MODE_SELECTION("MODE_SELECTION", "mode_choice_main",
      "8030", AlgoServerType.BZ_PC, "方式选择"),

  TIME_DIVISION("TIME_DIVISION", "time_split_main",
      "8040", AlgoServerType.BZ_PC, "时间划分"),

  CAR_DIVISION("CAR_DIVISION", "auto_assignment_main",
      "8050", AlgoServerType.JMG_PC, "小汽车分配"),

  TRAFFIC_ASSIGNMENT("TRAFFIC_ASSIGNMENT", "transit_assignment_main",
      "8060", AlgoServerType.JMG_PC, "交通分配");


  private String code;

  private String apiCode;

  private String port;

  private AlgoServerType serverType;

  private String desc;

  AlgoNameEnum(String code, String apiCode, String port, AlgoServerType serverType, String desc) {
    this.code = code;
    this.apiCode = apiCode;
    this.port = port;
    this.serverType = serverType;
    this.desc = desc;
  }

  /**
   * getByCode.
   *
   * @param code 代码
   * @return AlgoNameEnum
   */
  public static AlgoNameEnum getByCode(String code) {
    for (AlgoNameEnum algoNameEnum : values()) {
      if (algoNameEnum.getCode() == code) {
        return algoNameEnum;
      }
    }
    return null;
  }
}
