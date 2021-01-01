package com.sutpc.transpaas.algoserver.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ModelClassificationProjectDto {

  @ApiModelProperty(value = "人群判断(1-一般居民（POP_TYPE=Wuhan），" +
      "2-特殊人群（POP_TYPE=Farmer, CollStu, MidStu, Pupil, Retired）)",
      example = "1")
  private String popJudge;

  @ApiModelProperty(value = "人群类型", example = "Wuhan")
  private String popType;

  @ApiModelProperty(value = "收入等级", example = "HI")
  private String incomeType;

  @ApiModelProperty(value = "拥车情况", example = "CA")
  private String carType;

  @ApiModelProperty(value = "出行目的", example = "HWH")
  private String purposeType;

  @ApiModelProperty(value = "数值(1-选中，0-未选中)", example = "1")
  private String value;

  @ApiModelProperty(value = "人群、目的交叉分类", example = "Wuhan_HI_CA_HWH")
  private String crossType1;

  @ApiModelProperty(value = "人群交叉分类", example = "Wuhan_HI_CA")
  private String crossType2;
}
