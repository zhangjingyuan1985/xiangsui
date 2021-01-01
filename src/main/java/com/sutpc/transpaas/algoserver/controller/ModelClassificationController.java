package com.sutpc.transpaas.algoserver.controller;

import com.sutpc.transpaas.algoserver.dto.ModelClassificationDto;
import com.sutpc.transpaas.algoserver.service.ModelClassificationService;
import com.sutpc.transpaas.algoserver.utils.ResponseResult;
import com.sutpc.transpaas.algoserver.utils.valid.ValidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "交叉设置", tags = "5、分类交叉设置")
@RestController
@RequestMapping(value = "/modelClassification")
@Slf4j
public class ModelClassificationController {

  @Autowired
  private ModelClassificationService modelClassificationService;

  @ApiOperation(value = "基础分类信息查询", notes = "基础分类信息查询（查全部）")
  @RequestMapping(value = "selectAll", method = RequestMethod.GET)
  public ResponseResult selectAll() {
    return ResponseResult.ok(modelClassificationService.selectAll());
  }

  @ApiOperation(value = "模型出行目的地分类设置查询", notes = "根据模型ID，查询出行目的，返回Map结构数据")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "taskId", value = "模型ID", paramType = "query", dataType = "String", required = true),
      @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", dataType = "string", required = true)
  })
  @RequestMapping(value = "getByTaskId", method = RequestMethod.GET)
  public ResponseResult getByTaskId(@RequestParam String taskId, @RequestParam String userId) {

    return ResponseResult
        .ok(modelClassificationService.getByTaskId(taskId, userId));
  }

  @ApiOperation(value = "模型分类设置查询", notes = "查询当前模型的配置，如果当前模型没有配置，将返回有所标准配置信息")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "taskId", value = "模型ID", paramType = "query", dataType = "String", required = true),
      @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", dataType = "string", required = true)
  })
  @RequestMapping(value = "queryList", method = RequestMethod.GET)
  public ResponseResult queryList(@RequestParam String taskId, @RequestParam String userId) {

    return ResponseResult
        .ok(modelClassificationService.queryModelClassificationProject(taskId, userId));
  }

  @ApiOperation(value = "用户设置的单字段去重查询", notes = "去重查询")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "taskId", value = "模型ID", paramType = "query", dataType = "String", required = true),
      @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", dataType = "string", required = true),
      @ApiImplicitParam(
          name = "attribute",
          value = "字段名称(POP_TYPE,INCOME_TYPE,CAR_TYPE,PURPOSE_TYPE,CROSS_TYPE1,CROSS_TYPE2)",
          paramType = "query", dataType = "string", required = true)
  })
  @RequestMapping(value = "queryDistinctList", method = RequestMethod.GET)
  public ResponseResult queryDistinctList(@RequestParam String taskId, @RequestParam String userId,
      String attribute) {

    return ResponseResult.ok(modelClassificationService.queryDistinct(taskId, userId, attribute));
  }

  @ApiOperation(value = "存储用户对当前模型的交叉分类设置", notes = "存储用户对当前模型的交叉分类设置")
  @RequestMapping(value = "save", method = RequestMethod.POST)
  public ResponseResult save(@RequestBody ModelClassificationDto modelClassificationDto) {
    ValidUtil.checkRequestParams(modelClassificationDto);
    modelClassificationService.saveModelClassificationProject(modelClassificationDto);
    return ResponseResult.ok();
  }
}
