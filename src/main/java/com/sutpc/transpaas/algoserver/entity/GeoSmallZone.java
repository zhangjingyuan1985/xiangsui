package com.sutpc.transpaas.algoserver.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@TableName("geo_small_zone")
@Data
public class GeoSmallZone {

  @TableId
  @ApiModelProperty("主键id")
  private String id;

  private String area;

  private String bigTazId;

  private String midTazId;

  private String bigTazName;

  private String midTazName;

  private String region;

  private String parkCostIndex;

}
