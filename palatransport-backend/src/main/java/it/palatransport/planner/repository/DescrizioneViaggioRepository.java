package it.palatransport.planner.repository;

import it.palatransport.planner.model.DescrizioneViaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * REPOSITORY: DescrizioneViaggioRepository
 */
@Repository
public interface DescrizioneViaggioRepository extends JpaRepository<DescrizioneViaggio, Long> {

    /** Trova per codice univoco (es. "CARICO") */
    Optional<DescrizioneViaggio> findByCodice(String codice);

    /** Solo le descrizioni attive (per il dropdown nel frontend) */
    List<DescrizioneViaggio> findByAttivo(boolean attivo);
}
