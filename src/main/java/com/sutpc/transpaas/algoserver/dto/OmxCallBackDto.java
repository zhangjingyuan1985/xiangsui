package com.sutpc.transpaas.algoserver.dto;

import io.swagger.annotations.ApiModelProperty;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

/**
 * OMX文件读取的回调参数.
 */
@Data
public class OmxCallBackDto {

  @ApiModelProperty(value = "请求唯一识别码")
  @NotEmpty(message = "token不能为空")
  private String token;

  @NotEmpty(message = "execStatus不能为空")
  private String execStatus;

  //反馈的数据信息
  private String data;

}
