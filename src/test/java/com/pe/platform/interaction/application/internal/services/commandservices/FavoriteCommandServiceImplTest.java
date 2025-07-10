package com.pe.platform.interaction.application.internal.services.commandservices;

import com.pe.platform.interaction.application.internal.services.outbound.ExternalVehicleService;
import com.pe.platform.interaction.domain.model.aggregates.Favorite;
import com.pe.platform.interaction.infrastructure.persistence.jpa.FavoriteRepository;
import com.pe.platform.shared.domain.exceptions.VehicleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FavoriteCommandServiceImplTest {

    private FavoriteRepository favoriteRepository;
    private ExternalVehicleService externalVehicleService;
    private FavoriteCommandServiceImpl service;

    @BeforeEach
    void setUp() {
        favoriteRepository = mock(FavoriteRepository.class);
        externalVehicleService = mock(ExternalVehicleService.class);
        service = new FavoriteCommandServiceImpl(favoriteRepository, externalVehicleService);
    }

    @Test
    void addFavorite_shouldReturnExistingFavoriteIfPresent() {
        Long vehicleId = 1L;
        long profileId = 2L;
        Favorite favorite = new Favorite(vehicleId, profileId);

        ExternalVehicleService.VehicleData vehicleData = mock(ExternalVehicleService.VehicleData.class);
        when(externalVehicleService.getVehicleById(vehicleId)).thenReturn(vehicleData);
        when(favoriteRepository.findByProfileIdAndVehicleId(profileId, vehicleId)).thenReturn(Optional.of(favorite));

        Optional<Favorite> result = service.addFavorite(vehicleId, profileId);

        assertTrue(result.isPresent());
        assertEquals(favorite, result.get());
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    void addFavorite_shouldSaveAndReturnNewFavoriteIfNotPresent() {
        Long vehicleId = 1L;
        long profileId = 2L;
        Favorite favorite = new Favorite(vehicleId, profileId);

        ExternalVehicleService.VehicleData vehicleData = mock(ExternalVehicleService.VehicleData.class);
        when(externalVehicleService.getVehicleById(vehicleId)).thenReturn(vehicleData);
        when(favoriteRepository.findByProfileIdAndVehicleId(profileId, vehicleId)).thenReturn(Optional.empty());
        when(favoriteRepository.save(any(Favorite.class))).thenReturn(favorite);

        Optional<Favorite> result = service.addFavorite(vehicleId, profileId);

        assertTrue(result.isPresent());
        assertEquals(favorite, result.get());
        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void addFavorite_shouldThrowExceptionIfVehicleNotFound() {
        Long vehicleId = 1L;
        long profileId = 2L;

        when(externalVehicleService.getVehicleById(vehicleId)).thenReturn(null);

        assertThrows(VehicleNotFoundException.class, () -> service.addFavorite(vehicleId, profileId));
        verify(favoriteRepository, never()).save(any());
    }

    @Test
    void removeFavorite_shouldDeleteIfPresent() {
        Long vehicleId = 1L;
        long profileId = 2L;
        Favorite favorite = new Favorite(vehicleId, profileId);

        when(favoriteRepository.findByProfileIdAndVehicleId(profileId, vehicleId)).thenReturn(Optional.of(favorite));

        service.removeFavorite(vehicleId, profileId);

        verify(favoriteRepository).delete(favorite);
    }

    @Test
    void removeFavorite_shouldDoNothingIfNotPresent() {
        Long vehicleId = 1L;
        long profileId = 2L;

        when(favoriteRepository.findByProfileIdAndVehicleId(profileId, vehicleId)).thenReturn(Optional.empty());

        service.removeFavorite(vehicleId, profileId);

        verify(favoriteRepository, never()).delete(any());
    }
}