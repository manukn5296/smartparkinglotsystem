package com.airtribe.smartparkinglotsystem.service;


import com.airtribe.smartparkinglotsystem.dto.AvailabilityResponse;
import com.airtribe.smartparkinglotsystem.dto.CheckInRequest;
import com.airtribe.smartparkinglotsystem.dto.ExitResponse;
import com.airtribe.smartparkinglotsystem.dto.TicketResponse;
import com.airtribe.smartparkinglotsystem.entity.*;
import com.airtribe.smartparkinglotsystem.exception.DuplicateEntryException;
import com.airtribe.smartparkinglotsystem.exception.LotFullException;
import com.airtribe.smartparkinglotsystem.exception.TicketNotFoundException;
import com.airtribe.smartparkinglotsystem.repository.ParkingSpotRepository;
import com.airtribe.smartparkinglotsystem.repository.ParkingTicketRepository;
import com.airtribe.smartparkinglotsystem.repository.RateCardRepository;
import com.airtribe.smartparkinglotsystem.strategy.SpotAllocationStrategy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.Instant;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;

@Service
public class ParkingService {

    private final SpotAllocationStrategy allocationStrategy;
    private final ParkingTicketRepository ticketRepository;
    private final ParkingSpotRepository spotRepository;
    private final RateCardRepository rateCardRepository;
    private final FeeCalculator feeCalculator;
    private final Clock clock;

    public ParkingService(SpotAllocationStrategy allocationStrategy,
                          ParkingTicketRepository ticketRepository,
                          ParkingSpotRepository spotRepository,
                          RateCardRepository rateCardRepository,
                          FeeCalculator feeCalculator,
                          Clock clock) {
        this.allocationStrategy = allocationStrategy;
        this.ticketRepository = ticketRepository;
        this.spotRepository = spotRepository;
        this.rateCardRepository = rateCardRepository;
        this.feeCalculator = feeCalculator;
        this.clock = clock;
    }

    @Transactional
    public TicketResponse checkIn(CheckInRequest request) {
        String plate = request.vehicleNumber().trim().toUpperCase();

        if (ticketRepository.existsByVehicleNumberAndStatus(plate, TicketStatus.ACTIVE)) {
            throw new DuplicateEntryException(plate);
        }

        ParkingSpot spot = allocationStrategy.allocate(request.vehicleType())
                .orElseThrow(() -> new LotFullException(request.vehicleType()));
        spot.occupy();

        ParkingTicket ticket = ParkingTicket.open(plate, request.vehicleType(), spot, clock.instant());
        ticketRepository.save(ticket);

        return TicketResponse.from(ticket);
    }

    @Transactional
    public ExitResponse checkOut(UUID ticketId) {
        ParkingTicket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));

        // Already checked out: return the original result instead of charging again
        if (ticket.getStatus() == TicketStatus.COMPLETED) {
            return ExitResponse.from(ticket);
        }

        RateCard rate = rateCardRepository.findById(ticket.getVehicleType())
                .orElseThrow(() -> new IllegalStateException("No rate card for " + ticket.getVehicleType()));

        Instant now = clock.instant();
        ticket.close(now, feeCalculator.calculate(rate, ticket.getEntryTime(), now));
        ticket.getSpot().release();

        return ExitResponse.from(ticket);
    }

    @Transactional(readOnly = true)
    public TicketResponse getTicket(UUID ticketId) {
        return ticketRepository.findById(ticketId)
                .map(TicketResponse::from)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
    }

    @Transactional(readOnly = true)
    public AvailabilityResponse availability() {
        Map<SpotType, Long> available = new EnumMap<>(SpotType.class);
        Map<SpotType, Long> total = new EnumMap<>(SpotType.class);
        for (SpotType type : SpotType.values()) {
            available.put(type, spotRepository.countBySpotTypeAndStatus(type, SpotStatus.AVAILABLE));
            total.put(type, spotRepository.countBySpotType(type));
        }
        return new AvailabilityResponse(available, total);
    }
}
