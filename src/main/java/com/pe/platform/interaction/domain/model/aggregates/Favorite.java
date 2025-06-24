package com.pe.platform.interaction.domain.model.aggregates;

import com.pe.platform.vehicle.domain.model.aggregates.Vehicle; // Si tienes el vehículo en otro módulo
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "favorites")
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(nullable = false)
    private long profileId;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    public Favorite() {

    }

    public Favorite(Vehicle vehicle, long profileId) {
        this.vehicle = vehicle;
        this.profileId = profileId;
        this.createdAt = LocalDateTime.now();
    }
}
