package com.sutpc.transpaas.algoserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sutpc.transpaas.algoserver.dao.ZoneSpeedBigMapper;
import com.sutpc.transpaas.algoserver.entity.ZoneSpeedBig;
import com.sutpc.transpaas.algoserver.service.ZoneSpeedBigService;
import com.sutpc.transpaas.algoserver.utils.CSVUtil;
import com.sutpc.transpaas.algoserver.utils.Uuid;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ZoneSpeedBigServiceImpl
    extends ServiceImpl<ZoneSpeedBigMapper, ZoneSpeedBig>
    implements ZoneSpeedBigService {

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
    ZoneSpeedBig zoneSpeedBig;
    ObjectMapper objectMapper = new ObjectMapper();
    List<ZoneSpeedBig> zoneSpeedBigList = new ArrayList<>();
    for (LinkedHashMap<String, String> map : list) {
      zoneSpeedBig = new ZoneSpeedBig();
      zoneSpeedBig = objectMapper.convertValue(map, ZoneSpeedBig.class);
      zoneSpeedBig.setId(Uuid.getSysUuid());
      zoneSpeedBig.setModelProjectId(modelProjectId);
      zoneSpeedBig.setTimeSegment(timeSegment);
      zoneSpeedBigList.add(zoneSpeedBig);
    }
    long startTime = System.currentTimeMillis();
    this.saveBatch(zoneSpeedBigList, 1000);
    long endTime = System.currentTimeMillis();
    log.info("保存数据的耗时：{}", endTime - startTime);
  }
}
