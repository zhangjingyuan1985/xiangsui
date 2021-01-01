package com.sutpc.transpaas.algoserver.controller;


import com.sutpc.transpaas.algoserver.service.GeoSmallZoneService;
import com.sutpc.transpaas.algoserver.utils.ResponseResult;
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

@Api(value = "小区数据查询查询", tags = "6、小区查询")
@RestController
@RequestMapping(value = "/geoSmallZone")
@Slf4j
public class GeoSmallZoneController {

  @Autowired
  private GeoSmallZoneService geoSmallZoneService;

  @ApiOperation(value = "小区ID数据查询", notes = "小区ID数据查询")
  @RequestMapping(value = "getSmallZoneIds", method = RequestMethod.GET)
  public ResponseResult getSmallZoneIds() {

    return ResponseResult.ok(this.geoSmallZoneService.getSmallZoneIds());
  }

  @ApiOperation(value = "用户设置的单字段去重查询", notes = "去重查询")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "taskId", value = "模型ID", paramType = "query", dataType = "String", required = true),
      @ApiImplicitParam(name = "userId", value = "用户ID", paramType = "query", dataType = "string", required = true),
      @ApiImplicitParam(
          name = "attribute",
          value = "字段名称(REGION,PARK_COST_INDEX)",
          paramType = "query", dataType = "string", required = true)
  })
  @RequestMapping(value = "queryDistinctList", method = RequestMethod.GET)
  public ResponseResult queryDistinctList(@RequestParam String taskId, @RequestParam String userId,
      String attribute) {

    return ResponseResult.ok(geoSmallZoneService.queryDistinct(taskId, userId, attribute));
  }

}
