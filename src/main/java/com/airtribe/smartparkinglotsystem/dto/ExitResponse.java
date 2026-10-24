package com.airtribe.smartparkinglotsystem.dto;


import com.airtribe.smartparkinglotsystem.entity.ParkingTicket;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public record ExitResponse(UUID ticketId,
                           String vehicleNumber,
                           Instant entryTime,
                           Instant exitTime,
                           BigDecimal fee) {

    public static ExitResponse from(ParkingTicket t) {
        return new ExitResponse(t.getId(), t.getVehicleNumber(),
                t.getEntryTime(), t.getExitTime(), t.getFee());
    }
}
