package com.sutpc.transpaas.algoserver.controller;


import com.sutpc.transpaas.algoserver.constant.AlgoServerType;
import com.sutpc.transpaas.algoserver.service.ModelAlgoServerService;
import com.sutpc.transpaas.algoserver.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "查询资源使用情况与释放资源", tags = "8、算法资源管控")
@RestController
@RequestMapping(value = "/modelAlgoServer")
@Slf4j
public class ModelAlgoServerController {

  @Autowired
  private ModelAlgoServerService modelAlgoServerService;

  @ApiOperation(value = "可用算法服务器查询", notes = "如果返回数据为空，说明资源都被占用了，建议释放资源")
  @RequestMapping(value = "queryList", method = RequestMethod.GET)
  public ResponseResult queryList(@RequestParam AlgoServerType algoServerType) {

    return ResponseResult.ok(modelAlgoServerService.queryServerList(algoServerType));
  }

  @ApiOperation(value = "释放所有算法机器", notes = "释放所有机器")
  @RequestMapping(value = "unLockAll", method = RequestMethod.POST)
  public ResponseResult unLockAll() {
    modelAlgoServerService.unLockAll();
    return ResponseResult.ok();
  }

}
