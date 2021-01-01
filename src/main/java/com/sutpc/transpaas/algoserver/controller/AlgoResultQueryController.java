package com.sutpc.transpaas.algoserver.controller;

import com.sutpc.transpaas.algoserver.constant.AlgoNameEnum;
import com.sutpc.transpaas.algoserver.dto.AlgoResultDto;
import com.sutpc.transpaas.algoserver.dto.response.AlgoResultResponse;
import com.sutpc.transpaas.algoserver.service.AlgoRequestService;
import com.sutpc.transpaas.algoserver.utils.ResponseResult;
import com.sutpc.transpaas.algoserver.utils.valid.ValidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 算法执行结果查询.
 */
@Api(value = "算法执行结果查询", tags = "4、算法执行结果查询")
@RestController
@RequestMapping(value = "/algoResultQuery")
@Slf4j
public class AlgoResultQueryController {

  @Autowired
  private AlgoRequestService algoRequestService;

  /**
   * 根据用户ID，ProjectID，算法名查询执行结果.
   *
   * @param taskId 模型ID
   * @param algoNameEnum 算法名称
   * @param userId 用户ID
   * @param fileType 文件类型：CSV/OMX
   * @param fileName 算法输出文件的文件名称
   * @param matrixName 矩阵名称
   * @param execType 读行：row,读列：col,聚合：aggregate
   * @param location 矩阵的行数或者列数
   * @param aggregateIndex 聚合：小区ID对应中区
   * @return ResponseResult
   */
  @ApiOperation(value = "算法执行结果查询", notes = "根据用户ID，ProjectID，算法名查询执行结果")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "taskId", value = "模型ID", required = true),
      @ApiImplicitParam(name = "userId", value = "用户ID", required = true),
      @ApiImplicitParam(name = "algoNameEnum", value = "算法名称", required = true),
      @ApiImplicitParam(name = "fileType", value = "文件类型：CSV/OMX", required = true),
      @ApiImplicitParam(name = "fileName", value = "算法输出文件的文件名称", required = true),
      @ApiImplicitParam(name = "matrixName", value = "矩阵名称", required = false),
      @ApiImplicitParam(name = "execType", value = "读行：row,读列：col,聚合：aggregate", required = false),
      @ApiImplicitParam(name = "location", value = "矩阵的行数或者列数", dataType = "int", required = false),
      @ApiImplicitParam(name = "aggregateIndex", value = "聚合：小区ID对应中区", required = false)
  })
  @RequestMapping(value = "query", method = RequestMethod.GET)
  public ResponseResult<AlgoResultResponse> query(@RequestParam String taskId,
      @RequestParam AlgoNameEnum algoNameEnum,
      @RequestParam String userId,
      @RequestParam String fileType,
      @RequestParam String fileName,
      @RequestParam(required = false) String matrixName,
      @RequestParam(required = false) String execType,
      @RequestParam(required = false, defaultValue = "0") int location,
      @RequestParam(required = false) String aggregateIndex) {
    AlgoResultDto algoResultDto = new AlgoResultDto();
    algoResultDto.setTaskId(taskId);
    algoResultDto.setAlgoNameEnum(algoNameEnum);
    algoResultDto.setUserId(userId);
    algoResultDto.setFileType(fileType);
    algoResultDto.setFileName(fileName);
    algoResultDto.setMatrixName(matrixName);
    algoResultDto.setExecType(execType);
    algoResultDto.setLocation(location);
    algoResultDto.setAggregateIndex(aggregateIndex);
    ValidUtil.checkRequestParams(algoResultDto);
    return algoRequestService.algoResultQuery(algoResultDto);
  }

}
