package com.sutpc.transpaas.algoserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sutpc.transpaas.algoserver.constant.AlgoConstant;
import com.sutpc.transpaas.algoserver.constant.AlgoFileTypeEnum;
import com.sutpc.transpaas.algoserver.constant.AlgoRunStatus;
import com.sutpc.transpaas.algoserver.dto.AlgoRequestDto;
import com.sutpc.transpaas.algoserver.entity.ModelAlgo;
import com.sutpc.transpaas.algoserver.entity.ModelConfig;
import com.sutpc.transpaas.algoserver.service.AlgoParameterService;
import com.sutpc.transpaas.algoserver.service.ModelAlgoService;
import com.sutpc.transpaas.algoserver.service.ModelConfigService;
import com.sutpc.transpaas.algoserver.service.ModelProjectService;
import com.sutpc.transpaas.algoserver.utils.CSVUtil;
import com.sutpc.transpaas.algoserver.utils.FtpUtil;
import com.sutpc.transpaas.algoserver.utils.JsonFileUtil;
import com.sutpc.transpaas.algoserver.utils.ResponseResult;
import com.sutpc.transpaas.algoserver.utils.Uuid;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * 算法参数管理.
 */
@Service
@Slf4j
public class AlgoParameterServiceImpl implements AlgoParameterService {

  @Autowired
  private ModelProjectService modelProjectService;
  @Autowired
  private ModelAlgoService modelAlgoService;
  @Autowired
  private ModelConfigService modelConfigService;

  @Value("${client.modelInputFile}")
  private String modelInputFile;

  @Autowired
  private FtpUtil ftpUtil;

  /**
   * 获取算法参数.
   *
   * @param algoRequestDto 参数对象
   * @return 结果数据
   */
  @Override
  public ResponseResult getFileContent(AlgoRequestDto algoRequestDto) {
    //1、根据请求参数查询数据库中是否有参数内容，如果有直接将数据库中的参数返回， 如果没有就返回标准文件的内容
    QueryWrapper<ModelAlgo> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("model_project_id", algoRequestDto.getTaskId());
//    queryWrapper.eq("algo_code", algoRequestDto.getAlgoNameEnum().getCode());
    queryWrapper.eq("step_code", algoRequestDto.getStepCode());
    queryWrapper.eq("create_by", algoRequestDto.getUserId());
    ModelAlgo modelAlgo = this.modelAlgoService.getOne(queryWrapper);
    if (null != modelAlgo) {
      if (AlgoFileTypeEnum.JSON == algoRequestDto.getFileType()) {
        LinkedHashMap<String, String> map = JSON
            .parseObject(modelAlgo.getStepParametersContent(), LinkedHashMap.class);
        return ResponseResult.ok(map, "文件内容读取成功，来源为数据库");
      }
      if (AlgoFileTypeEnum.CSV == algoRequestDto.getFileType()) {
        List<LinkedHashMap> list = JSON
            .parseArray(modelAlgo.getStepParametersContent(), LinkedHashMap.class);
        return ResponseResult.ok(list, "文件内容读取成功，来源为数据库");
      }
    } else {
      switch (AlgoFileTypeEnum.getByCode(algoRequestDto.getFileType().getCode())) {
        case JSON:
          try {
            String fileName = algoRequestDto.getStepCode() + ".json";
            String filePath = modelInputFile + File.separator + fileName;
            String result = JsonFileUtil.readJsonFile(filePath);
            LinkedHashMap<String, String> map = JSON.parseObject(result, LinkedHashMap.class);
            return ResponseResult.ok(map, "文件内容读取成功，来源为文件");
          } catch (Exception e) {
            log.error("json文件读取失败", e);
            return ResponseResult.error("json文件读取失败");
          }
        case CSV:
          String fileName = algoRequestDto.getStepCode() + ".csv";
          String filePath = modelInputFile + File.separator + fileName;
          try {
            List<LinkedHashMap<String, String>> list = CSVUtil.readCSV(filePath);
            return ResponseResult.ok(list, "文件内容读取成功，来源为文件");
          } catch (Exception e) {
            log.error("csv文件读取失败{}", e);
            return ResponseResult.error("csv文件读取失败{}");
          }
        default:
          log.info("default");
      }
    }
    return ResponseResult.ok();
  }

