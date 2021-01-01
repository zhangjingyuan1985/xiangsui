package com.sutpc.transpaas.algoserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sutpc.transpaas.algoserver.dto.ModelClassificationDto;
import com.sutpc.transpaas.algoserver.entity.ModelClassification;
import com.sutpc.transpaas.algoserver.entity.ModelClassificationProject;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 出行分类基础表.
 */
public interface ModelClassificationService extends IService<ModelClassification> {

  /**
   * 将所有设置数据返回.
   *
   * @return list对象
   */
  List<ModelClassification> selectAll();

  /**
   * 查询当前模型的配置，如果当前模型没有配置，将返回有所标准配置信息.
   *
   * @param taskId 模型ID
   * @param userId 用户ID
   * @return List对象
   */
  List<ModelClassificationProject> queryModelClassificationProject(String taskId, String userId);

  /**
   * 根据模型ID，查出行目的.
   *
   * @param taskId 模型ID
   * @param userId 用户ID
   * @return Map对象
   */
  Map<String, Object> getByTaskId(String taskId, String userId);

  /**
   * 字段去重查询.
   *
   * @param taskId 模型ID
   * @param userId 用户ID
   * @param attribute 字段名称
   * @return LinkedHashMap对象
   */
  LinkedHashMap<String, List<String>> queryDistinct(String taskId, String userId, String attribute);

  /**
   * 存储用户对当前模型的交叉分类设置.
   *
   * @param modelClassificationDto 请求参数对象
   */
  void saveModelClassificationProject(ModelClassificationDto modelClassificationDto);

}
