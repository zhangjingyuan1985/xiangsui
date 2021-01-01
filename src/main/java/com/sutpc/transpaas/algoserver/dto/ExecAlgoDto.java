package com.sutpc.transpaas.algoserver.dto;

import com.sutpc.transpaas.algoserver.constant.AlgoNameEnum;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 执行算法的参数
 */
@Data
public class ExecAlgoDto implements Serializable {

  @NotEmpty(message = "taskId不能为空")
  @ApiModelProperty(value = "模型ID")
  private String taskId;

  @NotNull(message = "algoNameEnum不能为空")
  @ApiModelProperty(value = "算法步骤枚举")
  private AlgoNameEnum algoNameEnum;

  @ApiModelProperty(value = "用户ID")
  private String userId;
}
