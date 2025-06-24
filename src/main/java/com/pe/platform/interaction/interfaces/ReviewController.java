package com.pe.platform.interaction.interfaces;

import com.pe.platform.interaction.domain.model.aggregates.Review;
import com.pe.platform.interaction.domain.services.ReviewCommandService;
import com.pe.platform.interaction.interfaces.rest.dto.CreateReviewRequest;
import com.pe.platform.interaction.interfaces.rest.dto.ReviewDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/reviews")
public class ReviewController {

    private final ReviewCommandService reviewCommandService;

    public ReviewController(ReviewCommandService reviewCommandService) {
        this.reviewCommandService = reviewCommandService;
    }

    @PreAuthorize("hasAuthority('ROLE_MECHANIC')")
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody CreateReviewRequest reviewRequest) {
        Long vehicleId = reviewRequest.getVehicleId();
        if (reviewCommandService.getReviewByVehicleId(vehicleId).isPresent()) {
            return ResponseEntity.status(400).body("Review already exists for this vehicle");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long profileId = (Long) authentication.getPrincipal();

        String comment = reviewRequest.getNotes();
        int rating = reviewRequest.isApproved() ? 5 : 1;

        Review newReview = reviewCommandService.createReview(vehicleId, profileId, rating, comment);
        ReviewDTO reviewDTO = new ReviewDTO(newReview);

        return ResponseEntity.ok(reviewDTO);
    }

    @PreAuthorize("hasAuthority('ROLE_MECHANIC')")
    @GetMapping("/{vehicleId}")
    public ResponseEntity<ReviewDTO> getReviewByVehicleId(@PathVariable("vehicleId") Long vehicleId) {
        return reviewCommandService.getReviewByVehicleId(vehicleId)
                .map(review -> ResponseEntity.ok(new ReviewDTO(review)))
                .orElse(ResponseEntity.notFound().build());
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
        Long userId = (Long) authentication.getPrincipal();

        List<Review> reviews = reviewCommandService.getAllReviews().stream()
                .filter(review -> userId.equals(review.getProfileId()))
                .toList();

        List<ReviewDTO> reviewDTOs = reviews.stream()
                .map(ReviewDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(reviewDTOs);
    }
}
