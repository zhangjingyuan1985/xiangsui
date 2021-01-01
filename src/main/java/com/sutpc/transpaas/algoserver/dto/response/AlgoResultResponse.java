package com.sutpc.transpaas.algoserver.dto.response;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 矩阵数据读取Response
 */
@Data
public class AlgoResultResponse implements Serializable {

  @ApiModelProperty(value = "读取状态（等待算法反馈结果：0，已经收到结果：1，矩阵读取失败：9）")
  private int queryStatus;

  @ApiModelProperty(value = "CSV的结果")
  private List csvResult;

  @ApiModelProperty(value = "OMX的结果")
  private String omxResult;

}
