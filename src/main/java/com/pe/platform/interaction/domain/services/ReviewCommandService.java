package com.pe.platform.interaction.domain.services;

import com.pe.platform.interaction.domain.model.aggregates.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewCommandService {

    List<Review> getAllReviews();

    Optional<Review> getReviewByVehicleId(int vehicleId);
    Review createReview(int vehicleId, String reviewedBy, String notes, boolean isApproved);

}
