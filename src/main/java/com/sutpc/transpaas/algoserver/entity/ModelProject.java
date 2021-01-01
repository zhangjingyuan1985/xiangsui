package com.sutpc.transpaas.algoserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 模型表
 */
@TableName("t_model_project")
@Data
public class ModelProject extends BaseEntity {

  /**
   * 模型名称
   */
  private String modelName;

  /**
   * 模型全局参数
   */
  private String modelParameters;

  /** 交互全局参数 */
  private String interactParameters;

  /**
   * 创建人
   */
  private String createUser;

  /**
   * 模型算法执行服务器IP
   */
  private String modelServerIp;

}
