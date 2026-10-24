package com.airtribe.smartparkinglotsystem.controller;

import com.airtribe.smartparkinglotsystem.dto.AvailabilityResponse;
import com.airtribe.smartparkinglotsystem.dto.CheckInRequest;
import com.airtribe.smartparkinglotsystem.dto.ExitResponse;
import com.airtribe.smartparkinglotsystem.dto.TicketResponse;
import com.airtribe.smartparkinglotsystem.service.ParkingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/parking")
public class ParkingController {

    private final ParkingService parkingService;

    public ParkingController(ParkingService parkingService) {
        this.parkingService = parkingService;
    }

    @PostMapping("/entry")
    @ResponseStatus(HttpStatus.CREATED)
    public TicketResponse entry(@Valid @RequestBody CheckInRequest request) {
        return parkingService.checkIn(request);
    }

    @PostMapping("/exit/{ticketId}")
    public ExitResponse exit(@PathVariable UUID ticketId) {
        return parkingService.checkOut(ticketId);
    }

    @GetMapping("/tickets/{ticketId}")
    public TicketResponse ticket(@PathVariable UUID ticketId) {
        return parkingService.getTicket(ticketId);
    }

    @GetMapping("/availability")
    public AvailabilityResponse availability() {
        return parkingService.availability();
    }
}
