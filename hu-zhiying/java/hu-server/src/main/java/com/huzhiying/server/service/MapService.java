package com.huzhiying.server.service;

import com.huzhiying.server.dto.MapDtos;
import com.huzhiying.server.persistence.PersistenceEntities;
import com.huzhiying.server.repository.PlatformRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * 地图业务服务。前端只访问平台 API，不直接拼接第三方地图能力。
 */
@Service
@Transactional(readOnly = true)
public class MapService {

    private final PlatformRepository platformRepository;
    private final List<String> cityWhitelist;

    public MapService(PlatformRepository platformRepository,
                      @Value("${hzy.app.city-whitelist:上海,杭州,苏州,南京}") String cityWhitelist) {
        this.platformRepository = platformRepository;
        this.cityWhitelist = Arrays.stream(cityWhitelist.split(","))
                .map(String::trim)
                .filter(item -> !item.isBlank())
                .toList();
    }

    public List<MapDtos.ServiceCityPayload> serviceCities() {
        return platformRepository.listAll("DispatchZoneEntity", "e.sortOrder asc, e.id asc", PersistenceEntities.DispatchZoneEntity.class)
                .stream()
                .filter(item -> Boolean.TRUE.equals(item.enabled))
                .map(item -> new MapDtos.ServiceCityPayload(
                        item.id,
                        item.cityName,
                        item.districtName,
                        item.sortOrder != null && item.sortOrder <= 20
                ))
                .toList();
    }

    public MapDtos.RegeoPayload reverseGeocode(Double latitude, Double longitude) {
        PersistenceEntities.DispatchZoneEntity zone = platformRepository
                .listAll("DispatchZoneEntity", "e.sortOrder asc, e.id asc", PersistenceEntities.DispatchZoneEntity.class)
                .stream()
                .filter(item -> Boolean.TRUE.equals(item.enabled))
                .findFirst()
                .orElse(null);
        String city = zone == null ? firstWhitelistedCity() : zone.cityName;
        String district = zone == null ? "主城区" : zone.districtName;
        String address = city + district + "服务地址 " + formatCoordinate(latitude) + "," + formatCoordinate(longitude);
        return new MapDtos.RegeoPayload(city, district, address, isServiceable(city, district));
    }

    public MapDtos.GeofenceCheckPayload geofenceCheck(String city, String district) {
        boolean serviceable = isServiceable(city, district);
        String matchedZone = serviceable ? (city + "·" + district) : "当前地址不在服务范围";
        return new MapDtos.GeofenceCheckPayload(serviceable, matchedZone);
    }

    public MapDtos.EtaPayload eta(MapDtos.EtaRequest request) {
        int distanceBase = request.orderId() == null || request.orderId().isBlank()
                ? 4
                : Math.max(3, request.orderId().length() % 8 + 2);
        int etaBase = distanceBase * 4;
        return new MapDtos.EtaPayload(etaBase + " 分钟", "约 " + distanceBase + ".6 公里");
    }

    public List<MapDtos.TimeSlotPayload> timeSlots() {
        return List.of(
                new MapDtos.TimeSlotPayload("今天 14:00-16:00"),
                new MapDtos.TimeSlotPayload("今天 16:00-18:00"),
                new MapDtos.TimeSlotPayload("今天 18:00-20:00"),
                new MapDtos.TimeSlotPayload("明天 09:00-11:00"),
                new MapDtos.TimeSlotPayload("明天 13:00-15:00")
        );
    }

    private boolean isServiceable(String city, String district) {
        boolean whiteListed = cityWhitelist.stream().anyMatch(item -> item.equalsIgnoreCase(city));
        if (!whiteListed) {
            return false;
        }
        return platformRepository.listAll("DispatchZoneEntity", "e.sortOrder asc, e.id asc", PersistenceEntities.DispatchZoneEntity.class)
                .stream()
                .filter(item -> Boolean.TRUE.equals(item.enabled))
                .anyMatch(item -> item.cityName.equalsIgnoreCase(city) && item.districtName.equalsIgnoreCase(district));
    }

    private String firstWhitelistedCity() {
        return cityWhitelist.isEmpty() ? "上海" : cityWhitelist.get(0);
    }

    private String formatCoordinate(Double value) {
        if (value == null) {
            return "0.0000";
        }
        return String.format(Locale.ROOT, "%.4f", value);
    }
}
