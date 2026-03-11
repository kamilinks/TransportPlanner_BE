package it.palatransport.planner.config;

import it.palatransport.planner.model.*;
import it.palatransport.planner.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * DATA INITIALIZER: carica i dati demo all'avvio dell'applicazione.
 *
 * Questa classe implementa CommandLineRunner: il metodo run() viene eseguito
 * automaticamente da Spring Boot subito dopo che l'applicazione è partita
 * e il database è pronto.
 *
 * Serve a due scopi:
 *   1. PRE-POPOLARE il database H2 (in memoria) con dati di default
 *      ogni volta che l'applicazione si avvia.
 *      (Con H2 "create-drop" i dati vengono persi ad ogni riavvio)
 *   2. CREARE l'utente di default per poter fare il login subito.
 *
 * IMPORTANTE: Questi dati rispecchiano quelli finti (mock) del frontend Angular,
 *   quindi il comportamento dell'app sarà identico a prima, ma ora i dati
 *   vengono dal database H2 invece che dalla memoria del browser.
 *
 * @Component → Spring gestisce questa classe come Bean.
 * @Slf4j (Lombok) → genera automaticamente il logger:
 *   private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
 *   Usiamo log.info() per stampare messaggi nel terminale all'avvio.
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
