package com.pe.platform.interaction.application.internal.services.commandservices;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pe.platform.interaction.domain.model.aggregates.Review;
import com.pe.platform.interaction.domain.services.ReviewCommandService;
import com.pe.platform.interaction.infrastructure.persistence.jpa.ReviewRepository;

@Service
public class ReviewCommandServiceImpl implements ReviewCommandService {

    private final ReviewRepository reviewRepository;

    public ReviewCommandServiceImpl(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Optional<Review> getReviewByVehicleId(Long vehicleId) {
        return reviewRepository.findByVehicleId(vehicleId).stream().findFirst();
    }

    @Transactional
    @Override
    public Review createReview(Long vehicleId, Long profileId, int rating, String comment) {
        if (reviewRepository.findByVehicleId(vehicleId).stream().findFirst().isPresent()) {
            throw new IllegalArgumentException("A review already exists for this vehicle.");
        }
        Review newReview = new Review(vehicleId, profileId, rating, comment);
        return reviewRepository.save(newReview);
    }
}
