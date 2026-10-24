package com.airtribe.smartparkinglotsystem.service;

import com.airtribe.smartparkinglotsystem.entity.RateCard;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;

@Component
public class FeeCalculator {

    private static final long MINUTES_PER_DAY = 24 * 60;

    public BigDecimal calculate(RateCard rate, Instant entry, Instant exit) {
        long minutes = Duration.between(entry, exit).toMinutes();
        if (minutes <= rate.getGraceMinutes()) {
            return BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        }

        long fullDays = minutes / MINUTES_PER_DAY;
        long remainingMinutes = minutes % MINUTES_PER_DAY;
        long remainingHours = (remainingMinutes + 59) / 60; // round up to started hour

        BigDecimal remainder = rate.getHourlyRate()
                .multiply(BigDecimal.valueOf(remainingHours))
                .min(rate.getDailyCap());
        BigDecimal days = rate.getDailyCap().multiply(BigDecimal.valueOf(fullDays));

        return days.add(remainder).setScale(2, RoundingMode.HALF_UP);
    }
}