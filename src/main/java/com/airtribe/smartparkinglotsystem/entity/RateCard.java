package com.airtribe.smartparkinglotsystem.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "rate_card")
public class RateCard {

    @Id
    @Enumerated(EnumType.STRING)
    @Column(length = 15)
    private VehicleType vehicleType;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal hourlyRate;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal dailyCap;

    @Column(nullable = false)
    private int graceMinutes;

    protected RateCard() { }

    public RateCard(VehicleType vehicleType, BigDecimal hourlyRate, BigDecimal dailyCap, int graceMinutes) {
        this.vehicleType = vehicleType;
        this.hourlyRate = hourlyRate;
        this.dailyCap = dailyCap;
        this.graceMinutes = graceMinutes;
    }

    public VehicleType getVehicleType() { return vehicleType; }
    public BigDecimal getHourlyRate() { return hourlyRate; }
    public BigDecimal getDailyCap() { return dailyCap; }
    public int getGraceMinutes() { return graceMinutes; }
}