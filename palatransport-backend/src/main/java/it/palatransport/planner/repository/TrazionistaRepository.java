package it.palatransport.planner.repository;

import it.palatransport.planner.model.Trazionista;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrazionistaRepository extends JpaRepository<Trazionista, Long> {
}
