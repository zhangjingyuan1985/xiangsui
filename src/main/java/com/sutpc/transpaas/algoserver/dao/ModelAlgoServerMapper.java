package com.sutpc.transpaas.algoserver.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sutpc.transpaas.algoserver.constant.AlgoServerType;
import com.sutpc.transpaas.algoserver.entity.ModelAlgoServer;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ModelAlgoServerMapper extends BaseMapper<ModelAlgoServer> {

  /**
   * 根据所需的服务器类型，获取所有可用服务器IP及被使用次数，升序排列.
   *
   * @param serverType 服务器类型
   * @return List
   */
  public List<ModelAlgoServer> getModelIp(@Param("serverType") AlgoServerType serverType);

  /**
   * 查询 : 根据serverType查询服务器列表，分页显示
   *
   * @param page 分页组件
   * @param serverType 服务器类型
   * @return IPage
   */
  IPage<ModelAlgoServer> selectPageVo(Page<ModelAlgoServer> page, AlgoServerType serverType);

}
