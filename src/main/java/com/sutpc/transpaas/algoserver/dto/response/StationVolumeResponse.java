package com.sutpc.transpaas.algoserver.dto.response;

import java.io.Serializable;
import lombok.Data;

@Data
public class StationVolumeResponse implements Serializable {

  private int stationId;
  private String stationName;
  private String lng;
  private String lat;
  private long board;
  private long alight;

}
