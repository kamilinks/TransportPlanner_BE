package it.palatransport.planner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * CLASSE PRINCIPALE DELL'APPLICAZIONE.
 *
 * @SpringBootApplication è una "meta-annotazione" che combina tre annotazioni:
 *   - @SpringBootConfiguration: indica che questa è la classe di configurazione principale
 *   - @EnableAutoConfiguration: dice a Spring di configurare automaticamente i componenti
 *     in base alle dipendenze trovate nel classpath (es. vede H2 → configura il DataSource)
 *   - @ComponentScan: dice a Spring di cercare tutti i componenti (@Service, @Repository,
 *     @Controller, ecc.) nel package corrente e in tutti i suoi sotto-package
 *
 * Il metodo main() è il punto di ingresso standard Java. Chiama SpringApplication.run()
 * che avvia il server Tomcat integrato e tutta l'applicazione Spring.
 */
@SpringBootApplication
public class PalatransportBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(PalatransportBackendApplication.class, args);
    }
}
