package com.sutpc.transpaas.algoserver;

import com.alibaba.fastjson.JSON;
import com.sutpc.transpaas.algoserver.constant.AlgoServerType;
import com.sutpc.transpaas.algoserver.entity.ModelAlgoServer;
import com.sutpc.transpaas.algoserver.service.ModelAlgoServerService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class ModelAlgoServerServiceTest {
  @Autowired private ModelAlgoServerService modelAlgoServerService;

  // @Test
  public void getServerTest() {
    ModelAlgoServer modelAlgoServer = this.modelAlgoServerService.getServer(AlgoServerType.BZ_PC);
    log.info(JSON.toJSONString(modelAlgoServer));

    List<ModelAlgoServer> list = this.modelAlgoServerService.queryServerList(AlgoServerType.BZ_PC);
    log.info(JSON.toJSONString(list));
  }
}
