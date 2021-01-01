package com.sutpc.transpaas.algoserver.service.impl;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.sutpc.transpaas.algoserver.constant.AlgoConstant;
import com.sutpc.transpaas.algoserver.constant.AlgoNameEnum;
import com.sutpc.transpaas.algoserver.constant.AlgoRunStatus;
import com.sutpc.transpaas.algoserver.constant.AlgoServerStatus;
import com.sutpc.transpaas.algoserver.dto.AlgoResultDto;
import com.sutpc.transpaas.algoserver.dto.CallBackDto;
import com.sutpc.transpaas.algoserver.dto.ExecAlgoDto;
import com.sutpc.transpaas.algoserver.dto.response.AlgoResultResponse;
import com.sutpc.transpaas.algoserver.entity.ModelAlgo;
import com.sutpc.transpaas.algoserver.entity.ModelAlgoServer;
import com.sutpc.transpaas.algoserver.entity.ModelOmx;
import com.sutpc.transpaas.algoserver.entity.ModelProject;
import com.sutpc.transpaas.algoserver.exception.BusinessException;
import com.sutpc.transpaas.algoserver.exception.ServiceException;
import com.sutpc.transpaas.algoserver.service.AlgoRequestService;
import com.sutpc.transpaas.algoserver.service.LinkTransitVolumeService;
import com.sutpc.transpaas.algoserver.service.LinkVolumeService;
import com.sutpc.transpaas.algoserver.service.ModelAlgoServerService;
import com.sutpc.transpaas.algoserver.service.ModelAlgoService;
import com.sutpc.transpaas.algoserver.service.ModelConfigService;
import com.sutpc.transpaas.algoserver.service.ModelOmxService;
import com.sutpc.transpaas.algoserver.service.ModelProjectService;
import com.sutpc.transpaas.algoserver.service.SegmentVolumeService;
import com.sutpc.transpaas.algoserver.service.ZoneSpeedBigService;
import com.sutpc.transpaas.algoserver.service.ZoneSpeedMidService;
import com.sutpc.transpaas.algoserver.utils.CSVUtil;
import com.sutpc.transpaas.algoserver.utils.FtpUtil;
import com.sutpc.transpaas.algoserver.utils.ResponseResult;
import com.sutpc.transpaas.algoserver.utils.RestTemplateUtils;
import com.sutpc.transpaas.algoserver.utils.Uuid;
import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/** 远程算法调用. */
@Service
@Slf4j
public class AlgoRequestServiceImpl implements AlgoRequestService {

  @Autowired private ModelProjectService modelProjectService;
  @Autowired private ModelAlgoService modelAlgoService;
  @Autowired private ModelAlgoServerService modelAlgoServerService;
  @Autowired private ModelOmxService modelOmxService;
  @Autowired private LinkVolumeService linkVolumeService;
  @Autowired private LinkTransitVolumeService linkTransitVolumeService;
  @Autowired private SegmentVolumeService segmentVolumeService;
  @Autowired private FtpUtil ftpUtil;
  @Autowired private ZoneSpeedMidService zoneSpeedMidService;
  @Autowired private ZoneSpeedBigService zoneSpeedBigService;
  @Autowired private ModelConfigService modelConfigService;

  @Value("${client.modelOutputFile}")
  private String modelOutputFile;

  @Value("${client.omxUrl}")
  private String omxUrl;

