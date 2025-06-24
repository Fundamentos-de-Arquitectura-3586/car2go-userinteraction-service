package com.pe.platform.interaction.interfaces.rest.dto;  // Ajusta este paquete según la ubicación que elijas.

public class CreateReviewRequest {
    private int vehicleId;
    private String notes;
    private boolean isApproved;

    // Constructor sin parámetros
    public CreateReviewRequest() {}

    // Getters y Setters
    public int getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(int vehicleId) {
        this.vehicleId = vehicleId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
