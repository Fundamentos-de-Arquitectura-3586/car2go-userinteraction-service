package com.pe.platform.interaction.interfaces;

import com.pe.platform.interaction.domain.model.aggregates.Favorite;
import com.pe.platform.interaction.domain.services.FavoriteCommandService;
import com.pe.platform.interaction.infrastructure.persistence.jpa.FavoriteRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/api/v1/favorites")
public class FavoriteController {

    private final FavoriteCommandService favoriteCommandService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteController(FavoriteCommandService favoriteCommandService, FavoriteRepository favoriteRepository) {
        this.favoriteCommandService = favoriteCommandService;
        this.favoriteRepository = favoriteRepository;
    }


    @PreAuthorize("hasAuthority('ROLE_BUYER')")
    @PostMapping("/{vehicleId}")
    public ResponseEntity<Favorite> addFavorite(@PathVariable Long vehicleId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        long userId = (Long) authentication.getPrincipal();

        try {
            Optional<Favorite> favorite = favoriteCommandService.addFavorite(vehicleId, userId);
            return favorite.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.badRequest().build());
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).build();
        }


    }


    @PreAuthorize("hasAuthority('ROLE_BUYER')")
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> removeFavorite(@PathVariable Long vehicleId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).build();
        }

        long userId = (Long) authentication.getPrincipal();

        try {
            favoriteCommandService.removeFavorite(vehicleId, userId);
            return ResponseEntity.noContent().build();
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).build();
        }
    }


    @PreAuthorize("hasAuthority('ROLE_BUYER')")
    @GetMapping("/my-favorites")
    public ResponseEntity<List<Favorite>> getMyFavorites() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        long userId = (Long) authentication.getPrincipal();

        try {
            List<Favorite> favorites = favoriteRepository.findByProfileId(userId);
            return ResponseEntity.ok(favorites);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(403).build();
        }

    }
}
