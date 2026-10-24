package com.airtribe.smartparkinglotsystem.repository;

import com.airtribe.smartparkinglotsystem.entity.ParkingTicket;
import com.airtribe.smartparkinglotsystem.entity.TicketStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ParkingTicketRepository extends JpaRepository<ParkingTicket, UUID> {

    boolean existsByVehicleNumberAndStatus(String vehicleNumber, TicketStatus status);
}
