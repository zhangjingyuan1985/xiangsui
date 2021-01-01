package com.sutpc.transpaas.algoserver.dto.response;

import com.sutpc.transpaas.algoserver.constant.AlgoNameEnum;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

@Data
public class ModelAlgoResponse implements Serializable {

  @ApiModelProperty(value = "模型ID")
  private String modelProjectId;

  @ApiModelProperty(value = "算法代码")
  private AlgoNameEnum algoCode;

  @ApiModelProperty(value = "算法参数配置步骤代码")
  private String stepCode;

  @ApiModelProperty(value = "步骤影响状态（0：加载服务器端数据，1：前端自行生成数据）")
  private int stepRelationStatus;
}
