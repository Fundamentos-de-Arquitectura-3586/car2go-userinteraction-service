package com.pe.platform.interaction.interfaces.rest.dto;

import com.pe.platform.interaction.domain.model.aggregates.Review;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class ReviewDTO {
    private Long id;
    private Long profileId;
    private String comment;
    private int rating;
    private Long vehicleId;
    private LocalDateTime createdAt;

    public ReviewDTO(Review review) {
        this.id = review.getId();
        this.profileId = review.getProfileId();
        this.comment = review.getComment();
        this.rating = review.getRating();
        this.vehicleId = review.getVehicleId();
        this.createdAt = review.getCreatedAt().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public Long getId() { return id; }
    public Long getProfileId() { return profileId; }
    public String getComment() { return comment; }
    public int getRating() { return rating; }
    public Long getVehicleId() { return vehicleId; }
    public LocalDateTime getCreatedAt() { return createdAt; }
}
