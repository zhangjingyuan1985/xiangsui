package com.sutpc.transpaas.algoserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sutpc.transpaas.algoserver.dao.SegmentVolumeMapper;
import com.sutpc.transpaas.algoserver.dto.response.StationVolumeResponse;
import com.sutpc.transpaas.algoserver.entity.SegmentVolume;
import com.sutpc.transpaas.algoserver.service.SegmentVolumeService;
import com.sutpc.transpaas.algoserver.utils.CSVUtil;
import com.sutpc.transpaas.algoserver.utils.Uuid;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SegmentVolumeServiceImpl
    extends ServiceImpl<SegmentVolumeMapper, SegmentVolume>
    implements SegmentVolumeService {

  @Autowired
  private SegmentVolumeMapper segmentVolumeMapper;

  @Value("${client.standardTaskId}")
  private String standardTaskId;

  /**
   * 读取CSV文件的数据，并入库.
   *
   * @param fileName       文件名称全路径
   * @param modelProjectId 模型ID
   */
  @Transactional(propagation = Propagation.REQUIRED)
  @Override
  public void saveCsv(String fileName, String modelProjectId) {
    String timeSegment = "";
    if (fileName.contains("AM.csv")) {
      timeSegment = "AM";
    }
    if (fileName.contains("EN.csv")) {
      timeSegment = "EN";
    }
    if (fileName.contains("MD.csv")) {
      timeSegment = "MD";
    }
    if (fileName.contains("NT.csv")) {
      timeSegment = "NT";
    }
    if (fileName.contains("PM.csv")) {
      timeSegment = "PM";
    }
    List<LinkedHashMap<String, String>> list = CSVUtil.readCSV(fileName);
    SegmentVolume segmentVolume;
    ObjectMapper objectMapper = new ObjectMapper();
    List<SegmentVolume> segmentVolumeList = new ArrayList<>();
    for (LinkedHashMap<String, String> map : list) {
      segmentVolume = new SegmentVolume();
      segmentVolume = objectMapper.convertValue(map, SegmentVolume.class);
      segmentVolume.setId(Uuid.getSysUuid());
      segmentVolume.setModelProjectId(modelProjectId);
      segmentVolume.setTimeSegment(timeSegment);
      segmentVolumeList.add(segmentVolume);
    }
    long startTime = System.currentTimeMillis();
    this.saveBatch(segmentVolumeList, 1000);
    long endTime = System.currentTimeMillis();
    log.info("保存数据的耗时：{}", endTime - startTime);
  }

  /**
   * 查询站点流量.
   *
   * @param taskId      模型ID
   * @param timeSegment 时间段
   * @param stationType 站点类型（2:公交，3：地铁，5：有轨）
   * @return List
   */
  @Override
  public List<StationVolumeResponse> queryStationVolume(
      String taskId, String timeSegment, int stationType) {

    List<StationVolumeResponse> list = this.segmentVolumeMapper.queryStationVolume(taskId, timeSegment, stationType);
    if (null == list || list.size() ==0) {
      list = this.segmentVolumeMapper.queryStationVolume(standardTaskId, timeSegment, stationType);
    }

    return list;
  }

}
