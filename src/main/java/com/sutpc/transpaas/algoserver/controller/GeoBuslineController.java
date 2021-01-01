package com.sutpc.transpaas.algoserver.controller;

import com.sutpc.transpaas.algoserver.dto.response.StationVolumeResponse;
import com.sutpc.transpaas.algoserver.entity.GeoBusline;
import com.sutpc.transpaas.algoserver.entity.SegmentVolume;
import com.sutpc.transpaas.algoserver.entity.ZoneSpeedBig;
import com.sutpc.transpaas.algoserver.entity.ZoneSpeedMid;
import com.sutpc.transpaas.algoserver.service.GeoBuslineService;
import com.sutpc.transpaas.algoserver.service.LineStopRelaService;
import com.sutpc.transpaas.algoserver.service.SegmentVolumeService;
import com.sutpc.transpaas.algoserver.utils.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 公交信息查询.
 */
@Api(value = "公交信息查询", tags = "10、小汽车、公交分配相关接口")
@RestController
@RequestMapping(value = "/geoBusline")
@Slf4j
public class GeoBuslineController {

  @Autowired
  private GeoBuslineService geoBuslineService;
  @Autowired
  private LineStopRelaService lineStopRelaService;
  @Autowired
  private SegmentVolumeService segmentVolumeService;

  @ApiOperation(value = "根据线路名代码线路站点信息", notes = "公交线路信息查询")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "routeIndex", value = "线路代码", paramType = "query", dataType = "int", required = true)
  })
  @RequestMapping(value = "queryListByRouteIndex", method = RequestMethod.GET)
  public ResponseResult<List<GeoBusline>> queryListByRouteIndex(@RequestParam int routeIndex) {

    return ResponseResult.ok(this.lineStopRelaService.queryListByRouteIndex(routeIndex));
  }

  @ApiOperation(value = "根据线路类型查询线路列表", notes = "公交线路信息查询")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "transitMode", value = "线路类型(1为常规公交，2为地铁，3为有轨电车)", paramType = "query", dataType = "int", required = true)
  })
  @RequestMapping(value = "queryListByMode", method = RequestMethod.GET)
  public ResponseResult<List<GeoBusline>> queryListByMode(@RequestParam int transitMode) {

    return ResponseResult.ok(this.geoBuslineService.queryListByMode(transitMode));
  }

  @ApiOperation(value = "根据线路名查询单条线路断面流量", notes = "公交线路信息查询")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "taskId", value = "模型ID", paramType = "query", dataType = "String", required = true),
      @ApiImplicitParam(name = "timeSegment", value = "时间段（AM,EN,MD,NT,PM）", paramType = "query", dataType = "String", required = true),
      @ApiImplicitParam(name = "line", value = "线路名", paramType = "query", dataType = "String", required = true)
  })
  @RequestMapping(value = "queryWktByLine", method = RequestMethod.GET)
  public ResponseResult<List<SegmentVolume>> queryWktByLine(
      @RequestParam String taskId,
      @RequestParam String timeSegment,
      @RequestParam String line) {

    return ResponseResult.ok(this.geoBuslineService.queryWktByLine(taskId, timeSegment, line));
  }

  @ApiOperation(value = "站点流量查询", notes = "公交线路信息查询")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "taskId", value = "模型ID", paramType = "query", dataType = "String", required = true),
      @ApiImplicitParam(name = "timeSegment", value = "时间段（AM,EN,MD,NT,PM）", paramType = "query", dataType = "String", required = true),
      @ApiImplicitParam(name = "stationType", value = "站点类型（2:公交，3：地铁，5：有轨）", paramType = "query", dataType = "int", required = true)
  })
  @RequestMapping(value = "queryStationVolume", method = RequestMethod.GET)
  public ResponseResult<List<StationVolumeResponse>> queryStationVolume(
      @RequestParam String taskId,
      @RequestParam String timeSegment,
      @RequestParam int stationType) {

    return ResponseResult.ok(this.segmentVolumeService.queryStationVolume(taskId, timeSegment, stationType));
  }

  @ApiOperation(value = "根据from_id和to_id，从t_link_volume表查询所有时段速度", notes = "公交线路信息查询")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "taskId", value = "模型ID", paramType = "query", dataType = "String", required = true),
      @ApiImplicitParam(name = "fromId", value = "fromId", paramType = "query", dataType = "String", required = true),
      @ApiImplicitParam(name = "toId", value = "toId", paramType = "query", dataType = "String", required = true)
  })
  @RequestMapping(value = "querySpeedByFromIdAndToId", method = RequestMethod.GET)
  public ResponseResult<List<SegmentVolume>> querySpeedByFromIdAndToId(
      @RequestParam String taskId,
      @RequestParam String fromId,
      @RequestParam String toId) {

    return ResponseResult.ok(this.geoBuslineService.querySpeedByFromIdAndToId(taskId, fromId, toId));
  }

  @ApiOperation(value = "片区运行速度查询(中区)", notes = "ZoneSpeedMid")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "taskId", value = "模型ID", paramType = "query", dataType = "String", required = true),
      @ApiImplicitParam(name = "timeSegment", value = "时间段（AM,EN,MD,NT,PM）", paramType = "query", dataType = "String", required = true)
  })
  @RequestMapping(value = "queryZoneSpeedMid", method = RequestMethod.GET)
  public ResponseResult<List<ZoneSpeedMid>> queryZoneSpeedMid(
      @RequestParam String taskId,
      @RequestParam String timeSegment) {

    return ResponseResult.ok(this.geoBuslineService.queryZoneSpeedMid(taskId, timeSegment));
  }

  @ApiOperation(value = "片区运行速度查询(大区)", notes = "ZoneSpeedBig")
  @ApiImplicitParams({
      @ApiImplicitParam(name = "taskId", value = "模型ID", paramType = "query", dataType = "String", required = true),
      @ApiImplicitParam(name = "timeSegment", value = "时间段（AM,EN,MD,NT,PM）", paramType = "query", dataType = "String", required = true)
  })
  @RequestMapping(value = "queryZoneSpeedBig", method = RequestMethod.GET)
  public ResponseResult<List<ZoneSpeedBig>> queryZoneSpeedBig(
      @RequestParam String taskId,
      @RequestParam String timeSegment) {

    return ResponseResult.ok(this.geoBuslineService.queryZoneSpeedBig(taskId, timeSegment));
  }

}
