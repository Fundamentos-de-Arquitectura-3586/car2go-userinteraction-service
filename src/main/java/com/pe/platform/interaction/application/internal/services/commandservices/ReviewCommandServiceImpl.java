package com.pe.platform.interaction.application.internal.services.commandservices;

import com.pe.platform.interaction.domain.model.aggregates.Review;
import com.pe.platform.interaction.domain.services.ReviewCommandService;
import com.pe.platform.interaction.infrastructure.persistence.jpa.ReviewRepository;
import com.pe.platform.vehicle.domain.model.aggregates.Vehicle;
import com.pe.platform.vehicle.domain.model.valueobjects.vehicleStatus;
import com.pe.platform.vehicle.infrastructure.persistence.jpa.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewCommandServiceImpl implements ReviewCommandService {

    private final ReviewRepository reviewRepository;
    private final VehicleRepository vehicleRepository;

    public ReviewCommandServiceImpl(ReviewRepository reviewRepository, VehicleRepository vehicleRepository) {
        this.reviewRepository = reviewRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Optional<Review> getReviewByVehicleId(int vehicleId) {
        return reviewRepository.findByVehicleId(vehicleId).stream().findFirst();
    }

    @Transactional
    @Override
    public Review createReview(int vehicleId, String reviewedBy, String notes, boolean isApproved) {
        Optional<Review> existingReview = getReviewByVehicleId(vehicleId);
        if (existingReview.isPresent()) {
            throw new IllegalArgumentException("A review already exists for this vehicle.");
        }

        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        if (vehicleOptional.isEmpty()) {
            throw new IllegalArgumentException("Vehicle not found");
        }

        Vehicle vehicle = vehicleOptional.get();

        Review newReview = new Review(vehicle, reviewedBy, notes);
        Review savedReview = reviewRepository.save(newReview);

        if (isApproved) {
            vehicle.setStatus(vehicleStatus.REVIEWED);
        } else {
            vehicle.setStatus(vehicleStatus.REJECT);
        }

        vehicleRepository.save(vehicle);

        return savedReview;
    }
}
