package com.sutpc.transpaas.algoserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sutpc.transpaas.algoserver.constant.AlgoNameEnum;
import com.sutpc.transpaas.algoserver.constant.AlgoServerType;
import lombok.Data;

/**
 * 算法表
 */
@TableName("t_model_algo")
@Data
public class ModelAlgo extends BaseEntity {

  /**
   * 模型ID:FK
   */
  private String modelProjectId;

  /**
   * 算法所用服务器ID:FK
   */
  private String modelAlgoServerId;

  /**
   * 算法名称
   */
  private String algoName;

  /**
   * 算法代码
   */
  private AlgoNameEnum algoCode;

  /**
   * 算法参数配置步骤代码
   */
  private String stepCode;

  /**
   * 算法参数配置步骤内容
   */
  private String stepParametersContent;

  /**
   * 算法API代码
   */
  private String algoApiCode;

  /**
   * 算法所需服务器类型（标准服务器，加密狗服务器）
   */
  private AlgoServerType serverType;

  /**
   * 算法执行状态(初始化：0，运行中：1，运行正确：8，运行失败：9)
   */
  private int algoRunStatus;

  /**
   * 算法执行结果描述(当状态为9时，需要保存错误原因)
   */
  private String algoRunResultDesc;

}
