package com.sutpc.transpaas.algoserver.dto;

import com.sutpc.transpaas.algoserver.constant.AlgoNameEnum;
import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 *
 */
@Data
public class AlgoResultQueryDto {

  @NotEmpty(message = "taskId不能为空")
  @ApiModelProperty(value = "模型ID")
  private String taskId;

  @ApiModelProperty(value = "算法步骤枚举")
  private AlgoNameEnum algoNameEnum;

  @NotEmpty(message = "userId不能为空")
  @ApiModelProperty(value = "用户ID")
  private String userId;


}
