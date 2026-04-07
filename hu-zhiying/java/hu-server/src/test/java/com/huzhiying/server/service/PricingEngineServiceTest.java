package com.huzhiying.server.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class PricingEngineServiceTest {

    private final PricingEngineService pricingEngineService = new PricingEngineService();

    @Test
    void shouldApplyEmergencyAndNightCoefficient() {
        BigDecimal total = pricingEngineService.estimate(BigDecimal.valueOf(58), BigDecimal.valueOf(30), true, true);
        Assertions.assertEquals(BigDecimal.valueOf(140), total);
    }
}
