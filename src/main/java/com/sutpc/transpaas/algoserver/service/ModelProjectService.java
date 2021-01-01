package com.sutpc.transpaas.algoserver.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.sutpc.transpaas.algoserver.dto.ModelProjectDeleteDto;
import com.sutpc.transpaas.algoserver.entity.ModelProject;
import com.sutpc.transpaas.algoserver.utils.ResponseResult;
import java.util.HashSet;
import java.util.List;

/**
 * 模型管理接口.
 */
public interface ModelProjectService extends IService<ModelProject> {

  /**
   * 查询模型列表.
   *
   * @param userId 用户ID
   * @return List集合
   */
  public ResponseResult<List<ModelProject>> queryModelProject(String userId);

  /**
   * 模型创建.
   *
   * @param modelProject 模型对象
   * @return 状态
   */
  public ResponseResult saveModelProject(ModelProject modelProject);

  /**
   * 模型删除.
   *
   * @param modelProjectDeleteDto 请求参数对象
   */
  public void deleteModelProject(ModelProjectDeleteDto modelProjectDeleteDto);

  /**
   * 修改交互节点的值.
   *
   * @param taskId 模型ID
   * @param stepCodeSet 步骤代码
   * @param paramValue 状态值（0/1）
   */
  public void changeInteractParameters(String taskId, HashSet<String> stepCodeSet, int paramValue);

}
