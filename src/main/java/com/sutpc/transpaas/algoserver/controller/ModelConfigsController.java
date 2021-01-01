package com.sutpc.transpaas.algoserver.controller;

import com.sutpc.transpaas.algoserver.service.ModelConfigService;
import com.sutpc.transpaas.algoserver.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "参数配置", tags = "7、系统参数配置信息")
@RestController
@RequestMapping(value = "/modelConfigs")
@Slf4j
public class ModelConfigsController {

  @Autowired
  private ModelConfigService modelConfigService;


  @ApiOperation(value = "分组信息查",
      notes = "通过分组关键字查询配置信息（岗位分类：JOB_TYPE，岗位权重表：SV_FORMULA，线路类型：TRANSIT_MODE）")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "configGroup", value = "分组光健字", required = true)
  })
  @RequestMapping(value = "queryList", method = RequestMethod.GET)
  public ResponseResult queryList(@RequestParam String configGroup) {

    return ResponseResult.ok(modelConfigService.getConfigsByGroup(configGroup));
  }

  @ApiOperation(value = "查询所有分组信息",
      notes = "（岗位分类：JOB_TYPE，岗位权重表：SV_FORMULA，线路类型：TRANSIT_MODE）")

  @GetMapping(value = "getAllGroup")
  public ResponseResult getAllGroup() {

    return ResponseResult.ok(modelConfigService.getAllGroup());
  }

}
