package com.pe.platform.shared.domain.exceptions;

public class VehicleNotFoundException extends RuntimeException {
    public VehicleNotFoundException(String message) {
        super(message);
    }
    
    public VehicleNotFoundException(Long vehicleId) {
        super("Vehicle with ID " + vehicleId + " does not exist.");
    }
}
