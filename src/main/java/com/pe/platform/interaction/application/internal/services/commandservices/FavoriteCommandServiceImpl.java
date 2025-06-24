package com.pe.platform.interaction.application.internal.services.commandservices;

import com.pe.platform.interaction.domain.model.aggregates.Favorite;
import com.pe.platform.interaction.domain.services.FavoriteCommandService;
import com.pe.platform.interaction.infrastructure.persistence.jpa.FavoriteRepository;
import com.pe.platform.vehicle.domain.model.aggregates.Vehicle;
import com.pe.platform.vehicle.infrastructure.persistence.jpa.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FavoriteCommandServiceImpl implements FavoriteCommandService {

    private final FavoriteRepository favoriteRepository;
    private final VehicleRepository vehicleRepository;

    public FavoriteCommandServiceImpl(FavoriteRepository favoriteRepository, VehicleRepository vehicleRepository) {
        this.favoriteRepository = favoriteRepository;
        this.vehicleRepository = vehicleRepository;
    }

    @Override
    public Optional<Favorite> addFavorite(int vehicleId, long profileId) {
        Optional<Vehicle> vehicleOptional = vehicleRepository.findById(vehicleId);
        if (vehicleOptional.isEmpty()) {
            return Optional.empty();
        }

        Optional<Favorite> existingFavorite = favoriteRepository.findByProfileIdAndVehicleId(profileId, vehicleId);
        if (existingFavorite.isPresent()) {
            return existingFavorite;
        }

        Favorite newFavorite = new Favorite(vehicleOptional.get(), profileId);
        return Optional.of(favoriteRepository.save(newFavorite));
    }

    @Override
    public void removeFavorite(int vehicleId, long profileId) {
        Optional<Favorite> favoriteOptional = favoriteRepository.findByProfileIdAndVehicleId(profileId, vehicleId);
        favoriteOptional.ifPresent(favoriteRepository::delete);
    }
}
