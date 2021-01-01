package com.sutpc.transpaas.algoserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sutpc.transpaas.algoserver.constant.AlgoFileTypeEnum;
import com.sutpc.transpaas.algoserver.constant.AlgoNameEnum;
import com.sutpc.transpaas.algoserver.dto.AlgoRequestDto;
import com.sutpc.transpaas.algoserver.entity.ModelAlgo;
import com.sutpc.transpaas.algoserver.exception.BusinessException;
import com.sutpc.transpaas.algoserver.service.AlgoParameterService;
import com.sutpc.transpaas.algoserver.service.ModelAlgoService;
import com.sutpc.transpaas.algoserver.service.ServiceAbilityService;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 服务能力生成.
 */
@Service
@Slf4j
public class ServiceAbilityServiceImpl implements ServiceAbilityService {

  @Autowired
  private ModelAlgoService modelAlgoService;
  @Autowired
  private AlgoParameterService algoParameterService;

  /**
   * 服务能力生成.
   *
   * @param taskId 模型ID
   * @param userId 用户ID
   */
  @Override
  public void generate(String taskId, String userId) {

    //1、获取目的地选择--分区岗位设置–小区岗位表
    QueryWrapper<ModelAlgo> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("model_project_id", taskId);
    queryWrapper.eq("algo_code", AlgoNameEnum.DESTINATION_SELECTION.getCode());
    queryWrapper.eq("step_code", "SMALL_ZONE_WORK");
    queryWrapper.eq("create_by", userId);
    ModelAlgo smallZoneWork = this.modelAlgoService.getOne(queryWrapper);
    if (null == smallZoneWork) {
      throw new BusinessException("404", "小区岗位表未配置");
    }
    //2、获取目的地选择--SV设置--权重公式
    QueryWrapper<ModelAlgo> queryWrapperSv = new QueryWrapper<>();
    queryWrapperSv.eq("model_project_id", taskId);
    queryWrapperSv.eq("algo_code", AlgoNameEnum.DESTINATION_SELECTION.getCode());
    queryWrapperSv.eq("step_code", "SV_FORMULA");
    queryWrapperSv.eq("create_by", userId);
    ModelAlgo svFormula = this.modelAlgoService.getOne(queryWrapperSv);
    if (null == svFormula) {
      throw new BusinessException("404", "SV权重公式未配置");
    }
    List<LinkedHashMap> smallZoneWorkList =
        JSON.parseArray(smallZoneWork.getStepParametersContent(), LinkedHashMap.class);
    List<LinkedHashMap> svFormulaList =
        JSON.parseArray(svFormula.getStepParametersContent(), LinkedHashMap.class);

    List<LinkedHashMap> resultList = new ArrayList<>();
    for (LinkedHashMap szwMap : smallZoneWorkList) {
      LinkedHashMap<String, Object> resultMap = new LinkedHashMap<>();
      resultMap.put("id", szwMap.get("id"));
      for (LinkedHashMap svMap : svFormulaList) {
        //key:FBF, value:1*大学教职工 + 1*事业单位
        String purposeTypeKey = svMap.get("purposeType").toString().trim();
        String purposeTypeValue = svMap.get("formula").toString().trim();
        //values数据格式[1*大学教职工, 1*事业单位]
        String[] values = purposeTypeValue.split("\\+");
        //formulaValue数据格式1*大学教职工
        double indexValue = 0.00;
        for (String formulaValue : values) {
          String[] formulaArgs = formulaValue.split("\\*");
          //第一个数是指数1，第二个数是汉字作为Key
          try {
            double index = Double.valueOf(formulaArgs[0]);
            double data = 0;
            if(null != szwMap.get(formulaArgs[1]) && StringUtils.isNotEmpty(szwMap.get(formulaArgs[1])+"")) {
              data = Double.valueOf(szwMap.get(formulaArgs[1]) + "");
            }
            indexValue = indexValue + index * data;
          } catch (Exception ex) {
            log.error("数据转换异常svMap={}, szwMap={},index={}, data={}",svMap, szwMap, Double.valueOf(formulaArgs[0]), szwMap.get(formulaArgs[1]));
          }
        }
        resultMap.put(purposeTypeKey, indexValue);
      }
      resultList.add(resultMap);
    }

    AlgoRequestDto algoRequestDto = new AlgoRequestDto();
    algoRequestDto.setTaskId(taskId);
    algoRequestDto.setUserId(userId);
    algoRequestDto.setStepCode("M04_01_01");
    algoRequestDto.setFileType(AlgoFileTypeEnum.CSV);
    algoRequestDto.setAlgoNameEnum(AlgoNameEnum.DESTINATION_SELECTION);
    algoRequestDto.setFileContent(JSON.toJSONString(resultList));
    Thread thread = new Thread(() -> algoParameterService.saveUserFile(algoRequestDto));
    thread.start();
  }

}
