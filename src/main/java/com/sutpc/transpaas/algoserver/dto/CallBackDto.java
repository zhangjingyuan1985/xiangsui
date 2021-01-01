package com.sutpc.transpaas.algoserver.dto;

import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class CallBackDto implements Serializable {

  @ApiModelProperty(value = "请求唯一识别码")
  private String token;

  @NotEmpty(message = "taskId不能为空")
  @ApiModelProperty(value = "模型ID")
  private String taskId;

  @NotEmpty(message = "subtaskId不能为空")
  @ApiModelProperty(value = "算法Id")
  private String subTaskId;

  @NotEmpty(message = "execStatus不能为空")
  private String execStatus;

  @ApiModelProperty(value = "算法端反馈信息")
  private String msg;
}
