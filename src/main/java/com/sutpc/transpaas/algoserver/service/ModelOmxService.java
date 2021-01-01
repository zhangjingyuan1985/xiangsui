package com.sutpc.transpaas.algoserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sutpc.transpaas.algoserver.constant.AlgoNameEnum;
import com.sutpc.transpaas.algoserver.dto.OmxCallBackDto;
import com.sutpc.transpaas.algoserver.entity.ModelOmx;

/**
 * 矩阵文件内容读取与存储
 */
public interface ModelOmxService extends IService<ModelOmx> {

  /**
   * 矩阵文件内容查询.
   *
   * @param modelOmx 查询对象
   * @return ModelOmx
   */
  public ModelOmx getQueryResult(ModelOmx modelOmx);

  /**
   * 更新查询结果.
   *
   * @param omxCallBackDto 回调参数
   */
  public void updateCallback(OmxCallBackDto omxCallBackDto);

  /**
   * 算法执行后，清除历史存储结果.
   *
   * @param modelProjectId 模型ID
   * @param algoNameEnum 算法代码
   */
  public void deleteModelOmx(String modelProjectId, AlgoNameEnum algoNameEnum);

}
