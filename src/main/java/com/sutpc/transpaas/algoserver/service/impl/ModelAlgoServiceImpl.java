package com.sutpc.transpaas.algoserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sutpc.transpaas.algoserver.constant.AlgoConstant;
import com.sutpc.transpaas.algoserver.dao.ModelAlgoMapper;
import com.sutpc.transpaas.algoserver.dto.AlgoRequestDto;
import com.sutpc.transpaas.algoserver.dto.response.ModelAlgoResponse;
import com.sutpc.transpaas.algoserver.entity.ModelAlgo;
import com.sutpc.transpaas.algoserver.entity.ModelProject;
import com.sutpc.transpaas.algoserver.service.ModelAlgoService;
import com.sutpc.transpaas.algoserver.service.ModelProjectService;
import java.util.LinkedHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** 算法. */
@Service
@Slf4j
public class ModelAlgoServiceImpl extends ServiceImpl<ModelAlgoMapper, ModelAlgo>
    implements ModelAlgoService {

  @Autowired private ModelProjectService modelProjectService;
  @Autowired private ModelAlgoMapper modelAlgoMapper;

  /**
   * 查询当前步骤加载数据的逻辑.
   *
   * @param algoRequestDto 请求参数
   * @return 明细数据对象：ModelAlgoResponse
   */
  @Override
  public ModelAlgoResponse getStepStatus(AlgoRequestDto algoRequestDto) {

    String taskId = algoRequestDto.getTaskId();
    String algoCode = algoRequestDto.getAlgoNameEnum().getCode();
    String stepCode = algoRequestDto.getStepCode();

    ModelAlgoResponse response = new ModelAlgoResponse();
    response.setModelProjectId(taskId);
    response.setAlgoCode(algoRequestDto.getAlgoNameEnum());
    response.setStepCode(stepCode);
    response.setStepRelationStatus(0);

    ModelProject modelProject = this.modelProjectService.getById(taskId);
    if (null != modelProject) {
      String interactParam = modelProject.getInteractParameters();
      if (interactParam.contains(algoCode) && interactParam.contains(stepCode)) {
        LinkedHashMap<String, JSONObject> glbMap = JSON.parseObject(interactParam, LinkedHashMap.class);
        JSONObject jsonObject = glbMap.get(algoCode);
        if ("EXECUTE".equalsIgnoreCase(stepCode)) {
          stepCode = algoCode + "_" + stepCode;
        }
        int stepRelationStatus = Integer.parseInt(jsonObject.get(stepCode).toString());
        response.setStepRelationStatus(stepRelationStatus);
      }
    }
    return response;
  }

  /**
   * 根据模型ID与执行代码获取数据.
   *
   * @param taskId   模型ID
   * @param stepCode 执行代码
   * @return ModelAlgo
   */
  @Override
  public ModelAlgo getModelAlgo(String taskId, String stepCode) {
    QueryWrapper<ModelAlgo> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("model_project_id", taskId);
    queryWrapper.eq("step_code", stepCode);
    queryWrapper.eq("is_deleted", AlgoConstant.ZERO);
    return modelAlgoMapper.selectOne(queryWrapper);
  }

}
