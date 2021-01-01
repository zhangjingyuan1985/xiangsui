package com.sutpc.transpaas.algoserver.controller;

import com.sutpc.transpaas.algoserver.dto.CallBackDto;
import com.sutpc.transpaas.algoserver.dto.OmxCallBackDto;
import com.sutpc.transpaas.algoserver.service.AlgoRequestService;
import com.sutpc.transpaas.algoserver.service.ModelOmxService;
import com.sutpc.transpaas.algoserver.utils.ResponseResult;
import com.sutpc.transpaas.algoserver.utils.valid.ValidUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "算法执行状态的回调", tags = "3、算法执行状态的回调")
@RestController
@RequestMapping(value = "/algoCallBack")
@Slf4j
public class AlgoCallBackController {

  @Autowired
  private AlgoRequestService algoRequestService;
  @Autowired
  private ModelOmxService modelOmxService;

  @ApiOperation(value = "算法执行完成后的回调通知接口", notes = "接收算法执行完成的命令来更新算法任务执行完成状态")
  @RequestMapping(value = "algoDoneCallback", method = RequestMethod.POST)
  public ResponseResult algoDoneCallback(@RequestBody CallBackDto callBackDto) {
    ValidUtil.checkRequestParams(callBackDto);
    return this.algoRequestService.updateCallback(callBackDto);
  }

  @ApiOperation(value = "矩阵文件读取回调", notes = "主要根据token识别")
  @RequestMapping(value = "omxQueryCallback", method = RequestMethod.POST)
  public ResponseResult omxQueryCallback(@RequestBody OmxCallBackDto omxCallBackDto) {
    ValidUtil.checkRequestParams(omxCallBackDto);
    this.modelOmxService.updateCallback(omxCallBackDto);
    return ResponseResult.ok();
  }

}
