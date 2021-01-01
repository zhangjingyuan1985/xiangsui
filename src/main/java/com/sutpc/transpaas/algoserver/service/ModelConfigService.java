package com.sutpc.transpaas.algoserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sutpc.transpaas.algoserver.entity.ModelConfig;
import java.util.List;

/**
 * 系统参数配置.
 */
public interface ModelConfigService extends IService<ModelConfig> {

  /**
   * 通过key值查value.
   *
   * @param key 配置的key
   * @return ModelConfig
   */
  public ModelConfig getOneByKey(String key);

  /**
   * 根据组名获取配置信息.
   *
   * @param configGroup 配置组
   * @return List集合
   */
  public List<ModelConfig> getConfigsByGroup(String configGroup);

  /**
   * 查询所有分组.
   *
   * @return List
   */
  public List<String> getAllGroup();
}
