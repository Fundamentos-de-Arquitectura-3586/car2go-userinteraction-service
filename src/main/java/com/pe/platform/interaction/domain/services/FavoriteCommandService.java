package com.pe.platform.interaction.domain.services;

import java.util.Optional;

import com.pe.platform.interaction.domain.model.aggregates.Favorite;

public interface FavoriteCommandService {
    Optional<Favorite> addFavorite(Long vehicleId, long profileId);
    void removeFavorite(Long vehicleId, long profileId);
}
