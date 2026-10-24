package com.airtribe.smartparkinglotsystem.strategy;


import com.airtribe.smartparkinglotsystem.entity.ParkingSpot;
import com.airtribe.smartparkinglotsystem.entity.VehicleType;

import java.util.Optional;

public interface SpotAllocationStrategy {
    Optional<ParkingSpot> allocate(VehicleType vehicleType);
}
