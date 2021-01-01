package com.sutpc.transpaas.algoserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sutpc.transpaas.algoserver.constant.AlgoConstant;
import com.sutpc.transpaas.algoserver.constant.AlgoServerStatus;
import com.sutpc.transpaas.algoserver.constant.AlgoServerType;
import com.sutpc.transpaas.algoserver.constant.ReturnCodeEnum;
import com.sutpc.transpaas.algoserver.dao.ModelAlgoServerMapper;
import com.sutpc.transpaas.algoserver.entity.ModelAlgoServer;
import com.sutpc.transpaas.algoserver.exception.BusinessException;
import com.sutpc.transpaas.algoserver.service.ModelAlgoServerService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 算法服务器资源.
 *
 * @author yangx
 */
@Slf4j
@Service
public class ModelAlgoServerServiceImpl extends ServiceImpl<ModelAlgoServerMapper, ModelAlgoServer>
    implements ModelAlgoServerService {

  @Autowired
  private ModelAlgoServerMapper modelAlgoServerMapper;

  /**
   * 分页查询.
   *
   * @param page           分页对象
   * @param algoServerType 服务器类型
   * @return IPage
   */
  @Override
  public IPage<ModelAlgoServer> selectPage(Page<ModelAlgoServer> page,
      AlgoServerType algoServerType) {

    return modelAlgoServerMapper.selectPageVo(page, algoServerType);
  }

  /**
   * 查询服务器数据集合.
   *
   * @param algoServerType 服务器类型
   * @return ModelAlgoServer对象集合
   */
  @Override
  public List<ModelAlgoServer> queryServerList(AlgoServerType algoServerType) {
    QueryWrapper queryWrapper = new QueryWrapper();
    queryWrapper.eq("is_deleted", AlgoConstant.ZERO);
    queryWrapper.eq("run_status", AlgoServerStatus.FREE.getCode());
    queryWrapper.eq("server_type", algoServerType.getCode());
    return modelAlgoServerMapper.selectList(queryWrapper);
  }

  /**
   * 通过服务器类型，返回随机IP.
   *
   * @param algoServerType 服务器类型
   * @return 服务器数据对象
   */
  @Override
  public ModelAlgoServer getServer(AlgoServerType algoServerType) {

    List<ModelAlgoServer> serverList = this.modelAlgoServerMapper.getModelIp(algoServerType);
    ModelAlgoServer modelAlgoServer = null;
    if (null == serverList | serverList.size() == 0) {
      log.info("系统未配置算法服务器IP");
      throw new BusinessException(ReturnCodeEnum.FAILED, "系统未配置算法服务器IP");
    } else {
      modelAlgoServer = serverList.get(0);
    }
    return modelAlgoServer;
  }

  /**
   * 根据服务器类型，查询可用服务器资源，并且锁定指定资源.
   *
   * @param algoServerType 服务器类型
   * @param ipAddress ip地址
   * @return 数据对象
   */
  @Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
  @Override
  public ModelAlgoServer lockServer(AlgoServerType algoServerType, String ipAddress) {

    QueryWrapper<ModelAlgoServer> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("is_deleted", AlgoConstant.ZERO);
    queryWrapper.eq("run_status", AlgoServerStatus.FREE.getCode());
    queryWrapper.eq("server_type", algoServerType.getCode());
    if (AlgoServerType.BZ_PC == algoServerType) {
      queryWrapper.eq("ip_address", ipAddress);
    }
    List<ModelAlgoServer> serverList = this.modelAlgoServerMapper.selectList(queryWrapper);
    ModelAlgoServer modelAlgoServer = null;
    if (null != serverList && serverList.size() > 0) {
      modelAlgoServer = serverList.get(0);
      //将选择资源进行锁定
      modelAlgoServer.setRunStatus(AlgoServerStatus.FULL.getCode());
      queryWrapper.eq("id", modelAlgoServer.getId());
      this.modelAlgoServerMapper.update(modelAlgoServer, queryWrapper);
    }
    return modelAlgoServer;
  }

  /**
   * 解锁资源.
   *
   * @param id 服务器数据ID
   */
  @Override
  public void unLockServer(String id) {
    ModelAlgoServer updateModelAlgoServer = new ModelAlgoServer();
    updateModelAlgoServer.setId(id);
    updateModelAlgoServer.setRunStatus(AlgoServerStatus.FREE.getCode());
    this.modelAlgoServerMapper.updateById(updateModelAlgoServer);
  }

  /**
   * 释放所有资源.
   */
  @Override
  public void unLockAll() {
    UpdateWrapper updateWrapper = new UpdateWrapper();
    updateWrapper.eq("is_deleted", AlgoConstant.ZERO);
    ModelAlgoServer updateModelAlgoServer = new ModelAlgoServer();
    updateModelAlgoServer.setRunStatus(AlgoServerStatus.FREE.getCode());
    updateModelAlgoServer.setRunStatus(AlgoServerStatus.FREE.getCode());
    this.modelAlgoServerMapper.update(updateModelAlgoServer, updateWrapper);
  }
}
