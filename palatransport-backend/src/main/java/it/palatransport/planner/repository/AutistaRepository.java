package it.palatransport.planner.repository;

import it.palatransport.planner.model.Autista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Repository autisti
@Repository
public interface AutistaRepository extends JpaRepository<Autista, Long> {

    // Trova per stato attivo
    List<Autista> findByAttivo(boolean attivo);

    // Verifica duplicati per nome e cognome
    boolean existsByNomeAndCognome(String nome, String cognome);
}