  /**
   * 根据资源情况，选择服务器调用算法.
   *
   * @param execAlgoDto 参数
   * @return 结果
   */
  @Override
  public ResponseResult chooseServiceRequestAlgo(ExecAlgoDto execAlgoDto) {

    // 查询当前模型下的当前算法是否已经有提交记录，如果有就更新记录重新发起调用，如果没有就新增记录发起调用
    QueryWrapper<ModelAlgo> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("model_project_id", execAlgoDto.getTaskId());
    queryWrapper.eq("algo_code", execAlgoDto.getAlgoNameEnum().getCode());
    queryWrapper.eq("step_code", AlgoConstant.ALGO_SETP_CODE);
    ModelAlgo modelAlgo = this.modelAlgoService.getOne(queryWrapper);
    boolean insertFlag = true;
    if (null == modelAlgo) {
      modelAlgo = new ModelAlgo();
      modelAlgo.setId(Uuid.getSysUuid());
      modelAlgo.setModelProjectId(execAlgoDto.getTaskId());
      modelAlgo.setAlgoName(execAlgoDto.getAlgoNameEnum().getDesc());
      modelAlgo.setAlgoCode(execAlgoDto.getAlgoNameEnum());
      modelAlgo.setAlgoApiCode(execAlgoDto.getAlgoNameEnum().getApiCode());
      modelAlgo.setStepCode(AlgoConstant.ALGO_SETP_CODE);
      modelAlgo.setServerType(execAlgoDto.getAlgoNameEnum().getServerType());
      modelAlgo.setAlgoRunStatus(AlgoRunStatus.RUNING.getCode());
      modelAlgo.setCreateBy(execAlgoDto.getUserId());
      insertFlag = true;
    } else {
      modelAlgo.setUpdateTime(new Date());
      modelAlgo.setAlgoRunStatus(AlgoRunStatus.RUNING.getCode());
      modelAlgo.setAlgoRunResultDesc("");
      queryWrapper.eq("id", modelAlgo.getId());
      insertFlag = false;
    }

    String msg = "算法成功调用，请等待执行结果";
    // 根据模型ID，查询出模型绑定的算法服务器
    ModelProject modelProject = this.modelProjectService.getById(execAlgoDto.getTaskId());
    ModelAlgoServer modelAlgoServer =
        this.modelAlgoServerService.lockServer(
            execAlgoDto.getAlgoNameEnum().getServerType(), modelProject.getModelServerIp());
    // 3、如果有资源可用，直接向算法服务器发post请求，如果没有资源就先把本次操作的内容记录下来，待资源空闲的时候再找资源启动任务
    try {
      if (null != modelAlgoServer) {
        // 封装算法调用的请求参数
        Map<String, Object> parameterMap = new HashMap<>();
        parameterMap.put("token", modelAlgo.getId());
        parameterMap.put("taskId", execAlgoDto.getTaskId());
        parameterMap.put("userId", execAlgoDto.getUserId());
        parameterMap.put("subTaskId", modelAlgo.getId());
        modelAlgo.setModelAlgoServerId(modelAlgoServer.getId());
        String url =
            "http://"
                + modelAlgoServer.getIpAddress()
                + ":"
                + execAlgoDto.getAlgoNameEnum().getPort()
                + "/"
                + execAlgoDto.getAlgoNameEnum().getApiCode();
        LinkedHashMap map = this.sendPost(url, parameterMap);
        if (!"200".equalsIgnoreCase(map.get("code") + "")) {
          throw new BusinessException(map.get("code") + "", map.get("msg") + "");
        }
      } else {
        modelAlgo.setAlgoRunStatus(AlgoRunStatus.HOLD.getCode());
        msg = AlgoRunStatus.HOLD.getDesc();
      }
    } catch (Exception e) {
      modelAlgo.setAlgoRunStatus(AlgoRunStatus.FAIL.getCode());
      modelAlgo.setAlgoRunResultDesc(e.getMessage());
      modelAlgo.setModelAlgoServerId("");
      this.modelAlgoServerService.unLockServer(modelAlgoServer.getId());
      msg = "算法调用成功";
      log.error("算法执行异常：未找到相应的Python算法服务：", e);
    } finally {
      if (insertFlag) {
        this.modelAlgoService.save(modelAlgo);
      } else {
        this.modelAlgoService.update(modelAlgo, queryWrapper);
      }
    }
    return ResponseResult.ok(modelAlgo, msg);
  }

