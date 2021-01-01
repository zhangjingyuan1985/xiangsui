package com.sutpc.transpaas.algoserver.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

@TableName("geo_busline")
@Data
public class GeoBusline implements Serializable {

  private int routeId;

  private String routeName;

  private int routeIndex;

  private String pathName;

  private String lineName;

  private int transitMode;

}
