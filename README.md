# PALATRANSPORT - Transport Planner & Agenda

Benvenuto in **PalaTransport**, una soluzione professionale Full Stack per la gestione dei viaggi, la pianificazione degli autisti e il calcolo automatico delle tariffe basato sui km percorsi.

Il progetto è stato sviluppato con un'architettura moderna e scalabile, pensata per gestire volumi di dati enterprise con elevati standard di sicurezza e manutenibilità.

---

## 🚀 Tecnologie Utilizzate

### Backend (Java/Spring Boot)
*   **Spring Boot 3.2**: Core framework per le API REST.
*   **Hibernate & Spring Data JPA**: Per la gestione della persistenza.
*   **PostgreSQL**: Database relazionale ad alte prestazioni.
*   **Spring Security & JWT**: Autenticazione Stateless basata su token.
*   **MapStruct**: Per il mapping performante tra Entity e DTO.
*   **Lombok**: Per ridurre il codice boilerplate.
*   **Maven**: Gestione delle dipendenze e build.

### Frontend (Angular)
*   **Angular 17**: Framework per la Single Page Application (SPA).
*   **PrimeNG**: Suite di componenti UI premium.
*   **RxJS**: Gestione reattiva dello stato e degli stream asincroni.
*   **Chart.js**: Visualizzazione dati e analisi statistiche.

### Infrastruttura
*   **Docker & Docker Compose**: Containerizzazione completa dello stack.
*   **Nginx**: Web server per il serving dei file statici.

---

## 🏗️ Highlight Tecnici (Middle-Senior Level)

*   **Paginazione Server-side**: Gestione scalabile di grandi moli di dati tramite l'interazione tra Pageable di Spring e Lazy Loading del frontend.
*   **Soft Delete**: Implementazione della cancellazione logica (`deleted = true`) per proteggere l'integrità dei dati storici.
*   **RBAC (Role-Based Access Control)**: Gestione differenziata dei permessi tra `ADMIN` (gestione tariffe e configurazioni) e `USER` (operatività).
*   **Domain-Driven Exceptions**: Gestione centralizzata degli errori tramite un `GlobalExceptionHandler` nel backend e un `ErrorInterceptor` nel frontend.
*   **Audit Logging**: Tracciamento automatico di creazione e modifica dei record tramite JPA Auditing.

---

## 🛠️ Come Avviare il Progetto

Il modo più veloce per testare l'intera suite è utilizzare **Docker Compose**.

### Prerequisiti
*   [Docker Desktop](https://www.docker.com/products/docker-desktop) installato e avviato.

### Comandi
1.  Apri il terminale nella cartella root del progetto.
2.  Esegui il comando:
    ```bash
    docker-compose up --build
    ```
3.  Una volta avviato, potrai accedere a:
    *   **Frontend**: [http://localhost](http://localhost)
    *   **Swagger API Docs**: [http://localhost:8081/swagger-ui.html](http://localhost:8081/swagger-ui.html)

### Credenziali di Accesso (Default)
*   **Admin**: `mirko@mirko.it` / Password: `11`
*   **User**: `operatore@palatransport.it` / Password: `operatore123`

---

## 📈 Roadmap & Sviluppi Futuri
- [ ] Integrazione con provider GPS esterni (OSRM/OpenRouteService) per il routing intelligente.
- [ ] Export dei report mensili in formato PDF/Excel.
- [ ] Notifiche in tempo reale tramite WebSocket.

---
*Progetto sviluppato da Mirko Mameli.*
