package com.pe.platform.interaction.interfaces;

import com.pe.platform.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.pe.platform.interaction.domain.model.aggregates.Review;
import com.pe.platform.interaction.domain.services.ReviewCommandService;
import com.pe.platform.interaction.interfaces.rest.dto.CreateReviewRequest;
import com.pe.platform.interaction.interfaces.rest.dto.ReviewDTO;
import com.pe.platform.vehicle.domain.model.aggregates.Vehicle;
import com.pe.platform.vehicle.domain.services.VehicleCommandService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final VehicleCommandService vehicleCommandService;
    private final ReviewCommandService reviewCommandService;

    public ReviewController(VehicleCommandService vehicleCommandService, ReviewCommandService reviewCommandService) {
        this.vehicleCommandService = vehicleCommandService;
        this.reviewCommandService = reviewCommandService;
    }

    @PreAuthorize("hasAuthority('ROLE_MECHANIC')")
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody CreateReviewRequest reviewRequest) {
        int vehicleId = reviewRequest.getVehicleId();
        String notes = reviewRequest.getNotes();
        boolean isApproved = reviewRequest.isApproved();

        Optional<Vehicle> vehicleOptional = vehicleCommandService.findById(vehicleId);
        if (vehicleOptional.isEmpty()) {
            return ResponseEntity.status(404).body("Vehicle not found");
        }

        if (reviewCommandService.getReviewByVehicleId(vehicleId).isPresent()) {
            return ResponseEntity.status(400).body("Review already exists for this vehicle");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String reviewedBy = String.valueOf(userDetails.getId());

        Review newReview = reviewCommandService.createReview(vehicleId, reviewedBy, notes, isApproved);
        ReviewDTO reviewDTO = new ReviewDTO(newReview);

        return ResponseEntity.ok(reviewDTO);
    }



    @PreAuthorize("hasAuthority('ROLE_MECHANIC')")
    @GetMapping("/{carId}")
    public ResponseEntity<ReviewDTO> getReviewByCarId(@PathVariable("carId") int carId) {
        Optional<Review> review = reviewCommandService.getReviewByVehicleId(carId);

        if (review.isEmpty()) {
            return ResponseEntity.status(404).body(null);
        }

        ReviewDTO reviewDTO = new ReviewDTO(review.get());
        return ResponseEntity.ok(reviewDTO);
    }

    @PreAuthorize("hasAuthority('ROLE_MECHANIC')")
    @GetMapping
    public ResponseEntity<List<ReviewDTO>> getAllReviews() {
        List<Review> reviews = reviewCommandService.getAllReviews();

        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(reviewDTOs);
    }

    @PreAuthorize("hasAuthority('ROLE_MECHANIC')")
    @GetMapping("/me")
    public ResponseEntity<List<ReviewDTO>> getMyReviews() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String userId = String.valueOf(userDetails.getId());

        List<Review> reviews = reviewCommandService.getAllReviews().stream()
                .filter(review -> userId.equals(review.getReviewedBy()))
                .toList();

        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(reviewDTOs);
    }

}
