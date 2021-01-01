package com.sutpc.transpaas.algoserver.utils;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

/**
 * UUID生成.
 */
public class Uuid {

  @ApiModelProperty(hidden = true)
  @JsonIgnore
  @TableField(exist = false)
  private String sysUuid;

  public static String getSysUuid() {
    return java.util.UUID.randomUUID().toString().replace("-", "");
  }
}
