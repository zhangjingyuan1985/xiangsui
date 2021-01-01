package com.sutpc.transpaas.algoserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sutpc.transpaas.algoserver.constant.AlgoConstant;
import com.sutpc.transpaas.algoserver.dao.ModelConfigMapper;
import com.sutpc.transpaas.algoserver.entity.ModelConfig;
import com.sutpc.transpaas.algoserver.service.ModelConfigService;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 系统参数配置.
 */
@Service
@Slf4j
public class ModelConfigServiceImpl
    extends ServiceImpl<ModelConfigMapper, ModelConfig>
    implements ModelConfigService {

  @Autowired
  private ModelConfigMapper modelConfigMapper;

  /**
   * 通过key值查value.
   *
   * @param key 配置的key
   * @return ModelConfig
   */
  @Override
  public ModelConfig getOneByKey(String key) {
    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper.eq("is_deleted", AlgoConstant.ZERO);
    queryWrapper.eq("config_key", key);
    return this.modelConfigMapper.selectOne(queryWrapper);
  }

  /**
   * 根据组名获取配置信息.
   *
   * @param configGroup 配置组
   * @return ModelConfigs集合
   */
  @Override
  public List<ModelConfig> getConfigsByGroup(String configGroup) {

    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper.eq("is_deleted", AlgoConstant.ZERO);
    queryWrapper.eq("config_group", configGroup);
    queryWrapper.orderByAsc("group_order");
    return this.modelConfigMapper.selectList(queryWrapper);
  }

  /**
   * 查询所有分组.
   *
   * @return List
   */
  @Override
  public List<String> getAllGroup() {
    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper.eq("is_deleted", AlgoConstant.ZERO);
    queryWrapper.select("DISTINCT config_group");
    List<ModelConfig> list = this.modelConfigMapper.selectList(queryWrapper);
    List<String> resultList = list.stream().map(base -> base.getConfigGroup())
        .collect(Collectors.toList());
    return resultList;
  }

}
