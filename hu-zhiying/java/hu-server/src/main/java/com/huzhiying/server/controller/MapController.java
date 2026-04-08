package com.huzhiying.server.controller;

import com.huzhiying.domain.dto.ApiResponse;
import com.huzhiying.server.dto.MapDtos;
import com.huzhiying.server.service.MapService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 地图与服务范围接口。
 */
@RestController
@Tag(name = "map", description = "地图、服务范围和预约时段")
public class MapController {

    private final MapService mapService;

    public MapController(MapService mapService) {
        this.mapService = mapService;
    }

    @GetMapping("/api/map/service-cities")
    @Operation(summary = "查询可服务城市与区域列表")
    public ApiResponse<List<MapDtos.ServiceCityPayload>> serviceCities() {
        return ApiResponse.success(mapService.serviceCities());
    }

    @PostMapping("/api/map/regeo")
    @Operation(summary = "逆地理解析")
    public ApiResponse<MapDtos.RegeoPayload> reverseGeocode(@RequestBody MapDtos.RegeoRequest request) {
        return ApiResponse.success(mapService.reverseGeocode(request.latitude(), request.longitude()));
    }

    @PostMapping("/api/map/geofence/check")
    @Operation(summary = "校验地址是否在服务范围")
    public ApiResponse<MapDtos.GeofenceCheckPayload> geofenceCheck(@RequestBody MapDtos.GeofenceCheckRequest request) {
        return ApiResponse.success(mapService.geofenceCheck(request.city(), request.district()));
    }

    @PostMapping("/api/map/eta")
    @Operation(summary = "查询预计到达时间")
    public ApiResponse<MapDtos.EtaPayload> eta(@RequestBody MapDtos.EtaRequest request) {
        return ApiResponse.success(mapService.eta(request));
    }

    @GetMapping("/api/service-orders/time-slots")
    @Operation(summary = "查询预约时段列表")
    public ApiResponse<List<MapDtos.TimeSlotPayload>> timeSlots() {
        return ApiResponse.success(mapService.timeSlots());
    }
}
