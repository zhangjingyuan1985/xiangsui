package com.sutpc.transpaas.algoserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sutpc.transpaas.algoserver.entity.GeoBusline;
import com.sutpc.transpaas.algoserver.entity.GeoLink;
import com.sutpc.transpaas.algoserver.entity.LinkVolume;
import com.sutpc.transpaas.algoserver.entity.SegmentVolume;
import com.sutpc.transpaas.algoserver.entity.ZoneSpeedBig;
import com.sutpc.transpaas.algoserver.entity.ZoneSpeedMid;
import java.util.List;

public interface GeoBuslineService extends IService<GeoBusline> {

  /**
   * 根据线路类型查询线路列表.
   *
   * @param transitMode 线路类型
   * @return
   */
  public List<GeoBusline> queryListByMode(int transitMode);

  /**
   * 根据线路名查询单条线路断面流量.
   *
   * @param taskId 模型ID
   * @param timeSegment 时间段
   * @param line 线路名称，需拼接R
   * @return List
   */
  public List<GeoLink> queryWktByLine(String taskId, String timeSegment, String line);

  /**
   * 根据线路名查询单条线路断面流量.
   *
   * @param taskId 模型ID
   * @param routeName 线路名称
   * @return List集合
   */
  public List<SegmentVolume> queryListByRouteName(String taskId, String routeName);

  /**
   * 查询所有时段速度.
   *
   * @param taskId 模型ID
   * @param fromNode fromNode值
   * @param toNode toNode值
   * @return List集合
   */
  public List<LinkVolume> querySpeedByFromIdAndToId(String taskId, String fromNode, String toNode);

  /**
   * 根据片区类型、时段，查询中区或者大区的运行速度.
   *
   * @param taskId 模型ID
   * @param timeSegment 时间段（AM,EN,MD,NT,PM）
   * @return List集合
   */
  public List<ZoneSpeedMid> queryZoneSpeedMid(String taskId, String timeSegment);

  /**
   * 根据片区类型、时段，查询中区或者大区的运行速度.
   *
   * @param taskId 模型ID
   * @param timeSegment 时间段（AM,EN,MD,NT,PM）
   * @return List集合
   */
  public List<ZoneSpeedBig> queryZoneSpeedBig(String taskId, String timeSegment);

}
