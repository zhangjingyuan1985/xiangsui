package com.sutpc.transpaas.algoserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sutpc.transpaas.algoserver.dto.AlgoRequestDto;
import com.sutpc.transpaas.algoserver.dto.response.ModelAlgoResponse;
import com.sutpc.transpaas.algoserver.entity.ModelAlgo;

/**
 * 模型算法执行.
 */
public interface ModelAlgoService extends IService<ModelAlgo> {

  /**
   * 查询当前步骤加载数据的逻辑.
   *
   * @param algoRequestDto 请求参数
   * @return 明细数据对象：ModelAlgoResponse
   */
  public ModelAlgoResponse getStepStatus(AlgoRequestDto algoRequestDto);

  /**
   * 根据模型ID与执行代码获取数据.
   *
   * @param taskId 模型ID
   * @param stepCode 执行代码
   * @return ModelAlgo
   */
  public ModelAlgo getModelAlgo(String taskId, String stepCode);

}
