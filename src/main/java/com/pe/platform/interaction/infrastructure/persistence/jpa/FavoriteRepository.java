package com.pe.platform.interaction.infrastructure.persistence.jpa;

import com.pe.platform.interaction.domain.model.aggregates.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Integer> {
    List<Favorite> findByProfileId(long profileId);
    Optional<Favorite> findByProfileIdAndVehicleId(long profileId, int vehicleId);
}
