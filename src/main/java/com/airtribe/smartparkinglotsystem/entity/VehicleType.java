package com.airtribe.smartparkinglotsystem.entity;

import java.util.List;

public enum VehicleType {
    MOTORCYCLE(List.of(SpotType.SMALL, SpotType.MEDIUM, SpotType.LARGE)),
    CAR(List.of(SpotType.MEDIUM, SpotType.LARGE)),
    BUS(List.of(SpotType.LARGE));

    private final List<SpotType> compatibleSpots;

    VehicleType(List<SpotType> compatibleSpots) {
        this.compatibleSpots = compatibleSpots;
    }

    public List<SpotType> getCompatibleSpots() {
        return compatibleSpots;
    }
}
