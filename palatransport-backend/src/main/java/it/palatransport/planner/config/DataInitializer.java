package it.palatransport.planner.config;

import it.palatransport.planner.model.*;
import it.palatransport.planner.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * DATA INITIALIZER: seed idempotente del database PostgreSQL all'avvio.
 *
 * Questa classe implementa CommandLineRunner: il metodo run() viene eseguito
 * automaticamente da Spring Boot subito dopo l'avvio, quando il database è pronto.
 *
 * Esegue due operazioni idempotenti (sicure anche su riavvii successivi):
 *   1. CREA GLI UTENTI DI DEFAULT se non esistono ancora, in modo da poter
 *      fare il login al primo avvio su un database pulito.
 *   2. CREA LA CONFIGURAZIONE TARIFFE DI DEFAULT se la tabella è vuota,
 *      fornendo valori base necessari per il calcolo dei preventivi.
 *
 * Nessuna operazione viene ripetuta se i dati esistono già (check preventivo).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final TariffaConfigRepository tariffaConfigRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        log.info("========================================");
        log.info("  Inizializzazione db di base...");
        log.info("========================================");

        // Crea l'utente di default solo se non esiste già
        if (!userRepository.existsByEmail("mirko@mirko.it")) {
            userRepository.save(User.builder()
                    .email("mirko@mirko.it")
                    .password(passwordEncoder.encode("11"))
                    .nome("Mirko")
                    .cognome("Mameli")
                    .build());

            userRepository.save(User.builder()
                    .email("operatore@palatransport.it")
                    .password(passwordEncoder.encode("operatore123"))
                    .nome("Luca")
                    .cognome("Bianchi")
                    .build());

            log.info("Utenti creati: mirko@mirko.it, operatore@palatransport.it");
        }

        // Crea la configurazione tariffe di default necessaria perlomeno per avviare il tool
        if (tariffaConfigRepository.count() == 0) {
            tariffaConfigRepository.save(TariffaConfig.builder()
                    .tariffaBaseKm(1.15)
                    .maggiorazioneSabato(50.0)
                    .maggiorazioneDomenica(80.0)
                    .maggiorazioneBlue(30.0)
                    .maggiorazioneSosta(40.0)
                    .maggiorazioneFacchinaggio(25.0)
                    .maggiorazioneLavAgg(35.0)
                    .build());
            log.info("Configurazione tariffe creata (1.15 €/km)");
        }

        log.info("========================================");
        log.info("  Avvio Completato, persistenza abilitata!");
        log.info("========================================");
    }
}
