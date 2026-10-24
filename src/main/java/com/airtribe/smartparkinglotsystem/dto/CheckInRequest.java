package com.airtribe.smartparkinglotsystem.dto;

import com.airtribe.smartparkinglotsystem.entity.VehicleType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CheckInRequest(
        @NotBlank String vehicleNumber,
        @NotNull VehicleType vehicleType) { }
