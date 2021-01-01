package com.sutpc.transpaas.algoserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sutpc.transpaas.algoserver.dao.LineStopRelaMapper;
import com.sutpc.transpaas.algoserver.entity.LineStopRela;
import com.sutpc.transpaas.algoserver.service.LineStopRelaService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class LineStopRelaServiceImpl
    extends ServiceImpl<LineStopRelaMapper, LineStopRela>
    implements LineStopRelaService {

  @Autowired
  private LineStopRelaMapper lineStopRelaMapper;

  /**
   * 根据线路名查询线路站点信息.
   *
   * @param routeIndex 线路代码
   * @return List
   */
  @Override
  public List<LineStopRela> queryListByRouteIndex(int routeIndex) {
    QueryWrapper<LineStopRela> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("route_index", routeIndex);
    return lineStopRelaMapper.selectList(queryWrapper);
  }

}
