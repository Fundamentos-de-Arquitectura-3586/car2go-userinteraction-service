package com.pe.platform.interaction.infrastructure.persistence.jpa;

import com.pe.platform.interaction.domain.model.aggregates.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByVehicleId(Long vehicleId);
    
}
