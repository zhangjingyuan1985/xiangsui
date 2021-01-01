package com.sutpc.transpaas.algoserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sutpc.transpaas.algoserver.entity.LinkVolume;

/**
 * csv表数据入库消费发布者
 */
public interface LinkVolumeService extends IService<LinkVolume> {

  /**
   * 读取CSV文件的数据，并入库.
   *
   * @param fileName 文件名称全路径
   * @param modelProjectId 模型ID
   */
  public void saveCsv(String fileName, String modelProjectId);

}
