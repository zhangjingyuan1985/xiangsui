package com.sutpc.transpaas.algoserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("t_zone_speed_mid")
@Data
public class ZoneSpeedMid extends BaseEntity {

  private String modelProjectId;

  private int mediumZoneId;

  private double speed;

  /** 时间段（AM,EN,MD,NT,PM） */
  private String timeSegment;

}
