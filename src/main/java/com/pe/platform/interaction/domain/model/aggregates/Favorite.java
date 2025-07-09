package com.pe.platform.interaction.domain.model.aggregates;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "favorites")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "vehicle_id", nullable = false)
    private Long vehicleId;

    @Column(nullable = false)
    private long profileId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Favorite() {

        this.createdAt = LocalDateTime.now();
        

    }

    public Favorite(Long vehicleId, long profileId) {
        this.vehicleId = vehicleId;
        this.profileId = profileId;
        this.createdAt = LocalDateTime.now();
    }
}
