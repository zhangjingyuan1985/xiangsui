package com.sutpc.transpaas.algoserver.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sutpc.transpaas.algoserver.constant.AlgoConstant;
import com.sutpc.transpaas.algoserver.constant.AlgoNameEnum;
import com.sutpc.transpaas.algoserver.dao.ModelOmxMapper;
import com.sutpc.transpaas.algoserver.dto.OmxCallBackDto;
import com.sutpc.transpaas.algoserver.entity.ModelOmx;
import com.sutpc.transpaas.algoserver.service.ModelOmxService;
import com.sutpc.transpaas.algoserver.utils.RestTemplateUtils;
import com.sutpc.transpaas.algoserver.utils.Uuid;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * 矩阵文件内容读取.
 */
@Service
@Slf4j
@RefreshScope
public class ModelOmxServiceImpl
    extends ServiceImpl<ModelOmxMapper, ModelOmx>
    implements ModelOmxService {

  @Autowired
  private ModelOmxMapper modelOmxMapper;

  @Value("${client.omxUrl}")
  private String omxUrl;

  /**
   * 矩阵文件内容查询.
   *
   * @param modelOmx 查询对象
   * @return ModelOmx
   */
  @Override
  public ModelOmx getQueryResult(ModelOmx modelOmx) {
    Map<String, Object> paraMap = new HashMap<>();
    paraMap.put("taskId", modelOmx.getModelProjectId());
    paraMap.put("subTaskId", modelOmx.getAlgoApiCode());
    paraMap.put("fileName", modelOmx.getFileName());
    paraMap.put("matrixName", modelOmx.getMatrixName());
    paraMap.put("execType", modelOmx.getExecType());
    paraMap.put("location", modelOmx.getLocation());
    paraMap.put("aggregateIndex", modelOmx.getAggregateIndex());
    String token = Uuid.getSysUuid();
    paraMap.put("token", token);
    String url = "http://" + omxUrl + "/get_matrix_main";

    if("aggregate".equals(modelOmx.getExecType())) {
      //1、查询数据库是否有结果，如果有结果，直接返回结果
      QueryWrapper<ModelOmx> queryWrapper = new QueryWrapper();
      queryWrapper.eq("model_project_id", modelOmx.getModelProjectId());
      queryWrapper.eq("algo_api_code", modelOmx.getAlgoApiCode());
      queryWrapper.eq("file_name", modelOmx.getFileName());
      queryWrapper.eq("matrix_name", modelOmx.getMatrixName());
      queryWrapper.eq("exec_type", modelOmx.getExecType());
      queryWrapper.eq("location", modelOmx.getLocation());
      queryWrapper.eq("aggregate_index", modelOmx.getAggregateIndex());
      queryWrapper.eq("is_deleted", AlgoConstant.ZERO);
      ModelOmx modelOmxDb = this.modelOmxMapper.selectOne(queryWrapper);
      if (null == modelOmxDb) {
        LinkedHashMap<String, String> map = this.sendPost(url, paraMap);
        String responseCode = map.get("code");
        if ("500".equals(responseCode)) {
          modelOmx.setQueryStatus(9);
          modelOmx.setQueryResult(map.get("data"));
        }
        modelOmx.setId(token);
        this.modelOmxMapper.insert(modelOmx);
      } else {
        modelOmx.setQueryStatus(modelOmxDb.getQueryStatus());
        modelOmx.setQueryResult(modelOmxDb.getQueryResult());
      }
    } else {
      LinkedHashMap<String, String> map = this.sendPost(url, paraMap);
      String responseCode = map.get("code");
      if ("500".equals(responseCode)) {
        modelOmx.setQueryStatus(9);
        modelOmx.setQueryResult(map.get("data"));
      } else {
        modelOmx.setQueryStatus(1);
        modelOmx.setQueryResult(map.get("data"));
      }

    }
    return modelOmx;
  }

  /**
   * 更新查询结果.
   *
   * @param omxCallBackDto 回调参数
   */
  @Override
  public void updateCallback(OmxCallBackDto omxCallBackDto) {
    log.info("收到OMX回调请求，参数{}", JSON.toJSONString(omxCallBackDto));
    ModelOmx modelOmx = new ModelOmx();
    modelOmx.setId(omxCallBackDto.getToken());
    if ("false".equalsIgnoreCase(omxCallBackDto.getExecStatus())) {
      modelOmx.setQueryStatus(9);
    } else {
      modelOmx.setQueryStatus(1);
    }
    modelOmx.setQueryResult(omxCallBackDto.getData());
    this.modelOmxMapper.updateById(modelOmx);
  }

  /**
   * 算法执行后，清除历史存储结果.
   *
   * @param modelProjectId 模型ID
   * @param algoNameEnum   算法代码
   */
  @Override
  public void deleteModelOmx(String modelProjectId, AlgoNameEnum algoNameEnum) {
    UpdateWrapper<ModelOmx> updateWrapper = new UpdateWrapper();
    updateWrapper.eq("model_project_id", modelProjectId);
    updateWrapper.eq("algo_api_code", algoNameEnum.getApiCode());
    ModelOmx modelOmx = new ModelOmx();
    modelOmx.setIsDeleted(AlgoConstant.ONE);
    this.modelOmxMapper.update(modelOmx, updateWrapper);
  }

  /**
   * 算法接口远程调用.
   *
   * @param url 请求地址
   * @param parameterMap 请求参数
   * @return Map对象
   */
  private LinkedHashMap sendPost(String url, Map<String, Object> parameterMap) {
    ResponseEntity entity = RestTemplateUtils.getRestTemplate()
        .postForEntity(url, parameterMap, String.class);
    LinkedHashMap<String, String> map = JSON.parseObject(
        entity.getBody().toString(), LinkedHashMap.class);
    log.info("矩阵文件内容读取请求参数：url={},param={}.执行结果：{}",
        url, JSON.toJSONString(parameterMap), JSONUtils.toJSONString(map));
    return map;
  }

}
