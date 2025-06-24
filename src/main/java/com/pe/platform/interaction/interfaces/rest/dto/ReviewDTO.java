package com.pe.platform.interaction.interfaces.rest.dto;

import com.pe.platform.interaction.domain.model.aggregates.Review;
import com.pe.platform.vehicle.domain.model.aggregates.Vehicle;
import com.pe.platform.vehicle.interfaces.rest.dto.VehicleSummaryDTO;

import java.time.LocalDateTime;

public class ReviewDTO {
    private Long id;
    private String reviewedBy;
    private String notes;
    private LocalDateTime reviewDate;

    private VehicleSummaryDTO vehicle;

    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.reviewedBy = review.getReviewedBy();
        this.notes = review.getNotes();
        this.reviewDate = review.getReviewDate();

        Vehicle vehicle = review.getVehicle();
        if (vehicle != null) {
            this.vehicle = new VehicleSummaryDTO(
                    vehicle.getId(),
                    vehicle.getName(),
                    vehicle.getBrand(),
                    vehicle.getModel(),
                    vehicle.getYear(),
                    vehicle.getPrice(),
                    vehicle.getTransmission(),
                    vehicle.getEngine(),
                    vehicle.getStatus().toString()
            );
        }
    }

    public Long getId() { return id; }
    public String getReviewedBy() { return reviewedBy; }
    public String getNotes() { return notes; }
    public LocalDateTime getReviewDate() { return reviewDate; }
    public VehicleSummaryDTO getVehicle() { return vehicle; }
}
