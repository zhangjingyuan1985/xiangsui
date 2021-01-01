package com.sutpc.transpaas.algoserver;

import com.sutpc.transpaas.algoserver.service.ModelProjectService;
import java.util.HashSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ModelProjectServiceTest {

  @Autowired
  private ModelProjectService modelProjectService;

//  @Test
  public void testChangeInteractParameters() {
    String taskId = "35d6c3597cb44350893e9adf7c257729";
    int paramValue = 1;
    HashSet<String> stepCodeSet = new HashSet<>();
    stepCodeSet.add("M02_01_01");
    stepCodeSet.add("EXECUTE");
    stepCodeSet.add("M03_05_01");
    this.modelProjectService.changeInteractParameters(taskId, stepCodeSet, paramValue);
  }
}
