package com.pe.platform.interaction.application.internal.services.commandservices;

import java.io.Console;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pe.platform.interaction.application.internal.services.outbound.ExternalVehicleService;
import com.pe.platform.interaction.domain.model.aggregates.Review;
import com.pe.platform.interaction.domain.services.ReviewCommandService;
import com.pe.platform.interaction.infrastructure.persistence.jpa.ReviewRepository;
import com.pe.platform.shared.domain.exceptions.VehicleNotFoundException;

@Service
public class ReviewCommandServiceImpl implements ReviewCommandService {

    private final ReviewRepository reviewRepository;
    private final ExternalVehicleService externalVehicleService;

    public ReviewCommandServiceImpl(ReviewRepository reviewRepository, ExternalVehicleService externalVehicleService) {
        this.reviewRepository = reviewRepository;
        this.externalVehicleService = externalVehicleService;
    }

    @Override
    public List<Review> getAllReviews() {
        return reviewRepository.findAll();
    }

    @Override
    public Optional<Review> getReviewByVehicleId(Long vehicleId) {
        // Validar si el vehículo existe
        if(externalVehicleService.getVehicleById(vehicleId) == null){
            System.out.println("Vehicle not found: " + externalVehicleService.getVehicleById(vehicleId));
            throw new VehicleNotFoundException(vehicleId);
        }
        return reviewRepository.findByVehicleId(vehicleId).stream().findFirst();
    }

    @Transactional
    @Override
    public Review createReview(Long vehicleId, Long profileId, int rating, String comment) {
        // Validar si el vehículo existe
        if(externalVehicleService.getVehicleById(vehicleId) == null){
            throw new VehicleNotFoundException(vehicleId);
        }
        
        if (reviewRepository.findByVehicleId(vehicleId).stream().findFirst().isPresent()) {
            throw new IllegalArgumentException("A review already exists for this vehicle.");
        }
        
        Review newReview = new Review(vehicleId, profileId, rating, comment);
        return reviewRepository.save(newReview);
    }
}
