package it.palatransport.planner.repository;

import it.palatransport.planner.model.Veicolo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// Repository veicoli
@Repository
public interface VeicoloRepository extends JpaRepository<Veicolo, Long> {

    // Trova per targa
    Optional<Veicolo> findByTarga(String targa);

    // Trova per tipo (discesa/salita)
    List<Veicolo> findByTipo(Veicolo.TipoVeicolo tipo);

    // Verifica esistenza targa
    boolean existsByTarga(String targa);
}
