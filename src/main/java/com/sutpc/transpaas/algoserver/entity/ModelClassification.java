package com.sutpc.transpaas.algoserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 出行分类基础表
 */
@TableName("t_model_classification")
@Data
public class ModelClassification extends BaseEntity {

  /**
   * 人群判断
   */
  private String popJudge;

  /**
   * 人群类型
   */
  private String popType;

  /**
   * 收入等级
   */
  private String incomeType;

  /**
   * 拥车情况
   */
  private String carType;

  /**
   * 出行目的
   */
  private String purposeType;

}
