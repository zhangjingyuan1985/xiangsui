package com.sutpc.transpaas.algoserver.controller;

import com.sutpc.transpaas.algoserver.dto.ServiceAbilityDto;
import com.sutpc.transpaas.algoserver.service.ServiceAbilityService;
import com.sutpc.transpaas.algoserver.utils.ResponseResult;
import com.sutpc.transpaas.algoserver.utils.valid.ValidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 目的地选择-服务能力生成.
 */
@Api(value = "用户完成SV设置后，前端触发", tags = "9、目的地选择-服务能力生成")
@RestController
@RequestMapping(value = "/serviceAbility")
public class ServiceAbilityController {

  @Autowired
  private ServiceAbilityService serviceAbilityService;

  @ApiOperation(value = "1、服务能力生成", notes = "用户完成SV设置后，前端触发")
  @RequestMapping(value = "generate", method = RequestMethod.POST)
  public ResponseResult generate(@RequestBody ServiceAbilityDto dto) {
    ValidUtil.checkRequestParams(dto);
    String taskId = dto.getTaskId();
    String userId = dto.getUserId();
    this.serviceAbilityService.generate(taskId, userId);
    return ResponseResult.ok();
  }

}
