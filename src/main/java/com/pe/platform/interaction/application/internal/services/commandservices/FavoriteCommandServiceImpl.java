package com.pe.platform.interaction.application.internal.services.commandservices;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.pe.platform.interaction.application.internal.services.outbound.ExternalVehicleService;
import com.pe.platform.interaction.domain.model.aggregates.Favorite;
import com.pe.platform.interaction.domain.services.FavoriteCommandService;
import com.pe.platform.interaction.infrastructure.persistence.jpa.FavoriteRepository;
import com.pe.platform.shared.domain.exceptions.VehicleNotFoundException;

@Service
public class FavoriteCommandServiceImpl implements FavoriteCommandService {
    
    private final FavoriteRepository favoriteRepository;
    private final ExternalVehicleService externalVehicleService;

    public FavoriteCommandServiceImpl(FavoriteRepository favoriteRepository, ExternalVehicleService externalVehicleService) {
        this.favoriteRepository = favoriteRepository;
        this.externalVehicleService = externalVehicleService;

    }

    @Override
    public Optional<Favorite> addFavorite(Long vehicleId, long profileId) {

        // Check if the vehicle exists
        if(externalVehicleService.getVehicleById(vehicleId) == null){
            System.out.println("Vehicle not found: " + externalVehicleService.getVehicleById(vehicleId));
            throw new VehicleNotFoundException(vehicleId);
        }

        Optional<Favorite> existingFavorite = favoriteRepository.findByProfileIdAndVehicleId(profileId, vehicleId);
        if (existingFavorite.isPresent()) {
            return existingFavorite;
        }

        Favorite newFavorite = new Favorite(vehicleId, profileId);

        return Optional.of(favoriteRepository.save(newFavorite));
    }

    @Override
    public void removeFavorite(Long vehicleId, long profileId) {
        Optional<Favorite> favoriteOptional = favoriteRepository.findByProfileIdAndVehicleId(profileId, vehicleId);
        favoriteOptional.ifPresent(favoriteRepository::delete);
    }
}
