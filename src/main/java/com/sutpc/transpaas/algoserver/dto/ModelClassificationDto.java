package com.sutpc.transpaas.algoserver.dto;

import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class ModelClassificationDto {

  @NotEmpty(message = "userId不能为空")
  @ApiModelProperty(value = "用户ID")
  private String userId;

  @NotEmpty(message = "taskId不能为空")
  @ApiModelProperty(value = "模型ID")
  private String taskId;

  @ApiModelProperty(value = "ModelClassificationProject对象的数组结构")
  private List<ModelClassificationProjectDto> dataList;
}
