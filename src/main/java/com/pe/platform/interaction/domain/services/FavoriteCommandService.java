package com.pe.platform.interaction.domain.services;

import com.pe.platform.interaction.domain.model.aggregates.Favorite;

import java.util.Optional;

public interface FavoriteCommandService {
    Optional<Favorite> addFavorite(int vehicleId, long profileId);
    void removeFavorite(int vehicleId, long profileId);
}
