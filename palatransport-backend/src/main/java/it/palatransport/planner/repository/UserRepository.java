package it.palatransport.planner.repository;

import it.palatransport.planner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // Trova per email
    Optional<User> findByEmail(String email);

    // Verifica esistenza email
    boolean existsByEmail(String email);
}
