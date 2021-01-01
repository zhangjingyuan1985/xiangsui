package com.sutpc.transpaas.algoserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sutpc.transpaas.algoserver.dao.GeoSmallZoneMapper;
import com.sutpc.transpaas.algoserver.entity.GeoSmallZone;
import com.sutpc.transpaas.algoserver.service.GeoSmallZoneService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 小区数据查询.
 */
@Service
@Slf4j
public class GeoSmallZoneServiceImpl extends ServiceImpl<GeoSmallZoneMapper, GeoSmallZone>
    implements GeoSmallZoneService {

  @Autowired
  private GeoSmallZoneMapper geoSmallZoneMapper;

  /**
   * 获取小区ID数据列表.
   *
   * @return 所有小区数据集合
   */
  @Override
  public List<GeoSmallZone> getSmallZoneIds() {

    QueryWrapper queryWrapper = new QueryWrapper();
    List<GeoSmallZone> list = this.geoSmallZoneMapper.selectList(queryWrapper);
    return list;
  }

  /**
   * 两个字段的去重查询.
   *
   * @param taskId 模型ID
   * @param userId 用户ID
   * @param attribute 去重对象
   * @return Map对象
   */
  @Override
  public LinkedHashMap<String, List<String>> queryDistinct(String taskId, String userId,
      String attribute) {

    LinkedHashMap<String, List<String>> resultMap = new LinkedHashMap<>();

    String[] args = attribute.toUpperCase().split(",");
    List<String> attributes = new ArrayList<>();
    for (String arg : args) {
      attributes.add(arg);
    }
    QueryWrapper queryWrapperBase = new QueryWrapper<String>();
    List<GeoSmallZone> baseList = this.geoSmallZoneMapper.selectList(queryWrapperBase);
    if (attributes.contains("REGION")) {
      List<String> resultList = baseList.stream().map(base -> base.getRegion())
          .distinct().filter(str -> str != null && StringUtils.isNotEmpty(str))
          .collect(Collectors.toList());
      resultMap.put("REGION", resultList);
    }
    if (attributes.contains("PARK_COST_INDEX")) {
      List<String> resultList = baseList.stream().map(base -> base.getParkCostIndex())
          .distinct().filter(str -> str != null && StringUtils.isNotEmpty(str))
          .collect(Collectors.toList());
      resultMap.put("PARK_COST_INDEX", resultList);
    }
    return resultMap;
  }

}
