package com.sutpc.transpaas.algoserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sutpc.transpaas.algoserver.entity.LineStopRela;
import java.util.List;


public interface LineStopRelaService extends IService<LineStopRela> {

  /**
   * 根据线路名查询线路站点信息.
   *
   * @param routeIndex 线路代码
   * @return List
   */
  public List<LineStopRela> queryListByRouteIndex(int routeIndex);

}
