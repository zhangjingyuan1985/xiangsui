package com.sutpc.transpaas.algoserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sutpc.transpaas.algoserver.dto.response.StationVolumeResponse;
import com.sutpc.transpaas.algoserver.entity.SegmentVolume;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface SegmentVolumeMapper extends BaseMapper<SegmentVolume> {

  /**
   * 查询站点流量.
   *
   * @param taskId 模型ID
   * @param timeSegment 时间段
   * @param stationType 站点类型（2:公交，3：地铁，5：有轨）
   * @return List
   */
  public List<StationVolumeResponse> queryStationVolume(
      @Param("taskId") String taskId,
      @Param("timeSegment") String timeSegment,
      @Param("stationType") int stationType);
}