  /** 通过调度器将执行失败与初始化状态的任务重新执行. */
  @Override
  public void startAgloTask() {
    // 1、从数据库中查询出状态为0和9状态的任务
    QueryWrapper<ModelAlgo> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("algo_run_status", AlgoRunStatus.INIT.getCode());
    queryWrapper.eq("step_code", AlgoConstant.ALGO_SETP_CODE);
    List<ModelAlgo> modelAlgoList = this.modelAlgoService.getBaseMapper().selectList(queryWrapper);
    log.info("待执行的任务清单：{}", JSON.toJSONString(modelAlgoList));
    // 2、查看服务器资源状态
    QueryWrapper<ModelAlgoServer> queryWrapperServer = new QueryWrapper<>();
    queryWrapperServer.eq("run_status", AlgoServerStatus.FREE.getCode());
    List<ModelAlgoServer> serverList =
        this.modelAlgoServerService.getBaseMapper().selectList(queryWrapperServer);
    log.info("负载率低的服务器清单：{}", JSON.toJSONString(serverList));
  }

  /**
   * 算法运行状态查询.
   *
   * @param execAlgoDto 参数
   * @return 结果
   */
  @Override
  public ResponseResult algoRunStatusQuery(ExecAlgoDto execAlgoDto) {
    QueryWrapper<ModelAlgo> queryWrapper = new QueryWrapper<>();
    queryWrapper.eq("model_project_id", execAlgoDto.getTaskId());
    queryWrapper.eq("algo_code", execAlgoDto.getAlgoNameEnum().getCode());
    queryWrapper.eq("step_code", AlgoConstant.ALGO_SETP_CODE);
    ModelAlgo modelAlgo = this.modelAlgoService.getOne(queryWrapper);
    Map<String, Object> resultMap = new HashMap<>();
    if (null != modelAlgo) {
      resultMap.put("taskId", modelAlgo.getModelProjectId());
      resultMap.put("algoNameEnum", execAlgoDto.getAlgoNameEnum().getCode());
      resultMap.put("algoRunStatus", modelAlgo.getAlgoRunStatus());
      if (AlgoRunStatus.FAIL.getCode() == modelAlgo.getAlgoRunStatus()) {
        resultMap.put("algoRunResultDesc", modelAlgo.getAlgoRunResultDesc());
      }
    } else {
      throw new BusinessException("未找到相应的算法");
    }
    return ResponseResult.ok(resultMap, "算法运行状态");
  }

  /**
   * 算法执行结果查询.
   *
   * @param algoResultDto 参数
   * @return 结果
   */
  @Override
  public ResponseResult<AlgoResultResponse> algoResultQuery(AlgoResultDto algoResultDto) {

    String remotePath =
        File.separator
            + algoResultDto.getUserId()
            + File.separator
            + algoResultDto.getTaskId()
            + File.separator
            + "output"
            + File.separator;
    String localPath = modelOutputFile + remotePath;

    AlgoResultResponse algoResultResponse = new AlgoResultResponse();
    if ("CSV".equals(algoResultDto.getFileType())) {
      String fileName = algoResultDto.getFileName() + ".csv";
      ftpUtil.downLoad(remotePath, localPath, fileName);
      List<LinkedHashMap<String, String>> list = CSVUtil.readCSV(localPath + fileName);
      algoResultResponse.setQueryStatus(1);
      algoResultResponse.setCsvResult(list);
    }
    if ("OMX".equals(algoResultDto.getFileType())) {
      ModelOmx modelOmx = new ModelOmx();
      modelOmx.setModelProjectId(algoResultDto.getTaskId());
      modelOmx.setAlgoApiCode(algoResultDto.getAlgoNameEnum().getApiCode());
      modelOmx.setFileName(algoResultDto.getFileName() + ".omx");
      modelOmx.setMatrixName(algoResultDto.getMatrixName());
      modelOmx.setExecType(algoResultDto.getExecType());
      modelOmx.setLocation(algoResultDto.getLocation());
      modelOmx.setAggregateIndex(algoResultDto.getAggregateIndex());
      ModelOmx modelOmxDb = modelOmxService.getQueryResult(modelOmx);
      algoResultResponse.setOmxResult(modelOmxDb.getQueryResult());
      algoResultResponse.setQueryStatus(modelOmxDb.getQueryStatus());
    }
    return ResponseResult.ok(algoResultResponse);
  }

