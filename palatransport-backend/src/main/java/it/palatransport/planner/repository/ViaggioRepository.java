package it.palatransport.planner.repository;

import it.palatransport.planner.model.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

// Repository viaggi
@Repository
public interface ViaggioRepository extends JpaRepository<Viaggio, Long> {

    // Viaggi per autista
    List<Viaggio> findByAutistaId(Long autistaId);

    // Viaggi per intervallo date
    List<Viaggio> findByDataBetween(LocalDate dataInizio, LocalDate dataFine);

    // Viaggi per autista e date
    List<Viaggio> findByAutistaIdAndDataBetween(Long autistaId, LocalDate dataInizio, LocalDate dataFine);

    // Viaggi per autista e mese/anno (query custom)
    @Query("SELECT v FROM Viaggio v WHERE v.autista.id = :autistaId " +
           "AND YEAR(v.data) = :anno AND MONTH(v.data) = :mese " +
           "ORDER BY v.data ASC")
    List<Viaggio> findByAutistaIdAndMese(
            @Param("autistaId") Long autistaId,
            @Param("anno") int anno,
            @Param("mese") int mese
    );
}
