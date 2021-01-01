package com.sutpc.transpaas.algoserver.service;

import com.sutpc.transpaas.algoserver.dto.AlgoResultDto;
import com.sutpc.transpaas.algoserver.dto.CallBackDto;
import com.sutpc.transpaas.algoserver.dto.ExecAlgoDto;
import com.sutpc.transpaas.algoserver.dto.response.AlgoResultResponse;
import com.sutpc.transpaas.algoserver.utils.ResponseResult;

/**
 * 远程算法调用.
 */
public interface AlgoRequestService {

  /**
   * 根据资源情况，选择服务器调用算法.
   *
   * @param execAlgoDto 算法执行请求对象
   * @return 返回restFul数据
   */
  public ResponseResult chooseServiceRequestAlgo(ExecAlgoDto execAlgoDto);

  /**
   * 通过调度器将执行失败与初始化状态的任务重新执行.
   */
  public void startAgloTask();

  /**
   * 算法运行状态查询.
   *
   * @param execAlgoDto 算法执行请求对象
   * @return 返回restFul数据
   */
  public ResponseResult algoRunStatusQuery(ExecAlgoDto execAlgoDto);

  /**
   * 算法执行结果查询.
   *
   * @param algoResultDto 算法结果查询请求对象
   * @return 返回restFul数据
   */
  public ResponseResult<AlgoResultResponse> algoResultQuery(AlgoResultDto algoResultDto);

  /**
   * 回调方法，更新算法执行状态.
   *
   * @param callBackDto 回调请求对象
   * @return 返回restFul数据
   */
  public ResponseResult updateCallback(CallBackDto callBackDto);

}
