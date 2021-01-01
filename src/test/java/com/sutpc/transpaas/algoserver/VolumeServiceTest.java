package com.sutpc.transpaas.algoserver;

import com.sutpc.transpaas.algoserver.service.LinkTransitVolumeService;
import com.sutpc.transpaas.algoserver.service.LinkVolumeService;
import com.sutpc.transpaas.algoserver.service.SegmentVolumeService;
import com.sutpc.transpaas.algoserver.service.ZoneSpeedBigService;
import com.sutpc.transpaas.algoserver.service.ZoneSpeedMidService;
import java.io.File;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
public class VolumeServiceTest {

  @Autowired
  private LinkVolumeService volumeService;
  @Autowired
  private LinkTransitVolumeService linkTransitVolumeService;
  @Autowired
  private SegmentVolumeService segmentVolumeService;
  @Autowired
  private ZoneSpeedMidService zoneSpeedMidService;
  @Autowired
  private ZoneSpeedBigService zoneSpeedBigService;

  @Value("${client.modelOutputFile}")
  private String modelOutputFile;

  @Value("${client.standardTaskId}")
  private String standardTaskId;

  @Test
  public void testSaveData() {
//    String[] linkVolumeArgs = {"M07_R02_AM.csv","M07_R02_EN.csv","M07_R02_MD.csv","M07_R02_NT.csv","M07_R02_PM.csv"};
//    for (String linkVolumeStr : linkVolumeArgs) {
//      String fileName = modelOutputFile + File.separator + linkVolumeStr;
//      volumeService.saveCsv(fileName, standardTaskId);
//    }
    String[] zoneSpeedMidArgs = {"M07_R03_AM.csv","M07_R03_EN.csv","M07_R03_MD.csv","M07_R03_NT.csv","M07_R03_PM.csv"};
    for (String zoneSpeedMidStr : zoneSpeedMidArgs) {
      String fileName = modelOutputFile + File.separator + zoneSpeedMidStr;
      zoneSpeedMidService.saveCsv(fileName, standardTaskId);
    }
    String[] zoneSpeedBigArgs = {"M07_R04_AM.csv","M07_R04_EN.csv","M07_R04_MD.csv","M07_R04_NT.csv","M07_R04_PM.csv"};
    for (String zoneSpeedBigStr : zoneSpeedBigArgs) {
      String fileName = modelOutputFile + File.separator + zoneSpeedBigStr;
      zoneSpeedBigService.saveCsv(fileName, standardTaskId);
    }
//    String[] linkTransitVolumeArgs = {"M08_R02_AM.csv","M08_R02_EN.csv","M08_R02_MD.csv","M08_R02_NT.csv","M08_R02_PM.csv"};
//    for (String linkTransitVolumeStr : linkTransitVolumeArgs) {
//      String fileName = modelOutputFile + File.separator + linkTransitVolumeStr;
//      linkTransitVolumeService.saveCsv(fileName, standardTaskId);
//    }
//    String[] segmentVolumeArgs = {"M08_R03_AM.csv","M08_R03_EN.csv","M08_R03_MD.csv","M08_R03_NT.csv","M08_R03_PM.csv"};
//    for (String segmentVolumeStr : segmentVolumeArgs) {
//      String fileName = modelOutputFile + File.separator + segmentVolumeStr;
//      segmentVolumeService.saveCsv(fileName, standardTaskId);
//    }
  }
}
