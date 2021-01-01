package com.sutpc.transpaas.algoserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

@TableName("line_stop_rela")
@Data
public class LineStopRela implements Serializable {

  private int routeIndex;

  private int routeId;

  private String stationName;

  private double lng;

  private double lat;

}
