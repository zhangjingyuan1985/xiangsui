package com.sutpc.transpaas.algoserver.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelProjectDeleteDto {

  @NotEmpty(message = "taskId不能为空")
  @ApiModelProperty(value = "模型的ID")
  private String taskId;

  @NotNull(message = "userId不能为空")
  @ApiModelProperty(value = "用户ID")
  private String createUser;

}
