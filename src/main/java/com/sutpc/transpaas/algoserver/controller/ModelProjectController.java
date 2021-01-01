package com.sutpc.transpaas.algoserver.controller;

import com.sutpc.transpaas.algoserver.dto.ModelProjectDeleteDto;
import com.sutpc.transpaas.algoserver.dto.ModelProjectDto;
import com.sutpc.transpaas.algoserver.entity.ModelProject;
import com.sutpc.transpaas.algoserver.service.ModelProjectService;
import com.sutpc.transpaas.algoserver.utils.ResponseResult;
import com.sutpc.transpaas.algoserver.utils.valid.ValidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "针对用户级别的创建与查询", tags = "1、模型创建与查询")
@RestController
@RequestMapping(value = "/modelProject")
@Slf4j
public class ModelProjectController {

  @Autowired
  private ModelProjectService modelProjectService;

  @ApiOperation(value = "1、模型-创建", notes = "创建模型接口")
  @RequestMapping(value = "saveModelProject", method = RequestMethod.POST)
  public ResponseResult saveModelProject(@RequestBody ModelProjectDto dto) {
    ValidUtil.checkRequestParams(dto);
    ModelProject modelProject = new ModelProject();
    modelProject.setModelName(dto.getModelName().trim());
    modelProject.setModelParameters(dto.getModelParameters());
    modelProject.setCreateUser(dto.getCreateUser());
    return this.modelProjectService.saveModelProject(modelProject);
  }

  @ApiOperation(value = "2、用户模型查询", notes = "根据用户ID，ProjectID，算法名、文件名称查询信息")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", dataType = "string", required = true)
  })
  @RequestMapping(value = "queryModelProject", method = RequestMethod.GET)
  public ResponseResult<List<ModelProject>> queryModelProject(@RequestParam String userId) {

    return this.modelProjectService.queryModelProject(userId);
  }

  @ApiOperation(value = "3、模型-删除", notes = "删除模型接口")
  @RequestMapping(value = "delete", method = RequestMethod.POST)
  public ResponseResult delete(@RequestBody ModelProjectDeleteDto dto) {
    ValidUtil.checkRequestParams(dto);
    this.modelProjectService.deleteModelProject(dto);
    return ResponseResult.ok();
  }

}
