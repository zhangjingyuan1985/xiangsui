package com.sutpc.transpaas.algoserver.dto;

import com.sutpc.transpaas.algoserver.constant.AlgoNameEnum;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * 算法执行结果入参
 */
@Data
public class AlgoResultDto implements Serializable {

  @NotEmpty(message = "taskId不能为空")
  @ApiModelProperty(value = "模型ID")
  private String taskId;

  @ApiModelProperty(value = "算法步骤枚举")
  private AlgoNameEnum algoNameEnum;

  @NotEmpty(message = "userId不能为空")
  @ApiModelProperty(value = "用户ID")
  private String userId;

  @NotEmpty(message = "fileType不能为空")
  @ApiModelProperty(value = "文件类型：CSV/OMX")
  private String fileType;

  @ApiModelProperty(value = "输出文件的文件名称")
  private String fileName;

  @ApiModelProperty(value = "矩阵名称")
  private String matrixName;

  @ApiModelProperty(value = "读行：row,读列：col,聚合：aggregate")
  private String execType;

  @ApiModelProperty(value = "矩阵的行数或者列数")
  private int location;

  @ApiModelProperty(value = "聚合：小区ID对应中区")
  private String aggregateIndex;
}