  /**
   * 回调方法，更新算法执行状态.
   *
   * @param callBackDto 参数
   * @return 结果
   */
  @Override
  public ResponseResult updateCallback(CallBackDto callBackDto) {

    log.info("收到算法回调请求，参数{}", JSON.toJSONString(callBackDto));
    QueryWrapper<ModelAlgo> updateWrapper = new QueryWrapper<ModelAlgo>();
    updateWrapper.eq("model_project_id", callBackDto.getTaskId());
    updateWrapper.eq("id", callBackDto.getSubTaskId());
    ModelAlgo modelAlgo = this.modelAlgoService.getBaseMapper().selectOne(updateWrapper);
    if (null == modelAlgo) {
      throw new ServiceException("404", "未匹配到回调的算法：" + JSON.toJSONString(callBackDto));
    }
    AlgoRunStatus algoRunStatus =
        callBackDto.getExecStatus().equalsIgnoreCase("true")
            ? AlgoRunStatus.SUCCESS
            : AlgoRunStatus.FAIL;
    modelAlgo.setAlgoRunStatus(algoRunStatus.getCode());
    if (AlgoRunStatus.FAIL == algoRunStatus) {
      modelAlgo.setAlgoRunResultDesc(callBackDto.getMsg());
    } else {
      // 当回调的算法是交通分配时，需将算法输出的结果文件导入数据库（M08_R02_**.csv）
      if (AlgoNameEnum.TRAFFIC_ASSIGNMENT == modelAlgo.getAlgoCode()) {
        // 异步线程处理文件入库
        Thread thread =
            new Thread(
                () ->
                    saveTrafficAssignment(modelAlgo.getModelProjectId(), modelAlgo.getCreateBy()));
        thread.start();
      }
      // 当回调的算法是交通分配时，需将算法输出的结果文件导入数据库（M07_R02_**.csv）
      if (AlgoNameEnum.CAR_DIVISION == modelAlgo.getAlgoCode()) {
        // 异步线程处理文件入库
        Thread thread =
            new Thread(
                () -> saveCarDivision(modelAlgo.getModelProjectId(), modelAlgo.getCreateBy()));
        thread.start();
      }
      // 更新交互状态值
      HashSet<String> zeroSet = new HashSet<>();
      zeroSet.add(modelAlgo.getAlgoCode() + "_" + modelAlgo.getStepCode());
      this.modelProjectService.changeInteractParameters(
          modelAlgo.getModelProjectId(), zeroSet, 0);
    }
    modelAlgo.setUpdateTime(new Date());
    modelAlgo.setUpdateBy(AlgoConstant.SYSTEM_USER);
    this.modelAlgoService.update(modelAlgo, updateWrapper);
    this.modelAlgoServerService.unLockServer(modelAlgo.getModelAlgoServerId());
    this.modelOmxService.deleteModelOmx(callBackDto.getTaskId(), modelAlgo.getAlgoCode());
    return ResponseResult.ok(callBackDto, "回调成功，服务器资源已经释放");
  }

