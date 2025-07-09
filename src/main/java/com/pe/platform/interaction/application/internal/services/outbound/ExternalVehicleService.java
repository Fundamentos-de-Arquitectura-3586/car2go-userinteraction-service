package com.pe.platform.interaction.application.internal.services.outbound;

import com.pe.platform.shared.infrastructure.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;



@Service
public class ExternalVehicleService {
    
    @Autowired
    private WebClient.Builder webClientBuilder;
    
    // Nombre del servicio registrado en Eureka
    private static final String VEHICLE_SERVICE_NAME = "vehicle-service";

    /**
     * Validates if vehicle exists by calling Vehicle Service through Service Discovery
     * @param vehicleId The vehicle ID to validate
     * @return true if vehicle exists, false otherwise
     */
    public boolean validateVehicleExists(Long vehicleId) {
        try {
            String jwtToken = JwtTokenUtil.getCurrentJwtTokenWithBearer();
            
            WebClient.RequestHeadersSpec<?> request = webClientBuilder.build()
                .get()
                .uri("http://" + VEHICLE_SERVICE_NAME + "/api/v1/vehicle/{id}", vehicleId);
            
            // Agregar el JWT token si está disponible
            if (jwtToken != null) {
                request = request.header("Authorization", jwtToken);
            }
            
            request.retrieve()
                .bodyToMono(Boolean.class)
                .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets vehicle information through Service Discovery
     * @param vehicleId The vehicle ID
     * @return Vehicle data or null if not found
     */
    public VehicleData getVehicleById(Long vehicleId) {
        try {
            String jwtToken = JwtTokenUtil.getCurrentJwtTokenWithBearer();
            
            WebClient.RequestHeadersSpec<?> request = webClientBuilder.build()
                .get()
                .uri("http://" + VEHICLE_SERVICE_NAME + "/api/v1/vehicle/{id}", vehicleId);
            
            // Agregar el JWT token si está disponible
            if (jwtToken != null) {
                request = request.header("Authorization", jwtToken);
            }
            
            return request.retrieve()
                .bodyToMono(VehicleData.class)
                .block();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Validates if vehicle exists by calling Vehicle Service with explicit JWT token
     * @param vehicleId The vehicle ID to validate
     * @param jwtToken The JWT token for authorization
     * @return true if vehicle exists, false otherwise
     */
    public boolean validateVehicleExists(Long vehicleId, String jwtToken) {
        try {
            WebClient.RequestHeadersSpec<?> request = webClientBuilder.build()
                .get()
                .uri("http://" + VEHICLE_SERVICE_NAME + "/api/v1/vehicle/{id}", vehicleId);
            
            // Agregar el JWT token
            if (jwtToken != null && !jwtToken.isEmpty()) {
                String bearerToken = jwtToken.startsWith("Bearer ") ? jwtToken : "Bearer " + jwtToken;
                request = request.header("Authorization", bearerToken);
            }
            
            request.retrieve()
                .bodyToMono(Boolean.class)
                .block();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Gets vehicle information with explicit JWT token
     * @param vehicleId The vehicle ID
     * @param jwtToken The JWT token for authorization
     * @return Vehicle data or null if not found
     */
    public VehicleData getVehicleById(Long vehicleId, String jwtToken) {
        try {
            WebClient.RequestHeadersSpec<?> request = webClientBuilder.build()
                .get()
                .uri("http://" + VEHICLE_SERVICE_NAME + "/api/v1/vehicle/{id}", vehicleId);
            
            // Agregar el JWT token
            if (jwtToken != null && !jwtToken.isEmpty()) {
                String bearerToken = jwtToken.startsWith("Bearer ") ? jwtToken : "Bearer " + jwtToken;
                request = request.header("Authorization", bearerToken);
            }
            
            return request.retrieve()
                .bodyToMono(VehicleData.class)
                .block();
        } catch (Exception e) {
            return null;
        }
    }

    // DTO para recibir datos del vehículo
    public static class VehicleData {
        private Long id;
        private String model;
        private Long profileId;
        private String status;
        
        // Getters y setters
        public Long getId() { return id; }
        public void setId(Long id) { this.id = id; }
        public String getModel() { return model; }
        public void setModel(String model) { this.model = model; }
        public Long getProfileId() { return profileId; }
        public void setProfileId(Long profileId) { this.profileId = profileId; }
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
}