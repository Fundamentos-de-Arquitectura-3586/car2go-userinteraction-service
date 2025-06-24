package com.pe.platform.interaction.domain.model.aggregates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.pe.platform.vehicle.domain.model.aggregates.Vehicle;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    @JsonIgnore
    private Vehicle vehicle;

    @Column(nullable = false)
    private String reviewedBy;

    @Column(nullable = false)
    private String notes;

    @Column(nullable = false)
    private LocalDateTime reviewDate;

    protected Review() {}

    public Review(Vehicle vehicle, String reviewedBy, String notes) {
        this.vehicle = vehicle;
        this.reviewedBy = reviewedBy;
        this.notes = notes;
        this.reviewDate = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public String getReviewedBy() {
        return reviewedBy;
    }

    public void setReviewedBy(String reviewedBy) {
        this.reviewedBy = reviewedBy;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getReviewDate() {
        return reviewDate;
    }

    public void setReviewDate(LocalDateTime reviewDate) {
        this.reviewDate = reviewDate;
    }


}
