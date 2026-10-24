package com.airtribe.smartparkinglotsystem.repository;


import com.airtribe.smartparkinglotsystem.entity.ParkingSpot;
import com.airtribe.smartparkinglotsystem.entity.SpotStatus;
import com.airtribe.smartparkinglotsystem.entity.SpotType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {

    Optional<ParkingSpot> findFirstBySpotTypeAndStatusOrderByFloorNumberAscSpotNumberAsc(
            SpotType spotType, SpotStatus status);

    long countBySpotTypeAndStatus(SpotType spotType, SpotStatus status);

    long countBySpotType(SpotType spotType);
}