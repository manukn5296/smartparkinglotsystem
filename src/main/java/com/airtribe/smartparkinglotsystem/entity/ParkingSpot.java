package com.airtribe.smartparkinglotsystem.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "parking_spot",
        uniqueConstraints = @UniqueConstraint(columnNames = {"floor_number", "spot_number"}))
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "floor_number", nullable = false)
    private int floorNumber;

    @Column(name = "spot_number", nullable = false)
    private int spotNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SpotType spotType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SpotStatus status = SpotStatus.AVAILABLE;

    protected ParkingSpot() { }

    public ParkingSpot(int floorNumber, int spotNumber, SpotType spotType) {
        this.floorNumber = floorNumber;
        this.spotNumber = spotNumber;
        this.spotType = spotType;
    }

    public void occupy() {
        if (status != SpotStatus.AVAILABLE) {
            throw new IllegalStateException("Spot " + id + " is not available");
        }
        status = SpotStatus.OCCUPIED;
    }

    public void release() {
        status = SpotStatus.AVAILABLE;
    }

    public Long getId() { return id; }
    public int getFloorNumber() { return floorNumber; }
    public int getSpotNumber() { return spotNumber; }
    public SpotType getSpotType() { return spotType; }
    public SpotStatus getStatus() { return status; }
}