  /**
   * 根据用户请求参数，将内容生成文件.
   *
   * @param algoRequestDto 参数对象
   * @return 结果
   */
  @Override
  public ResponseResult saveUserFile(AlgoRequestDto algoRequestDto) {
    if ("parameter".equalsIgnoreCase(algoRequestDto.getStepCode())) {
      String result = this.analyseParameterFile(algoRequestDto.getTaskId(), algoRequestDto.getFileContent());
      algoRequestDto.setFileContent(result);
    }
    //1、根据请求参数查询数据库中是否有本次参数配置记录，如果有，更新数据库记录， 如果没有将本次配置信息存储到数据库
    QueryWrapper<ModelAlgo> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("model_project_id", algoRequestDto.getTaskId());
//    queryWrapper.eq("algo_code", algoRequestDto.getAlgoNameEnum().getCode());
    queryWrapper.eq("step_code", algoRequestDto.getStepCode());
    queryWrapper.eq("create_by", algoRequestDto.getUserId());
    ModelAlgo modelAlgo = this.modelAlgoService.getOne(queryWrapper);
    if (null == modelAlgo) {
      modelAlgo = new ModelAlgo();
      modelAlgo.setId(Uuid.getSysUuid());
      modelAlgo.setModelProjectId(algoRequestDto.getTaskId());
      modelAlgo.setAlgoCode(algoRequestDto.getAlgoNameEnum());
      modelAlgo.setAlgoName(algoRequestDto.getAlgoNameEnum().getDesc());
      modelAlgo.setAlgoApiCode(algoRequestDto.getAlgoNameEnum().getApiCode());
      modelAlgo.setServerType(algoRequestDto.getAlgoNameEnum().getServerType());
      modelAlgo.setStepCode(algoRequestDto.getStepCode());
      modelAlgo.setStepParametersContent(algoRequestDto.getFileContent());
      modelAlgo.setAlgoRunStatus(AlgoRunStatus.INIT.getCode());
      modelAlgo.setCreateBy(algoRequestDto.getUserId());
      this.modelAlgoService.getBaseMapper().insert(modelAlgo);
    } else {
      modelAlgo.setStepParametersContent(algoRequestDto.getFileContent());
      modelAlgo.setUpdateTime(new Date());
      this.modelAlgoService.update(modelAlgo, queryWrapper);
    }
    // 更新交互状态值
    ModelConfig modelConfig =
        modelConfigService.getOneByKey(AlgoConstant.UPDATE_FLAG + modelAlgo.getStepCode());
    if (null != modelConfig && StringUtils.isNoneBlank(modelConfig.getConfigValue())) {
      HashSet<String> stepCodeSet = new HashSet<>();
      String[] configValueArgs = modelConfig.getConfigValue().split(",");
      for (String configValue : configValueArgs) {
        stepCodeSet.add(configValue);
      }
      HashSet<String> zeroSet = new HashSet<>();
      zeroSet.add(algoRequestDto.getStepCode());
      this.modelProjectService.changeInteractParameters(
          algoRequestDto.getTaskId(),
          zeroSet,
          0);
      this.modelProjectService.changeInteractParameters(
          algoRequestDto.getTaskId(),
          stepCodeSet,
          1);
    }

    this.saveFileContent(algoRequestDto.getUserId(), algoRequestDto.getTaskId(),
        algoRequestDto.getStepCode(), algoRequestDto.getFileType(), algoRequestDto.getFileContent());

    return ResponseResult.ok("数据文件已经存储");
  }

  /**
   * 解析全局参数，分析影响度，返回全新的全局参数.
   *
   * @param taskId           模型ID
   * @param requestParameter 请求的参数内容
   * @return 全新的全局参数
   */
  @Override
  public String analyseParameterFile(String taskId, String requestParameter) {
    String initParameter = "";
    ModelAlgo modelAlgo = this.modelAlgoService.getModelAlgo(taskId, "parameter");
    if (null != modelAlgo && StringUtils.isNoneBlank(modelAlgo.getStepParametersContent())) {
      initParameter = modelAlgo.getStepParametersContent();
    } else {
      ModelConfig modelConfig = this.modelConfigService.getOneByKey(AlgoConstant.MODEL_PARAMETER);
      initParameter = modelConfig.getConfigValue();
    }
    LinkedHashMap<String, JSONObject> initParaMap = JSON.parseObject(initParameter, LinkedHashMap.class);
    LinkedHashMap<String, JSONObject> requestParaMap = JSON.parseObject(requestParameter, LinkedHashMap.class);
    String[] nodeArgs = {"Demand", "Supply"};
    HashSet<String> stepCodeSet = new HashSet<>();
    for (String nodeStr : nodeArgs) {
      JSONObject initNode = initParaMap.get(nodeStr);
      JSONObject reqNode = requestParaMap.get(nodeStr);
      if (null == reqNode) {
        break;
      }
      Iterator iterator = reqNode.entrySet().iterator();
      while (iterator.hasNext()) {
        Map.Entry entry = (Map.Entry) iterator.next();
        initNode.put(entry.getKey()+"", entry.getValue());
        // 通过变更的节点，找出受影响的值，入HashSet
        String queryKey = AlgoConstant.UPDATE_FLAG + entry.getKey();
        ModelConfig modelConfig = this.modelConfigService.getOneByKey(queryKey);
        if (null != modelConfig && StringUtils.isNoneBlank(modelConfig.getConfigValue())) {
          String[] configValueArgs = modelConfig.getConfigValue().split(",");
          for (String configValue : configValueArgs) {
            stepCodeSet.add(configValue);
          }
        }
      }
      this.modelProjectService.changeInteractParameters(taskId, stepCodeSet, 1);
    }
    return JSON.toJSONString(initParaMap);
  }

  /**
   * 存储用户的算法参数文件.
   *
   * @param userId      用户ID
   * @param projectId   模型ID
   * @param fileName    文件名称
   * @param fileType    文件类型
   * @param fileContent 文件内容
   */
  private void saveFileContent(String userId, String projectId, String fileName,
      AlgoFileTypeEnum fileType, String fileContent) {
    String localPath = modelInputFile + File.separator + userId + File.separator + projectId;
    String ftpPath =
        File.separator + userId + File.separator + projectId + File.separator + "input";
    switch (AlgoFileTypeEnum.getByCode(fileType.getCode())) {
      case JSON:
        try {
          fileName = fileName + ".json";
          JsonFileUtil.createJsonFile(fileContent, fileName, localPath);
        } catch (Exception e) {
          log.error("json文件创建失败", e);
        }
        break;
      case CSV:
        try {
          fileName = fileName + ".csv";
          CSVUtil.createCSV(fileContent, localPath, fileName);
        } catch (Exception e) {
          log.error("csv文件创建失败", e);
        }
        break;
      default:
        log.info("default");
    }

    try {
      ftpUtil.upload(localPath, ftpPath, fileName);
    } catch (IOException e) {
      log.error("FTP 文件处理异常", e);
    }
  }

}
