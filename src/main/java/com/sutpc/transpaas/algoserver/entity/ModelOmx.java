package com.sutpc.transpaas.algoserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 矩阵文件读取缓存.
 */
@Data
@TableName("t_model_omx")
public class ModelOmx extends BaseEntity {

  private String modelProjectId;

  private String algoApiCode;

  private String fileName;

  private String matrixName;

  private String execType;

  private int location;

  private String aggregateIndex;

  private int queryStatus;

  private String queryResult;

}
