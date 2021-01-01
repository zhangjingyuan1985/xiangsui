package com.sutpc.transpaas.algoserver;

import com.sutpc.transpaas.algoserver.constant.AlgoFileTypeEnum;
import com.sutpc.transpaas.algoserver.constant.AlgoNameEnum;
import com.sutpc.transpaas.algoserver.dto.AlgoRequestDto;
import com.sutpc.transpaas.algoserver.service.AlgoParameterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class AlgoParameterServiceTest {

  @Autowired
  private AlgoParameterService algoParameterService;

//  @Test
  public void testAnalyseParameterFile() {

    String req =
        "{\"Demand\":{\"ActualLoadRate\":{\"Taxi\":{\"AM\":1.5,\"MD\":\"1.2000\",\"PM\":\"1.2000\",\"EN\":\"1.2000\",\"NT\":\"1.2000\"},\"TNCs\":{\"AM\":\"1.2000\",\"MD\":\"1.2000\",\"PM\":\"1.2000\",\"EN\":\"1.2000\",\"NT\":\"1.2000\"}}}}";
    String taskId = "e18c190c50d44a2e98348728b5219c15";
    String result = this.algoParameterService.analyseParameterFile(taskId, req);
    log.info(result);
  }

//  @Test
  public void testSaveUserFile() {

    AlgoRequestDto algoRequestDto = new AlgoRequestDto();
    algoRequestDto.setAlgoNameEnum(AlgoNameEnum.DESTINATION_SELECTION);
    algoRequestDto.setStepCode("WORK_CONFIG");
    algoRequestDto.setFileType(AlgoFileTypeEnum.CSV);
    algoRequestDto.setUserId("admin");
    algoRequestDto.setTaskId("b7a456b5c5b24df4b82d471ced5c5f7f");
    String fileContent =
        "[{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"教育\",\"configValue\":\"小学教职工\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":16,\"id\":\"7\",\"isDeleted\":0,\"tableOrder\":17,\"updateBy\":\"\",\"updateTime\":null},{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"教育\",\"configValue\":\"中学教职工\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":17,\"id\":\"8\",\"isDeleted\":0,\"tableOrder\":18,\"updateBy\":\"\",\"updateTime\":null},{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"教育\",\"configValue\":\"大学教职工\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":18,\"id\":\"9\",\"isDeleted\":0,\"tableOrder\":19,\"updateBy\":\"\",\"updateTime\":null},{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"学生\",\"configValue\":\"小学\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":12,\"id\":\"3\",\"isDeleted\":0,\"tableOrder\":13,\"updateBy\":\"\",\"updateTime\":null},{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"学生\",\"configValue\":\"初中\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":13,\"id\":\"4\",\"isDeleted\":0,\"tableOrder\":14,\"updateBy\":\"\",\"updateTime\":null},{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"学生\",\"configValue\":\"高中\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":14,\"id\":\"5\",\"isDeleted\":0,\"tableOrder\":15,\"updateBy\":\"\",\"updateTime\":null},{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"学生\",\"configValue\":\"大学\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":15,\"id\":\"6\",\"isDeleted\":0,\"tableOrder\":16,\"updateBy\":\"\",\"updateTime\":null},{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"服务\",\"configValue\":\"酒店\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":4,\"id\":\"14\",\"isDeleted\":0,\"tableOrder\":5,\"updateBy\":\"\",\"updateTime\":null},{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"服务\",\"configValue\":\"商业综合体\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":5,\"id\":\"15\",\"isDeleted\":0,\"tableOrder\":6,\"updateBy\":\"\",\"updateTime\":null},{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"服务\",\"configValue\":\"大型超市\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":6,\"id\":\"16\",\"isDeleted\":0,\"tableOrder\":7,\"updateBy\":\"\",\"updateTime\":null},{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"服务\",\"configValue\":\"餐饮\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":8,\"id\":\"18\",\"isDeleted\":0,\"tableOrder\":9,\"updateBy\":\"\",\"updateTime\":null},{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"服务\",\"configValue\":\"零售\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":9,\"id\":\"19\",\"isDeleted\":0,\"tableOrder\":10,\"updateBy\":\"\",\"updateTime\":null},{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"其他\",\"configValue\":\"企业白领\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":1,\"id\":\"11\",\"isDeleted\":0,\"tableOrder\":2,\"updateBy\":\"\",\"updateTime\":null},{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"其他\",\"configValue\":\"个体户\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":2,\"id\":\"12\",\"isDeleted\":0,\"tableOrder\":3,\"updateBy\":\"\",\"updateTime\":null},{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"其他\",\"configValue\":\"仓储\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":7,\"id\":\"17\",\"isDeleted\":0,\"tableOrder\":8,\"updateBy\":\"\",\"updateTime\":null},{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"其他\",\"configValue\":\"事业单位\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":19,\"id\":\"10\",\"isDeleted\":0,\"tableOrder\":20,\"updateBy\":\"\",\"updateTime\":null},{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"资源\",\"configValue\":\"农林牧渔\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":10,\"id\":\"1\",\"isDeleted\":0,\"tableOrder\":11,\"updateBy\":\"\",\"updateTime\":null},{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"资源\",\"configValue\":\"工业\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":11,\"id\":\"2\",\"isDeleted\":0,\"tableOrder\":12,\"updateBy\":\"\",\"updateTime\":null},{\"configDesc\":\"岗位分类\",\"configGroup\":\"JOB_TYPE\",\"configKey\":\"医疗\",\"configValue\":\"医院职工\",\"createBy\":\"\",\"createTime\":\"2020-11-23 00:00:00\",\"groupOrder\":3,\"id\":\"13\",\"isDeleted\":0,\"tableOrder\":4,\"updateBy\":\"\",\"updateTime\":null}]";
    algoRequestDto.setFileContent(fileContent);
    this.algoParameterService.saveUserFile(algoRequestDto);
  }

}
