package com.sutpc.transpaas.algoserver.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sutpc.transpaas.algoserver.constant.AlgoServerType;
import com.sutpc.transpaas.algoserver.entity.ModelAlgoServer;
import java.util.List;

/**
 * 算法服务器数据.
 */
public interface ModelAlgoServerService extends IService<ModelAlgoServer> {

  /**
   * 分页查询.
   *
   * @param page 分页对象
   * @param algoServerType 服务器类型
   * @return IPage
   */
  public IPage<ModelAlgoServer> selectPage(Page<ModelAlgoServer> page, AlgoServerType algoServerType);

  /**
   * 查询资源使用情况.
   *
   * @param algoServerType 服务器类型
   * @return List对象
   */
  public List<ModelAlgoServer> queryServerList(AlgoServerType algoServerType);

  /**
   * 通过服务器类型，返回随机IP.
   *
   * @return 服务器数据对象
   */
  public ModelAlgoServer getServer(AlgoServerType algoServerType);

  /**
   * 根据服务器类型，查询可用服务器资源.
   *
   * @param algoServerType 服务器类型
   * @param ipAddress ip地址
   * @return 服务器对象
   */
  public ModelAlgoServer lockServer(AlgoServerType algoServerType, String ipAddress);

  /**
   * 解锁资源 .
   *
   * @param id 服务器数据ID
   */
  public void unLockServer(String id);

  /**
   * 释放所有资源.
   */
  public void unLockAll();

}
