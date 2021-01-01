package com.sutpc.transpaas.algoserver.dto;

import com.sutpc.transpaas.algoserver.constant.AlgoFileTypeEnum;
import com.sutpc.transpaas.algoserver.constant.AlgoNameEnum;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AlgoRequestDto implements Serializable {

  @NotEmpty(message = "userId不能为空")
  @ApiModelProperty(value = "用户ID")
  private String userId;

  @NotEmpty(message = "taskId不能为空")
  @ApiModelProperty(value = "模型ID")
  private String taskId;

  @NotNull(message = "algoNameEnum不能为空")
  @ApiModelProperty(value = "算法步骤枚举")
  private AlgoNameEnum algoNameEnum;

  @NotEmpty(message = "stepCode不能为空")
  @ApiModelProperty(value = "算法参数配置步骤代码", example = "M02_01_01")
  private String stepCode;

  @ApiModelProperty(value = "文件类型", example = "CSV")
  @NotNull(message = "fileType不能为空")
  private AlgoFileTypeEnum fileType;

  @ApiModelProperty(value = "用户参数内容，后台根据要求，将数据生成文件csv,json,yaml")
  private String fileContent;
}
