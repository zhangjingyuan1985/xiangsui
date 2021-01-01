package com.sutpc.transpaas.algoserver.service;

import com.sutpc.transpaas.algoserver.entity.GeoSmallZone;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * 小区信息.
 */
public interface GeoSmallZoneService {

  /**
   * 获取小区ID数据列表.
   *
   * @return List对象
   */
  public List<GeoSmallZone> getSmallZoneIds();

  /**
   *  去重查询.
   *
   * @param taskId 模型ID
   * @param userId 用户ID
   * @param attribute 去重对象
   * @return map对象
   */
  LinkedHashMap<String, List<String>> queryDistinct(String taskId, String userId, String attribute);
}
