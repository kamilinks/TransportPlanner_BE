package it.palatransport.planner.repository;

import it.palatransport.planner.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * REPOSITORY: UserRepository
 *
 * Un Repository in Spring Data JPA è un'interfaccia (non una classe!).
 * Estendendo JpaRepository<User, Long> otteniamo GRATIS e AUTOMATICAMENTE
 * decine di metodi per lavorare col database, senza scrivere una riga di SQL:
 *
 *   - save(user)          → INSERT o UPDATE nel database
 *   - findById(id)        → SELECT * FROM users WHERE id = ?
 *   - findAll()           → SELECT * FROM users
 *   - delete(user)        → DELETE FROM users WHERE id = ?
 *   - count()             → SELECT COUNT(*) FROM users
 *   - existsById(id)      → SELECT id FROM users WHERE id = ?  (restituisce boolean)
 *   ... e molti altri ancora
 *
 * JpaRepository<User, Long>:
 *   - Primo parametro (User): la classe Entity che gestisce
 *   - Secondo parametro (Long): il tipo della chiave primaria (id)
 *
 * METODI PERSONALIZZATI: possiamo aggiungere metodi semplicemente
 * nominandoli in modo conveniente: Spring capisce automaticamente la query!
 * Es: findByEmail → SELECT * FROM users WHERE email = ?
 *     findByNomeAndCognome → SELECT * FROM users WHERE nome = ? AND cognome = ?
 *     existsByEmail → SELECT COUNT(*) > 0 FROM users WHERE email = ?
 *
 * Questa "magia" si chiama "Query Derivation" e funziona seguendo le convenzioni
 * dei nomi dei metodi.
 *
 * @Repository → "marker" che dice a Spring che questa interfaccia è un componente
 *   del livello dati. In Spring Data JPA è facoltativo (JpaRepository lo implica già),
 *   ma è buona pratica includerlo per chiarezza.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Trova un utente per email.
     * Usato dal sistema di autenticazione per verificare le credenziali.
     *
     * Optional<User>: un contenitore che può contenere o meno un valore.
     * Se l'utente non esiste, restituisce Optional.empty() invece di null.
     * Questo evita il temuto NullPointerException.
     */
    Optional<User> findByEmail(String email);

    /**
     * Controlla se esiste già un utente con quella email.
     * Usato al momento della registrazione per evitare duplicati.
     */
    boolean existsByEmail(String email);
}
