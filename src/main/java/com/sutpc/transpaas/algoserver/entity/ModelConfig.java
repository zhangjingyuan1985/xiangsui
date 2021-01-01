package com.sutpc.transpaas.algoserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 模型项目配置信息
 */
@Data
@TableName("t_model_config")
public class ModelConfig extends BaseEntity {

  private String configKey;

  private String configValue;

  private String configGroup;

  private String configDesc;

  private int groupOrder;

  private int tableOrder;

}
