package com.sutpc.transpaas.algoserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("t_segment_volume")
@Data
public class SegmentVolume extends BaseEntity {

  private String modelProjectId;

  private String line;

  private String segment;

  private double length;

  private double timeMin;

  private double speed;

  private double loadFactor;

  private double volume;

  private double stop;

  private double board;

  private double alight;

  private double prob;

  /** 时间段（AM,EN,MD,NT,PM） */
  private String timeSegment;

}
