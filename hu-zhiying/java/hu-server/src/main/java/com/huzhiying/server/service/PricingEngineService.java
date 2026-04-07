package com.huzhiying.server.service;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Service
public class PricingEngineService {

    public BigDecimal estimate(BigDecimal basePrice, BigDecimal doorPrice, boolean emergency, boolean nightService) {
        BigDecimal total = basePrice.add(doorPrice);
        if (emergency) {
            total = total.add(BigDecimal.valueOf(20));
        }
        if (nightService) {
            total = total.multiply(BigDecimal.valueOf(1.3));
        }
        return total.setScale(0, RoundingMode.HALF_UP);
    }
}
