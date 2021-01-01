package com.sutpc.transpaas.algoserver.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ModelProjectDto {

  @NotEmpty(message = "modelName不能为空")
  @ApiModelProperty(value = "模型名称")
  private String modelName;

  @ApiModelProperty(value = "模型全局参数")
  private String modelParameters;

  @NotEmpty(message = "createUser不能为空")
  @ApiModelProperty(value = "模型创建人")
  private String createUser;

}
