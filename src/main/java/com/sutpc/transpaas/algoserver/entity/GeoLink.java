package com.sutpc.transpaas.algoserver.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

@TableName("geo_link")
@Data
public class GeoLink implements Serializable {


  private long fromId;

  private long toId;

  private String wkt;

  @TableField(exist = false)
  private String line;
  @TableField(exist = false)
  private double speed;
  @TableField(exist = false)
  private int volume;

}
