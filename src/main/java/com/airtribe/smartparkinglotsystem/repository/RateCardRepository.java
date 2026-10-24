package com.airtribe.smartparkinglotsystem.repository;

import com.airtribe.smartparkinglotsystem.entity.RateCard;
import com.airtribe.smartparkinglotsystem.entity.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RateCardRepository extends JpaRepository<RateCard, VehicleType> { }
