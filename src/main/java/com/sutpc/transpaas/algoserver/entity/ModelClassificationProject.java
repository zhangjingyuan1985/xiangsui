package com.sutpc.transpaas.algoserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 出行分类基础表-用户模型
 */
@TableName("t_model_classification_project")
@Data
public class ModelClassificationProject extends ModelClassification {

  /**
   * 模型ID:FK
   */
  private String modelProjectId;

  /**
   * 数值(1-选中，0-未选中)
   */
  private String value;

  /**
   * 人群、目的交叉分类
   */
  private String crossType1;

  /**
   * 人群交叉分类
   */
  private String crossType2;
}
