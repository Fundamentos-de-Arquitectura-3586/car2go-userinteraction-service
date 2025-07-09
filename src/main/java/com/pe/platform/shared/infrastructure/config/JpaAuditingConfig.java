package com.pe.platform.shared.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
    // Esta configuración habilita la auditoría automática de JPA
    // para que @CreatedDate y @LastModifiedDate funcionen correctamente
}
