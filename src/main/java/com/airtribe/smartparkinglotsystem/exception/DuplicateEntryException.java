package com.airtribe.smartparkinglotsystem.exception;

public class DuplicateEntryException extends RuntimeException {
    public DuplicateEntryException(String vehicleNumber) {
        super("Vehicle " + vehicleNumber + " is already parked");
    }
}
