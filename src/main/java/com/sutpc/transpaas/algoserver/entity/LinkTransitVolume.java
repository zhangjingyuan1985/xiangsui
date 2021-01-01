package com.sutpc.transpaas.algoserver.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("t_link_transit_volume")
@Data
public class LinkTransitVolume extends BaseEntity {

  private String modelProjectId;

  @TableField(value = "from_node", exist = true)
  private String from;

  @TableField(value = "to_node", exist = true)
  private String to;

  private double length;

  private String modes;

  private double type;

  private double noLines;

  private double noVehs;

  private double seatedCap;

  private double totalCap;

  private double volume;

  private double avgLoad;

  private double auxVolume;

  /** 时间段（AM,EN,MD,NT,PM） */
  private String timeSegment;

}
