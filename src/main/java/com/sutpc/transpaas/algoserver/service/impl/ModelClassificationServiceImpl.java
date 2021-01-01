package com.sutpc.transpaas.algoserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sutpc.transpaas.algoserver.constant.AlgoConstant;
import com.sutpc.transpaas.algoserver.dao.ModelClassificationMapper;
import com.sutpc.transpaas.algoserver.dao.ModelClassificationProjectMapper;
import com.sutpc.transpaas.algoserver.dto.ModelClassificationDto;
import com.sutpc.transpaas.algoserver.dto.ModelClassificationProjectDto;
import com.sutpc.transpaas.algoserver.entity.ModelClassification;
import com.sutpc.transpaas.algoserver.entity.ModelClassificationProject;
import com.sutpc.transpaas.algoserver.entity.ModelConfig;
import com.sutpc.transpaas.algoserver.service.ModelClassificationService;
import com.sutpc.transpaas.algoserver.service.ModelConfigService;
import com.sutpc.transpaas.algoserver.service.ModelProjectService;
import com.sutpc.transpaas.algoserver.utils.Uuid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 交叉分类设置.
 */
@Service
@Slf4j
public class ModelClassificationServiceImpl extends
    ServiceImpl<ModelClassificationMapper, ModelClassification> implements
    ModelClassificationService {

  @Autowired
  private ModelClassificationMapper modelClassificationMapper;
  @Autowired
  private ModelClassificationProjectMapper modelClassificationProjectMapper;
  @Autowired
  private ModelConfigService modelConfigService;
  @Autowired
  private ModelProjectService modelProjectService;

  @Value("${client.standardTaskId}")
  private String standardTaskId;

  /**
   * 将所有设置数据返回.
   *
   * @return ModelClassification对象结合
   */
  @Override
  public List<ModelClassification> selectAll() {
    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper.eq("is_deleted", AlgoConstant.ZERO);
    return modelClassificationMapper.selectList(queryWrapper);
  }

  /**
   * 查询当前模型的配置，如果当前模型没有配置，将返回有所标准配置信息.
   *
   * @param taskId 模型ID
   * @param userId 用户ID
   * @return ModelClassificationProject集合
   */
  @Override
  public List<ModelClassificationProject> queryModelClassificationProject(String taskId,
      String userId) {

    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper.eq("is_deleted", AlgoConstant.ZERO);
    queryWrapper.eq("model_project_id", taskId);
    List<ModelClassificationProject> list = this.modelClassificationProjectMapper
        .selectList(queryWrapper);
    if (null == list || list.size() == 0){
      QueryWrapper queryStandWrapper = new QueryWrapper();
      queryStandWrapper.eq("is_deleted", AlgoConstant.ZERO);
      queryStandWrapper.eq("model_project_id", standardTaskId);
      list = this.modelClassificationProjectMapper.selectList(queryStandWrapper);
    }
    return list;
  }

  /**
   * 根据模型ID，查出行目的.
   *
   * @param taskId 模型ID
   * @param userId 用户ID
   * @return Map对象
   */
  @Override
  public Map<String, Object> getByTaskId(String taskId, String userId) {

    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper.eq("is_deleted", AlgoConstant.ZERO);
    queryWrapper.eq("model_project_id", taskId);
    List<ModelClassificationProject> list =
        this.modelClassificationProjectMapper.selectList(queryWrapper);
    if(null == list || list.size() == 0) {
      QueryWrapper queryWrapperStand = new QueryWrapper();
      queryWrapperStand.eq("is_deleted", AlgoConstant.ZERO);
      queryWrapperStand.eq("model_project_id", standardTaskId);
      list = this.modelClassificationProjectMapper.selectList(queryWrapperStand);
    }
    List<String> popJudgeList = list.stream().map(base -> base.getPopJudge())
        .distinct().filter(str -> str != null && StringUtils.isNotEmpty(str))
        .collect(Collectors.toList());
    Map<String, Object> resultMap = new HashMap<>();
    for (String popJudge : popJudgeList) {
      List<String> purposeTypeList = list.stream().filter(
          object -> object.getPopJudge().equals(popJudge)
                 && object.getPurposeType() != null
      ).map(base -> base.getPurposeType()).distinct().collect(Collectors.toList());
      resultMap.put(popJudge, purposeTypeList);
    }
    return resultMap;
  }

  /**
   * 字段去重查询.
   *
   * @param taskId 模型ID
   * @param userId 用户ID
   * @param attribute 字段参数
   * @return Map对象
   */
  @Override
  public LinkedHashMap<String, List<String>> queryDistinct(String taskId, String userId,
      String attribute) {
    LinkedHashMap<String, List<String>> resultMap = new LinkedHashMap<>();

    String[] args = attribute.split(",");
    List<String> attributes = new ArrayList<>();
    for (String arg : args) {
      attributes.add(arg);
    }
    QueryWrapper queryWrapper = new QueryWrapper<String>();
    queryWrapper.eq("model_project_id", taskId);
    queryWrapper.eq("value", "1");
    List<ModelClassificationProject> baseList = this.modelClassificationProjectMapper
        .selectList(queryWrapper);
    if (null ==baseList || baseList.size() == 0) {
      QueryWrapper queryWrapperBase = new QueryWrapper<String>();
      queryWrapperBase.eq("model_project_id", standardTaskId);
      queryWrapperBase.eq("value", "1");
      baseList = this.modelClassificationProjectMapper.selectList(queryWrapperBase);
    }
    if (attributes.contains("POP_TYPE")) {
      List<String> resultList = baseList.stream().map(base -> base.getPopType())
          .distinct().filter(str -> str != null && StringUtils.isNotEmpty(str))
          .collect(Collectors.toList());
      resultMap.put("POP_TYPE", resultList);
    }
    if (attributes.contains("INCOME_TYPE")) {
      List<String> resultList = baseList.stream().map(base -> base.getIncomeType())
          .distinct().filter(str -> str != null && StringUtils.isNotEmpty(str))
          .collect(Collectors.toList());
      resultMap.put("INCOME_TYPE", resultList);
    }
    if (attributes.contains("CAR_TYPE")) {
      List<String> resultList = baseList.stream().map(base -> base.getCarType())
          .distinct().filter(str -> str != null && StringUtils.isNotEmpty(str))
          .collect(Collectors.toList());
      resultMap.put("CAR_TYPE", resultList);
    }
    if (attributes.contains("PURPOSE_TYPE")) {
      List<String> resultList = baseList.stream().map(base -> base.getPurposeType())
          .distinct().filter(str -> str != null && StringUtils.isNotEmpty(str))
          .collect(Collectors.toList());
      resultMap.put("PURPOSE_TYPE", resultList);
    }
    if (attributes.contains("CROSS_TYPE1")) {
      List<String> resultList = baseList.stream().map(base -> base.getCrossType1())
          .distinct().filter(str -> str != null && StringUtils.isNotEmpty(str))
          .collect(Collectors.toList());
      resultMap.put("CROSS_TYPE1", resultList);
    }
    if (attributes.contains("CROSS_TYPE2")) {
      List<String> resultList = baseList.stream().map(base -> base.getCrossType2())
          .distinct().filter(str -> str != null && StringUtils.isNotEmpty(str))
          .collect(Collectors.toList());
      resultMap.put("CROSS_TYPE2", resultList);
    }
    return resultMap;
  }

  /**
   * 存储用户对当前模型的交叉分类设置.
   *
   * @param modelClassificationDto 请求参数对象
   */
  @Transactional
  @Override
  public void saveModelClassificationProject(ModelClassificationDto modelClassificationDto) {

    String taskId = modelClassificationDto.getTaskId();
    String userId = modelClassificationDto.getUserId();

    QueryWrapper deleteWrapper = new QueryWrapper();
    deleteWrapper.eq("model_project_id", taskId);
    this.modelClassificationProjectMapper.delete(deleteWrapper);

    List<ModelClassificationProjectDto> data = modelClassificationDto.getDataList();
    for (ModelClassificationProjectDto project : data) {
      ModelClassificationProject projectData = new ModelClassificationProject();
      BeanUtils.copyProperties(project, projectData);
      projectData.setId(Uuid.getSysUuid());
      projectData.setModelProjectId(taskId);
      projectData.setCreateBy(userId);
      this.modelClassificationProjectMapper.insert(projectData);
    }

    ModelConfig modelConfig =
        modelConfigService.getOneByKey(AlgoConstant.UPDATE_FLAG + "M02_01_01");
    if (null != modelConfig && StringUtils.isNoneBlank(modelConfig.getConfigValue())) {
      HashSet<String> stepCodeSet = new HashSet<>();
      String[] configValueArgs = modelConfig.getConfigValue().split(",");
      for (String configValue : configValueArgs) {
        stepCodeSet.add(configValue);
      }
      HashSet<String> zeroSet = new HashSet<>();
      zeroSet.add("M02_01_01");
      this.modelProjectService.changeInteractParameters(
          taskId,
          zeroSet,
          0);
      this.modelProjectService.changeInteractParameters(
          taskId,
          stepCodeSet,
          1);
    }
  }
}
