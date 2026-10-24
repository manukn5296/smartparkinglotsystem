package com.airtribe.smartparkinglotsystem.dto;

import com.airtribe.smartparkinglotsystem.entity.SpotType;

import java.util.Map;

public record AvailabilityResponse(Map<SpotType, Long> available,
                                   Map<SpotType, Long> total) { }
