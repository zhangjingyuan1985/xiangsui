package com.sutpc.transpaas.algoserver.schedule;

import com.sutpc.transpaas.algoserver.dao.ModelConfigMapper;
import com.sutpc.transpaas.algoserver.service.AlgoRequestService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@Slf4j
public class AlgoScheduleTask {

  @Autowired
  private ModelConfigMapper modelConfigMapper;

  @Autowired
  private AlgoRequestService algoRequestService;
  /**
   * 任务启用开关
   */
  @Value("${client.taskFlag}")
  private boolean taskFlag;

  /**
   * 每5秒执行一次
   */
//    @Scheduled(cron = "0/5 * * * * ?")
  private void configureTasks() {
    if (taskFlag) {
      this.algoRequestService.startAgloTask();
      log.info("算法任务执行");
    }

  }
}
