package com.sutpc.transpaas.algoserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sutpc.transpaas.algoserver.dto.response.StationVolumeResponse;
import com.sutpc.transpaas.algoserver.entity.SegmentVolume;
import java.util.List;

public interface SegmentVolumeService extends IService<SegmentVolume>  {

  /**
   * 读取CSV文件的数据，并入库.
   *
   * @param fileName 文件名称全路径
   * @param modelProjectId 模型ID
   */
  public void saveCsv(String fileName, String modelProjectId);

  /**
   * 查询站点流量.
   *
   * @param taskId 模型ID
   * @param stationType 站点类型（2:公交，3：地铁，5：有轨）
   * @param stationType 站点类型（2:公交，3：地铁，5：有轨）
   * @return List
   */
  public List<StationVolumeResponse> queryStationVolume(
      String taskId,
      String timeSegment,
      int stationType);
}
