package it.palatransport.planner.repository;

import it.palatransport.planner.model.Autista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 * REPOSITORY: AutistaRepository
 *
 * Estende JpaRepository<Autista, Long> → ci dà tutti i metodi CRUD base.
 * I metodi personalizzati qui sotto vengono generati automaticamente da Spring Data
 * interpretando il nome del metodo.
 */
@Repository
public interface AutistaRepository extends JpaRepository<Autista, Long> {

    /**
     * Trova tutti gli autisti per stato attivo/inattivo.
     * Utile per il filtro nella lista autisti del frontend.
     * SQL generato: SELECT * FROM autisti WHERE attivo = ?
     */
    List<Autista> findByAttivo(boolean attivo);

    /**
     * Verifica l'esistenza di un autista con stesso nome e cognome.
     * Usato per evitare duplicati.
     */
    boolean existsByNomeAndCognome(String nome, String cognome);
}
