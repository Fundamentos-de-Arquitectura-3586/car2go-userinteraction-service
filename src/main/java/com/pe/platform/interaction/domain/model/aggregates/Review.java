package com.pe.platform.interaction.domain.model.aggregates;

import com.pe.platform.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "reviews")
public class Review extends AuditableAbstractAggregateRoot<Review> {
    @Column(nullable = false)
    private Long vehicleId;
    @Column(nullable = false)
    private Long profileId;
    @Column(nullable = false)
    private int rating;
    @Column(length = 500)
    private String comment;

    public Review() {

    }

    public Review(Long vehicleId, Long profileId, int rating, String comment) {
        this.vehicleId = vehicleId;
        this.profileId = profileId;
        this.rating = rating;
        this.comment = comment;
        // Inicializar campos de auditoría temporalmente hasta que JPA Auditing esté configurado
        java.util.Date now = new java.util.Date();
        this.setCreatedAt(now);
        this.setUpdatedAt(now);
    }
}
