package com.sutpc.transpaas.algoserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("t_zone_speed_big")
@Data
public class ZoneSpeedBig extends BaseEntity {

  private String modelProjectId;

  private int bigZoneId;

  private double speed;

  /** 时间段（AM,EN,MD,NT,PM） */
  private String timeSegment;

}
