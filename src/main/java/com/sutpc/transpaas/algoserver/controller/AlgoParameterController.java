package com.sutpc.transpaas.algoserver.controller;

import com.sutpc.transpaas.algoserver.constant.AlgoFileTypeEnum;
import com.sutpc.transpaas.algoserver.constant.AlgoNameEnum;
import com.sutpc.transpaas.algoserver.dto.AlgoRequestDto;
import com.sutpc.transpaas.algoserver.dto.ExecAlgoDto;
import com.sutpc.transpaas.algoserver.dto.response.ModelAlgoResponse;
import com.sutpc.transpaas.algoserver.service.AlgoParameterService;
import com.sutpc.transpaas.algoserver.service.AlgoRequestService;
import com.sutpc.transpaas.algoserver.service.ModelAlgoService;
import com.sutpc.transpaas.algoserver.utils.ResponseResult;
import com.sutpc.transpaas.algoserver.utils.valid.ValidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "算法参数采用JSON格式传输，通过服务端调用算法", tags = "2、算法参数配置与算法执行")
@RestController
@RequestMapping(value = "/algoParameter")
@Slf4j
@Validated
public class AlgoParameterController {

  @Autowired
  private AlgoRequestService algoRequestService;
  @Autowired
  private AlgoParameterService algoParameterService;
  @Autowired
  private ModelAlgoService modelAlgoService;

  @ApiOperation(value = "步骤数据加载方式", notes = "根据stepRelationStatus字段值得到加载方式")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "taskId", value = "模型ID", paramType = "query", dataType = "String", required = true),
      @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", dataType = "string", required = true),
      @ApiImplicitParam(name = "stepCode", value = "算法参数配置步骤代码/文件名称", paramType = "query", dataType = "string", required = true),
      @ApiImplicitParam(name = "fileType", value = "文件类型", paramType = "query", dataType = "string", required = true),
      @ApiImplicitParam(name = "algoNameEnum", value = "算法名称", paramType = "query", dataType = "string", required = true)
  })
  @RequestMapping(value = "getStepStatus", method = RequestMethod.GET)
  public ResponseResult<ModelAlgoResponse> getStepStatus(@RequestParam String taskId,
      @RequestParam String userId,
      @RequestParam AlgoNameEnum algoNameEnum,
      @RequestParam String stepCode,
      @RequestParam AlgoFileTypeEnum fileType
  ) {
    AlgoRequestDto algoRequestDto = new AlgoRequestDto();
    algoRequestDto.setTaskId(taskId);
    algoRequestDto.setUserId(userId);
    algoRequestDto.setStepCode(stepCode);
    algoRequestDto.setFileType(fileType);
    algoRequestDto.setAlgoNameEnum(algoNameEnum);
    ValidUtil.checkRequestParams(algoRequestDto);
    return ResponseResult.ok(this.modelAlgoService.getStepStatus(algoRequestDto));
  }

  @ApiOperation(value = "算法文件读取", notes = "根据用户ID，ProjectID，算法名、文件名称查询信息")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "taskId", value = "模型ID", paramType = "query", dataType = "String", required = true),
      @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", dataType = "string", required = true),
      @ApiImplicitParam(name = "stepCode", value = "算法参数配置步骤代码/文件名称", paramType = "query", dataType = "string", required = true),
      @ApiImplicitParam(name = "fileType", value = "文件类型", paramType = "query", dataType = "string", required = true),
      @ApiImplicitParam(name = "algoNameEnum", value = "算法名称", paramType = "query", dataType = "string", required = true)
  })
  @RequestMapping(value = "readUserFile", method = RequestMethod.GET)
  public ResponseResult readUserFile(@RequestParam String taskId,
      @RequestParam String userId,
      @RequestParam AlgoNameEnum algoNameEnum,
      @RequestParam String stepCode,
      @RequestParam AlgoFileTypeEnum fileType
  ) {
    AlgoRequestDto algoRequestDto = new AlgoRequestDto();
    algoRequestDto.setTaskId(taskId);
    algoRequestDto.setUserId(userId);
    algoRequestDto.setStepCode(stepCode);
    algoRequestDto.setFileType(fileType);
    algoRequestDto.setAlgoNameEnum(algoNameEnum);
    ValidUtil.checkRequestParams(algoRequestDto);
    return this.algoParameterService.getFileContent(algoRequestDto);
  }

  @ApiOperation(value = "用户参数文件存储", notes = "用户模型算法的参数转化为文件")
  @RequestMapping(value = "saveUserFile", method = RequestMethod.POST)
  public ResponseResult saveUserFile(@RequestBody AlgoRequestDto algoRequestDto) {
    ValidUtil.checkRequestParams(algoRequestDto);
    return this.algoParameterService.saveUserFile(algoRequestDto);
  }

  @ApiOperation(value = "算法调用", notes = "服务根据算法任务ID，选择合适的算法服务器执行算法")
  @RequestMapping(value = "algoRequest", method = RequestMethod.POST)
  public ResponseResult algoRequest(@RequestBody ExecAlgoDto execAlgoDto) {
    ValidUtil.checkRequestParams(execAlgoDto);
    return this.algoRequestService.chooseServiceRequestAlgo(execAlgoDto);
  }

  @ApiOperation(value = "算法运行状态查询", notes = "根据请求参数查询算法运行状态")
  @RequestMapping(value = "algoRunStatusQuery", method = RequestMethod.GET)
  public ResponseResult algoRunStatusQuery(@RequestParam String taskId,
      @RequestParam String userId,
      @RequestParam AlgoNameEnum algoNameEnum) {
    ExecAlgoDto execAlgoDto = new ExecAlgoDto();
    execAlgoDto.setTaskId(taskId);
    execAlgoDto.setAlgoNameEnum(algoNameEnum);
    execAlgoDto.setUserId(userId);
    return this.algoRequestService.algoRunStatusQuery(execAlgoDto);
  }

}
