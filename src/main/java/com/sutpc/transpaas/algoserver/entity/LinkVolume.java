package com.sutpc.transpaas.algoserver.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@TableName("t_link_volume")
@Data
public class LinkVolume extends BaseEntity {

  private String modelProjectId;

  @TableField(value = "from_node", exist = true)
  private String from;

  @TableField(value = "to_node", exist = true)
  private String to;

  private double volau;

  private double timau;

  private double speedau;

  private double length;

  /** 时间段（AM,EN,MD,NT,PM） */
  private String timeSegment;

}