  /**
   * 数据入库M08.
   *
   * @param taskId 模型ID
   * @param userId 用户ID
   */
  private void saveTrafficAssignment(String taskId, String userId) {
    String remotePath =
        File.separator
            + userId
            + File.separator
            + taskId
            + File.separator
            + "output"
            + File.separator;
    String localPath = modelOutputFile + remotePath;

    String[] linkTransitVolumeArgs = {
      "M08_R02_AM.csv", "M08_R02_EN.csv", "M08_R02_MD.csv", "M08_R02_NT.csv", "M08_R02_PM.csv"
    };
    for (String linkTransitVolumeStr : linkTransitVolumeArgs) {
      String fileName = linkTransitVolumeStr;
      ftpUtil.downLoad(remotePath, localPath, fileName);
      fileName =
          modelOutputFile
              + File.separator
              + userId
              + File.separator
              + taskId
              + File.separator
              + "output"
              + File.separator
              + fileName;
      linkTransitVolumeService.saveCsv(fileName, taskId);
    }
    String[] segmentVolumeArgs = {
      "M08_R03_AM.csv", "M08_R03_EN.csv", "M08_R03_MD.csv", "M08_R03_NT.csv", "M08_R03_PM.csv"
    };
    for (String segmentVolumeStr : segmentVolumeArgs) {
      String fileName = segmentVolumeStr;
      ftpUtil.downLoad(remotePath, localPath, fileName);
      fileName =
          modelOutputFile
              + File.separator
              + userId
              + File.separator
              + taskId
              + File.separator
              + "output"
              + File.separator
              + fileName;
      segmentVolumeService.saveCsv(fileName, taskId);
    }
  }
  /**
   * 数据入库M07.
   *
   * @param taskId 模型ID
   * @param userId 用户ID
   */
  private void saveCarDivision(String taskId, String userId) {

    String remotePath =
        File.separator
            + userId
            + File.separator
            + taskId
            + File.separator
            + "output"
            + File.separator;
    String localPath = modelOutputFile + remotePath;
    String[] linkVolumeArgs = {
      "M07_R02_AM.csv", "M07_R02_EN.csv", "M07_R02_MD.csv", "M07_R02_NT.csv", "M07_R02_PM.csv"
    };
    for (String linkVolumeStr : linkVolumeArgs) {
      String fileName = linkVolumeStr;
      ftpUtil.downLoad(remotePath, localPath, fileName);
      fileName =
          modelOutputFile
              + File.separator
              + userId
              + File.separator
              + taskId
              + File.separator
              + "output"
              + File.separator
              + fileName;
      linkVolumeService.saveCsv(fileName, taskId);
    }

    String[] zoneSpeedMidArgs = {
      "M07_R03_AM.csv", "M07_R03_EN.csv", "M07_R03_MD.csv", "M07_R03_NT.csv", "M07_R03_PM.csv"
    };
    for (String zoneSpeedMidStr : zoneSpeedMidArgs) {
      String fileName = zoneSpeedMidStr;
      ftpUtil.downLoad(remotePath, localPath, fileName);
      fileName =
          modelOutputFile
              + File.separator
              + userId
              + File.separator
              + taskId
              + File.separator
              + "output"
              + File.separator
              + fileName;
      zoneSpeedMidService.saveCsv(fileName, taskId);
    }

    String[] zoneSpeedBigArgs = {
      "M07_R04_AM.csv", "M07_R04_EN.csv", "M07_R04_MD.csv", "M07_R04_NT.csv", "M07_R04_PM.csv"
    };
    for (String zoneSpeedBigStr : zoneSpeedBigArgs) {
      String fileName = zoneSpeedBigStr;
      ftpUtil.downLoad(remotePath, localPath, fileName);
      fileName =
          modelOutputFile
              + File.separator
              + userId
              + File.separator
              + taskId
              + File.separator
              + "output"
              + File.separator
              + fileName;
      zoneSpeedBigService.saveCsv(fileName, taskId);
    }
  }

  /**
   * 算法接口远程调用.
   *
   * @param url 请求地址
   * @param parameterMap 请求参数
   * @return Map对象
   */
  private LinkedHashMap sendPost(String url, Map<String, Object> parameterMap) {
    ResponseEntity entity =
        RestTemplateUtils.getRestTemplate().postForEntity(url, parameterMap, String.class);
    LinkedHashMap<String, String> map =
        JSON.parseObject(entity.getBody().toString(), LinkedHashMap.class);
    log.info(
        "调用远程算法的请求参数：url={},param={}.执行结果：{}",
        url,
        JSON.toJSONString(parameterMap),
        JSONUtils.toJSONString(map));
    return map;
  }

  /**
   * 测试方法.
   *
   * @param args 参数
   */
  public static void main(String[] args) {

    Map<String, Object> paraMap = new HashMap<>();
    paraMap.put("taskId", "b4c6eaacdfa04a038805c35cbf24ccc6");
    paraMap.put("subTaskId", "destination_choice_main");
    paraMap.put("fileName", "M04_R01.omx");
    paraMap.put("matrixName", "Wuhan_HI_CA_HWH");
    paraMap.put("execType", "row");
    paraMap.put("location", 1);

    AlgoRequestServiceImpl o = new AlgoRequestServiceImpl();
    String url = "http://10.10.50.240:8090/get_matrix_main";
    o.sendPost(url, paraMap);
  }
}
