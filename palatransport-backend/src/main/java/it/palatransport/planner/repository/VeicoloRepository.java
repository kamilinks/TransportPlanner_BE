package it.palatransport.planner.repository;

import it.palatransport.planner.model.Veicolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * REPOSITORY: VeicoloRepository
 */
@Repository
public interface VeicoloRepository extends JpaRepository<Veicolo, Long> {

    /** Trova veicolo per targa (deve essere unica) */
    Optional<Veicolo> findByTarga(String targa);

    /** Trova tutti i veicoli di un tipo specifico (SALITA o DISCESA) */
    List<Veicolo> findByTipo(Veicolo.TipoVeicolo tipo);

    /** Controlla se esiste già un veicolo con quella targa */
    boolean existsByTarga(String targa);
}
