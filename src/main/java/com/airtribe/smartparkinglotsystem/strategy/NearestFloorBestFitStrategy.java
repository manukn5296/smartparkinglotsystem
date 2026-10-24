package com.airtribe.smartparkinglotsystem.strategy;

import com.airtribe.smartparkinglotsystem.entity.ParkingSpot;
import com.airtribe.smartparkinglotsystem.entity.SpotStatus;
import com.airtribe.smartparkinglotsystem.entity.SpotType;
import com.airtribe.smartparkinglotsystem.entity.VehicleType;
import com.airtribe.smartparkinglotsystem.repository.ParkingSpotRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class NearestFloorBestFitStrategy implements SpotAllocationStrategy {

    private final ParkingSpotRepository spotRepository;

    public NearestFloorBestFitStrategy(ParkingSpotRepository spotRepository) {
        this.spotRepository = spotRepository;
    }

    @Override
    public Optional<ParkingSpot> allocate(VehicleType vehicleType) {
        for (SpotType spotType : vehicleType.getCompatibleSpots()) {
            Optional<ParkingSpot> spot = spotRepository
                    .findFirstBySpotTypeAndStatusOrderByFloorNumberAscSpotNumberAsc(spotType, SpotStatus.AVAILABLE);
            if (spot.isPresent()) {
                return spot;
            }
        }
        return Optional.empty();
    }
}
