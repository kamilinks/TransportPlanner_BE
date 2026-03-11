package it.palatransport.planner.repository;

import it.palatransport.planner.model.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

/**
 * REPOSITORY: ViaggioRepository
 *
 * Repository per l'entità Viaggio. Oltre ai metodi CRUD standard,
 * qui aggiungiamo query personalizzate più complesse.
 */
@Repository
public interface ViaggioRepository extends JpaRepository<Viaggio, Long> {

    /**
     * Trova tutti i viaggi di un autista specifico.
     * ATTENZIONE: il campo dell'entità è "autista" (un oggetto Autista),
     * non "autistaId". Spring Data capisce la navigazione: "ByAutistaId"
     * significa "WHERE autista.id = ?".
     */
    List<Viaggio> findByAutistaId(Long autistaId);

    /**
     * Trova i viaggi in un intervallo di date.
     * "Between" → WHERE data BETWEEN ? AND ?
     */
    List<Viaggio> findByDataBetween(LocalDate dataInizio, LocalDate dataFine);

    /**
     * Trova i viaggi di un autista specifico in un range di date.
     * Combinazione di criteri: AND implicito nel nome del metodo.
     */
    List<Viaggio> findByAutistaIdAndDataBetween(Long autistaId, LocalDate dataInizio, LocalDate dataFine);

    /**
     * JPQL (Java Persistence Query Language): query scritte a mano.
     * A volte il "metodo derivato" non basta: usiamo @Query con JPQL.
     *
     * JPQL è simile a SQL, ma lavora sulle CLASSI JAVA, non sulle tabelle:
     *   - "v" è un alias per l'entità Viaggio
     *   - "v.autista.id" naviga la relazione: accede all'id dell'autista
     *
     * Qui recuperiamo i viaggi di un autista in un mese specifico
     * (per l'agenda mensile del frontend).
     *
     * @Param("autistaId") lega il parametro del metodo al :autistaId nella query.
     */
    @Query("SELECT v FROM Viaggio v WHERE v.autista.id = :autistaId " +
           "AND YEAR(v.data) = :anno AND MONTH(v.data) = :mese " +
           "ORDER BY v.data ASC")
    List<Viaggio> findByAutistaIdAndMese(
            @Param("autistaId") Long autistaId,
            @Param("anno") int anno,
            @Param("mese") int mese
    );
}
