package it.palatransport.planner.repository;

import it.palatransport.planner.model.DescrizioneViaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

// Repository descrizioni viaggi
@Repository
public interface DescrizioneViaggioRepository extends JpaRepository<DescrizioneViaggio, Long> {

    // Trova per codice univoco
    Optional<DescrizioneViaggio> findByCodice(String codice);

    // Trova per stato attivo
    List<DescrizioneViaggio> findByAttivo(boolean attivo);
}
