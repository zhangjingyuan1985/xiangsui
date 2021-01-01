package com.sutpc.transpaas.algoserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sutpc.transpaas.algoserver.entity.GeoLink;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface GeoLinkMapper extends BaseMapper<GeoLink> {

  /**
   * 根据线路名查询单条线路断面流量.
   *
   * @param taskId 模型ID
   * @param timeSegment 时间段
   * @param line 线路ID R1023
   * @return List
   */
  public List<GeoLink> queryWktByLine(
      @Param("taskId") String taskId,
      @Param("timeSegment") String timeSegment,
      @Param("line") String line);

}
