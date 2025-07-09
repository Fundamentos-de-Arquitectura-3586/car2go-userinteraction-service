package com.pe.platform.interaction.interfaces;

import com.pe.platform.interaction.domain.model.aggregates.Favorite;
import com.pe.platform.interaction.domain.services.FavoriteCommandService;
import com.pe.platform.interaction.infrastructure.persistence.jpa.FavoriteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Favorites", description = "API para gestionar vehículos favoritos")
public class FavoriteController {

    private final FavoriteCommandService favoriteCommandService;
    private final FavoriteRepository favoriteRepository;

    public FavoriteController(FavoriteCommandService favoriteCommandService, FavoriteRepository favoriteRepository) {
        this.favoriteCommandService = favoriteCommandService;
        this.favoriteRepository = favoriteRepository;
    }


    @Operation(summary = "Agregar vehículo a favoritos", 
               description = "Permite a un comprador agregar un vehículo a su lista de favoritos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Vehículo agregado a favoritos exitosamente"),
        @ApiResponse(responseCode = "400", description = "Error al agregar a favoritos"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PreAuthorize("hasAuthority('ROLE_BUYER')")
    @PostMapping("/{vehicleId}")
    public ResponseEntity<Favorite> addFavorite(
            @Parameter(description = "ID del vehículo a agregar a favoritos", required = true)
            @PathVariable Long vehicleId) {
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


    @Operation(summary = "Remover vehículo de favoritos", 
               description = "Permite a un comprador remover un vehículo de su lista de favoritos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Vehículo removido de favoritos exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
    @PreAuthorize("hasAuthority('ROLE_BUYER')")
    @DeleteMapping("/{vehicleId}")
    public ResponseEntity<Void> removeFavorite(
            @Parameter(description = "ID del vehículo a remover de favoritos", required = true)
            @PathVariable Long vehicleId) {
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


    @Operation(summary = "Obtener mis favoritos", 
               description = "Obtiene la lista de vehículos favoritos del usuario autenticado")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de favoritos obtenida exitosamente"),
        @ApiResponse(responseCode = "401", description = "No autorizado"),
        @ApiResponse(responseCode = "403", description = "Acceso denegado")
    })
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
