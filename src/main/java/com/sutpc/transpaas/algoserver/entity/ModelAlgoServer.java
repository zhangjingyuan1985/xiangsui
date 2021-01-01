package com.sutpc.transpaas.algoserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.sutpc.transpaas.algoserver.constant.AlgoServerType;
import lombok.Data;

/**
 * 模型算法服务器资源表
 */
@TableName("t_model_algo_server")
@Data
public class ModelAlgoServer extends BaseEntity {

  /**
   * 算法所需服务器类型（标准服务器，加密狗服务器）
   */
  private AlgoServerType serverType;

  /**
   * 算法IP地址
   */
  private String ipAddress;

  /**
   * 服务器资源状态（0：闲置， 1：计算中）
   */
  private int runStatus;
}
