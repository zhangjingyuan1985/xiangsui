package com.sutpc.transpaas.algoserver.service;

/**
 * 服务能力生成.
 */
public interface ServiceAbilityService {

  /**
   * 服务能力生成.
   *
   * @param taskId 模型ID
   * @param userId 用户ID
   */
  public void generate(String taskId, String userId);

}
