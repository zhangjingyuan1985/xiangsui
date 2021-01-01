package com.sutpc.transpaas.algoserver.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sutpc.transpaas.algoserver.constant.AlgoConstant;
import com.sutpc.transpaas.algoserver.constant.AlgoFileTypeEnum;
import com.sutpc.transpaas.algoserver.constant.AlgoNameEnum;
import com.sutpc.transpaas.algoserver.constant.AlgoServerType;
import com.sutpc.transpaas.algoserver.dao.ModelProjectMapper;
import com.sutpc.transpaas.algoserver.dto.AlgoRequestDto;
import com.sutpc.transpaas.algoserver.dto.ModelProjectDeleteDto;
import com.sutpc.transpaas.algoserver.entity.ModelAlgoServer;
import com.sutpc.transpaas.algoserver.entity.ModelProject;
import com.sutpc.transpaas.algoserver.exception.BusinessException;
import com.sutpc.transpaas.algoserver.exception.ServiceException;
import com.sutpc.transpaas.algoserver.service.AlgoParameterService;
import com.sutpc.transpaas.algoserver.service.ModelAlgoServerService;
import com.sutpc.transpaas.algoserver.service.ModelConfigService;
import com.sutpc.transpaas.algoserver.service.ModelProjectService;
import com.sutpc.transpaas.algoserver.utils.FtpUtil;
import com.sutpc.transpaas.algoserver.utils.ResponseResult;
import com.sutpc.transpaas.algoserver.utils.Uuid;
import java.io.File;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/** 用户模型创建与查询. */
@Service
@Slf4j
public class ModelProjectServiceImpl extends ServiceImpl<ModelProjectMapper, ModelProject>
    implements ModelProjectService {

  @Autowired private ModelProjectMapper modelProjectMapper;
  @Autowired private ModelAlgoServerService modelAlgoServerService;
  @Autowired private AlgoParameterService algoParameterService;
  @Autowired private ModelConfigService modelConfigService;
  @Autowired private FtpUtil ftpUtil;

  @Value("${client.modelInputFile}")
  private String modelInputFile;

  @Value("${client.modelOutputFile}")
  private String modelOutputFile;

  /**
   * 查询模型列表.
   *
   * @param userId 用户ID
   * @return 结果数据
   */
  @Override
  public ResponseResult<List<ModelProject>> queryModelProject(String userId) {

    QueryWrapper<ModelProject> queryWrapper = new QueryWrapper<>();
    if (StringUtils.isNotBlank(userId)) {
      queryWrapper.eq("create_user", userId);
      queryWrapper.eq("is_deleted", 0);
    }
    List<ModelProject> list = this.modelProjectMapper.selectList(queryWrapper);
    return ResponseResult.ok(list);
  }

  /**
   * 创建模型数据.
   *
   * @param modelProject 模型对象
   * @return 结果对象
   */
  @Override
  public ResponseResult saveModelProject(ModelProject modelProject) {

    QueryWrapper<ModelProject> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("create_user", modelProject.getCreateUser());
    queryWrapper.eq("model_name", modelProject.getModelName());
    List<ModelProject> queryList = this.modelProjectMapper.selectList(queryWrapper);
    if (null != queryList && queryList.size() > 0) {
      return ResponseResult.error("模型名称" + modelProject.getModelName() + "已经存在");
    }

    String uuid = Uuid.getSysUuid();
    String userId = modelProject.getCreateUser();

    // 1、创建模型
    ModelAlgoServer server = this.modelAlgoServerService.getServer(AlgoServerType.BZ_PC);
    String interactParameters =
        this.modelConfigService.getOneByKey(AlgoConstant.INTERACT_PARAMETERS).getConfigValue();
    modelProject.setId(uuid);
    modelProject.setCreateUser(userId);
    modelProject.setCreateBy(userId);
    modelProject.setModelServerIp(server.getIpAddress());
    modelProject.setInteractParameters(interactParameters);
    this.modelProjectMapper.insert(modelProject);

    // 2、将模型全局参数生成文件，存储路径为：/用户ID/模型ID/parameter.json
    AlgoRequestDto algoRequestDto = new AlgoRequestDto();
    algoRequestDto.setUserId(userId);
    algoRequestDto.setTaskId(uuid);
    algoRequestDto.setStepCode("parameter");
    algoRequestDto.setAlgoNameEnum(AlgoNameEnum.INIT);
    algoRequestDto.setFileType(AlgoFileTypeEnum.JSON);
    algoRequestDto.setFileContent(modelProject.getModelParameters());
    this.algoParameterService.saveUserFile(algoRequestDto);
    // 异步线程处理文件初始化
    Thread thread = new Thread(() -> initProjectFile(uuid, userId));
    thread.start();
    modelProject.setModelParameters("");
    return ResponseResult.ok(modelProject, "模型创建成功");
  }

  /**
   * 初始化新建项目的标准输入、输出文件并上传FTP.
   *
   * @param taskId 模型ID
   * @param userId 用户ID
   */
  private void initProjectFile(String taskId, String userId) {
    String remotePath = File.separator + userId + File.separator + taskId + File.separator;
    String[] dirs = {"input", "output"};
    for (String dir : dirs) {
      String localPath = modelInputFile;
      if ("output".equalsIgnoreCase(dir)) {
        localPath = modelOutputFile;
      }
      File[] inputFiles = new File(localPath).listFiles();
      for (File file : inputFiles) {
        if (!file.isDirectory()) {
          String localFileName = file.getName();
          try {
            ftpUtil.upload(localPath, remotePath + dir + File.separator, localFileName);
          } catch (Exception e) {
            throw new ServiceException("404", "文件初始化失败");
          }
        }
      }
    }
  }

  /**
   * 模型删除.
   *
   * @param modelProjectDeleteDto 请求参数对象
   */
  @Override
  public void deleteModelProject(ModelProjectDeleteDto modelProjectDeleteDto) {
    ModelProject modelProject =
        this.modelProjectMapper.selectById(modelProjectDeleteDto.getTaskId());
    if (null != modelProject) {
      modelProject.setIsDeleted(1);
      modelProject.setUpdateBy(modelProjectDeleteDto.getCreateUser());
      modelProject.setUpdateTime(new Date());
    }
    QueryWrapper<ModelProject> queryWrapper = new QueryWrapper();
    queryWrapper.eq("id", modelProjectDeleteDto.getTaskId());
    this.modelProjectMapper.update(modelProject, queryWrapper);
  }

  /**
   * 修改交互节点的值.
   *
   * @param taskId 模型ID
   * @param stepCodeSet 步骤代码
   * @param paramValue 状态值（0/1）
   */
  @Override
  public void changeInteractParameters(String taskId, HashSet<String> stepCodeSet, int paramValue) {
    ModelProject modelProject = this.getById(taskId);
    if (null == modelProject) {
      throw new BusinessException("404", "模型ID=" + taskId + "没有数据");
    } else {
      // 整个json拿出来
      String interactParameters = modelProject.getInteractParameters();
      LinkedHashMap<String, JSONObject> glbMap =
          JSON.parseObject(interactParameters, LinkedHashMap.class);
      // 遍历整个json参数
      Iterator<?> iterator = glbMap.entrySet().iterator();
      while (iterator.hasNext()) {
        Map.Entry<String, JSONObject> entry = (Map.Entry<String, JSONObject>) iterator.next();
        String key = entry.getKey();
        // 通过key值（algoCode）将单个算法的节点拿出来，进行遍历和值覆盖
        JSONObject jsonObject = glbMap.get(key);
        for (String stepCode : stepCodeSet) {
          if (jsonObject.containsKey(stepCode)) {
            jsonObject.put(stepCode, paramValue);
          }
        }
        glbMap.put(key, jsonObject);
      }
      interactParameters = JSONObject.toJSONString(glbMap);
      modelProject.setInteractParameters(interactParameters);
      this.updateById(modelProject);
    }
  }
}
