package com.airtribe.smartparkinglotsystem.exception;

import com.airtribe.smartparkinglotsystem.entity.VehicleType;

public class LotFullException extends RuntimeException {
    public LotFullException(VehicleType type) {
        super("No parking spot available for vehicle type " + type);
    }
}
