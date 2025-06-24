package com.pe.platform.interaction.domain.services;

import java.util.List;
import java.util.Optional;

import com.pe.platform.interaction.domain.model.aggregates.Review;

public interface ReviewCommandService {

    List<Review> getAllReviews();

    Optional<Review> getReviewByVehicleId(Long vehicleId);
    Review createReview(Long vehicleId, Long profileId, int rating, String comment);

}
