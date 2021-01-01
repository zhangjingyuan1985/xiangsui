package com.sutpc.transpaas.algoserver.service;

import com.sutpc.transpaas.algoserver.dto.AlgoRequestDto;
import com.sutpc.transpaas.algoserver.utils.ResponseResult;

/**
 * 算法参数管理.
 */
public interface AlgoParameterService {

  /**
   * 获取算法参数.
   *
   * @param algoRequestDto 算法调用请求对象
   * @return 返回restFul数据
   */
  public ResponseResult getFileContent(AlgoRequestDto algoRequestDto);

  /**
   * 根据用户请求参数，将内容生成文件.
   *
   * @param algoRequestDto 算法调用请求对象
   * @return 返回restFul数据
   */
  public ResponseResult saveUserFile(AlgoRequestDto algoRequestDto);

  /**
   * 解析全局参数，分析影响度，返回全新的全局参数.
   *
   * @param taskId 模型ID
   * @param requestParameter 请求的参数内容
   * @return 全新的全局参数
   */
  public String analyseParameterFile(String taskId, String requestParameter);

}
