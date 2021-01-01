package com.sutpc.transpaas.algoserver.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sutpc.transpaas.algoserver.dao.LinkTransitVolumeMapper;
import com.sutpc.transpaas.algoserver.entity.LinkTransitVolume;
import com.sutpc.transpaas.algoserver.service.LinkTransitVolumeService;
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
public class LinkTransitVolumeServiceImpl
    extends ServiceImpl<LinkTransitVolumeMapper, LinkTransitVolume>
    implements LinkTransitVolumeService {

  /**
   * 读取CSV文件的数据，并入库.
   *
   * @param fileName 文件名称全路径
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
    LinkTransitVolume linkTransitVolume;
    ObjectMapper objectMapper = new ObjectMapper();
    List<LinkTransitVolume> linkTransitVolumeList = new ArrayList<>();
    for (LinkedHashMap<String, String> map : list) {
      linkTransitVolume = new LinkTransitVolume();
      linkTransitVolume = objectMapper.convertValue(map, LinkTransitVolume.class);
      linkTransitVolume.setId(Uuid.getSysUuid());
      linkTransitVolume.setModelProjectId(modelProjectId);
      linkTransitVolume.setTimeSegment(timeSegment);
      linkTransitVolumeList.add(linkTransitVolume);
    }
    long startTime = System.currentTimeMillis();
    this.saveBatch(linkTransitVolumeList, 1000);
    long endTime = System.currentTimeMillis();
    log.info("保存数据的耗时：{}", endTime - startTime);
  }
}
