package com.airtribe.smartparkinglotsystem.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "parking_ticket", indexes = {
        @Index(name = "idx_ticket_vehicle_status", columnList = "vehicleNumber, status")
})
public class ParkingTicket {

    @Id
    private UUID id;

    @Column(nullable = false, length = 20)
    private String vehicleNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private VehicleType vehicleType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "spot_id")
    private ParkingSpot spot;

    @Column(nullable = false)
    private Instant entryTime;

    private Instant exitTime;

    @Column(precision = 10, scale = 2)
    private BigDecimal fee;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 15)
    private TicketStatus status;

    protected ParkingTicket() { }

    public static ParkingTicket open(String vehicleNumber, VehicleType type, ParkingSpot spot, Instant now) {
        ParkingTicket t = new ParkingTicket();
        t.id = UUID.randomUUID();
        t.vehicleNumber = vehicleNumber;
        t.vehicleType = type;
        t.spot = spot;
        t.entryTime = now;
        t.status = TicketStatus.ACTIVE;
        return t;
    }

    public void close(Instant now, BigDecimal fee) {
        this.exitTime = now;
        this.fee = fee;
        this.status = TicketStatus.COMPLETED;
    }

    public UUID getId() { return id; }
    public String getVehicleNumber() { return vehicleNumber; }
    public VehicleType getVehicleType() { return vehicleType; }
    public ParkingSpot getSpot() { return spot; }
    public Instant getEntryTime() { return entryTime; }
    public Instant getExitTime() { return exitTime; }
    public BigDecimal getFee() { return fee; }
    public TicketStatus getStatus() { return status; }
}
