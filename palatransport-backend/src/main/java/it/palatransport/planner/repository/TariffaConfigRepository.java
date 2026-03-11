package it.palatransport.planner.repository;

import it.palatransport.planner.model.TariffaConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * REPOSITORY: TariffaConfigRepository
 *
 * Questa tabella contiene sempre e solo UNA riga (la configurazione attuale).
 * Per recuperarla usiamo findFirst() che restituisce il primo (e unico)
 * record trovato.
 */
@Repository
public interface TariffaConfigRepository extends JpaRepository<TariffaConfig, Long> {

    /**
     * Recupera la prima (e unica) configurazione tariffe presente nel database.
     * SQL generato: SELECT * FROM tariffa_config LIMIT 1
     */
    Optional<TariffaConfig> findFirstByOrderByIdAsc();
}
