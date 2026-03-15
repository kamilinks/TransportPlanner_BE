package it.palatransport.planner.repository;

import it.palatransport.planner.model.TariffaConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

// Repository configurazione tariffe
@Repository
public interface TariffaConfigRepository extends JpaRepository<TariffaConfig, Long> {

    // Recupera l'unica configurazione esistente
    Optional<TariffaConfig> findFirstByOrderByIdAsc();
}
