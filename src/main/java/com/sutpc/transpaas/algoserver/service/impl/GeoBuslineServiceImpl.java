package com.sutpc.transpaas.algoserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sutpc.transpaas.algoserver.dao.GeoBuslineMapper;
import com.sutpc.transpaas.algoserver.dao.GeoLinkMapper;
import com.sutpc.transpaas.algoserver.dao.LinkVolumeMapper;
import com.sutpc.transpaas.algoserver.dao.SegmentVolumeMapper;
import com.sutpc.transpaas.algoserver.dao.ZoneSpeedBigMapper;
import com.sutpc.transpaas.algoserver.dao.ZoneSpeedMidMapper;
import com.sutpc.transpaas.algoserver.entity.GeoBusline;
import com.sutpc.transpaas.algoserver.entity.GeoLink;
import com.sutpc.transpaas.algoserver.entity.LinkVolume;
import com.sutpc.transpaas.algoserver.entity.SegmentVolume;
import com.sutpc.transpaas.algoserver.entity.ZoneSpeedBig;
import com.sutpc.transpaas.algoserver.entity.ZoneSpeedMid;
import com.sutpc.transpaas.algoserver.service.GeoBuslineService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class GeoBuslineServiceImpl extends ServiceImpl<GeoBuslineMapper, GeoBusline>
    implements GeoBuslineService {

  @Autowired
  private GeoBuslineMapper geoBuslineMapper;
  @Autowired
  private LinkVolumeMapper linkVolumeMapper;
  @Autowired
  private ZoneSpeedBigMapper zoneSpeedBigMapper;
  @Autowired
  private ZoneSpeedMidMapper zoneSpeedMidMapper;
  @Autowired
  private SegmentVolumeMapper segmentVolumeMapper;
  @Autowired
  private GeoLinkMapper geoLinkMapper;

  @Value("${client.standardTaskId}")
  private String standardTaskId;

  /**
   * 根据线路类型查询线路列表.
   *
   * @param transitMode 线路类型
   * @return
   */
  @Override
  public List<GeoBusline> queryListByMode(int transitMode) {
    QueryWrapper<GeoBusline> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("transit_mode", transitMode);
    queryWrapper.orderByAsc("route_id");
    return geoBuslineMapper.selectList(queryWrapper);
  }

  /**
   * 根据线路名查询单条线路断面流量.
   *
   * @param taskId 模型ID
   * @param timeSegment 时间段
   * @param line   线路名称，需拼接R
   * @return List
   */
  @Override
  public List<GeoLink> queryWktByLine(String taskId, String timeSegment, String line) {
    line = 'R' + line;
    List<GeoLink> list = this.geoLinkMapper.queryWktByLine(taskId, timeSegment, line);
    if (null == list || list.size() == 0) {
      list = this.geoLinkMapper.queryWktByLine(standardTaskId, timeSegment, line);
    }
    return list;
  }

  /**
   * 根据线路名查询单条线路断面流量.
   *
   * @param taskId 模型ID
   * @param routeName 线路名称
   * @return List集合
   */
  @Override
  public List<SegmentVolume> queryListByRouteName(String taskId, String routeName) {
    QueryWrapper<SegmentVolume> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("model_project_id", taskId);
    queryWrapper.eq("line", routeName);
    List<SegmentVolume> list = segmentVolumeMapper.selectList(queryWrapper);
    if (null == list || list.size() == 0) {
      QueryWrapper<SegmentVolume> queryStandWrapper = new QueryWrapper<>();
      queryStandWrapper.eq("model_project_id", standardTaskId);
      queryStandWrapper.eq("line", routeName);
      list = segmentVolumeMapper.selectList(queryStandWrapper);
    }
    return list;
  }

  /**
   * 查询所有时段速度.
   *
   * @param taskId 模型ID
   * @param fromNode fromNode值
   * @param toNode   toNode值
   * @return List集合
   */
  @Override
  public List<LinkVolume> querySpeedByFromIdAndToId(String taskId, String fromNode, String toNode) {

    QueryWrapper<LinkVolume> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("model_project_id", taskId);
    queryWrapper.eq("from_node", fromNode);
    queryWrapper.eq("to_node", toNode);
    List<LinkVolume> list = linkVolumeMapper.selectList(queryWrapper);
    if (null == list || list.size() == 0) {
      QueryWrapper<LinkVolume> queryStandWrapper = new QueryWrapper<>();
      queryStandWrapper.eq("model_project_id", standardTaskId);
      queryStandWrapper.eq("from_node", fromNode);
      queryStandWrapper.eq("to_node", toNode);
      list = linkVolumeMapper.selectList(queryStandWrapper);
    }
    return list;
  }

  /**
   * 根据片区类型、时段，查询中区或者大区的运行速度.
   *
   * @param taskId      模型ID
   * @param timeSegment 时间段（AM,EN,MD,NT,PM）
   * @return List集合
   */
  @Override
  public List<ZoneSpeedMid> queryZoneSpeedMid(String taskId, String timeSegment) {
    QueryWrapper<ZoneSpeedMid> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("model_project_id", taskId);
    queryWrapper.eq("time_segment", timeSegment);
    List<ZoneSpeedMid> list = zoneSpeedMidMapper.selectList(queryWrapper);
    if (null == list || list.size() == 0) {
      QueryWrapper<ZoneSpeedMid> queryStandWrapper = new QueryWrapper<>();
      queryStandWrapper.eq("model_project_id", standardTaskId);
      queryStandWrapper.eq("time_segment", timeSegment);
      list = zoneSpeedMidMapper.selectList(queryStandWrapper);
    }
    return list;
  }

  /**
   * 根据片区类型、时段，查询中区或者大区的运行速度.
   *
   * @param taskId      模型ID
   * @param timeSegment 时间段（AM,EN,MD,NT,PM）
   * @return List集合
   */
  @Override
  public List<ZoneSpeedBig> queryZoneSpeedBig(String taskId, String timeSegment) {
    QueryWrapper<ZoneSpeedBig> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("model_project_id", taskId);
    queryWrapper.eq("time_segment", timeSegment);
    List<ZoneSpeedBig> list = zoneSpeedBigMapper.selectList(queryWrapper);
    if (null == list || list.size() == 0) {
      QueryWrapper<ZoneSpeedBig> queryStandWrapper = new QueryWrapper<>();
      queryStandWrapper.eq("model_project_id", standardTaskId);
      queryStandWrapper.eq("time_segment", timeSegment);
      list = zoneSpeedBigMapper.selectList(queryStandWrapper);
    }
    return list;
  }

}
