package it.palatransport.planner.service;

import it.palatransport.planner.dto.TrazionistaRequest;
import it.palatransport.planner.dto.TrazionistaResponse;
import it.palatransport.planner.mapper.TrazionistaMapper;
import it.palatransport.planner.model.Trazionista;
import it.palatransport.planner.repository.TrazionistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// Servizio per la gestione dell'anagrafica dei trazionisti
@Service
@RequiredArgsConstructor
public class TrazionistaService {

    private final TrazionistaRepository trazionistaRepository;
    private final TrazionistaMapper trazionistaMapper;

    // Recupera tutti i trazionisti registrati
    public List<TrazionistaResponse> getAll() {
        return trazionistaRepository.findAll()
                .stream()
                .map(trazionistaMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Recupera un singolo trazionista tramite suo ID
    public TrazionistaResponse getById(Long id) {
        Trazionista t = trazionistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trazionista non trovato con id: " + id));
        return trazionistaMapper.toResponse(t);
    }

    // Crea un nuovo record trazionista
    public TrazionistaResponse create(TrazionistaRequest request) {
        Trazionista t = trazionistaMapper.fromRequest(request);
        return trazionistaMapper.toResponse(trazionistaRepository.save(t));
    }

    // Aggiorna le informazioni di un trazionista esistente
    public TrazionistaResponse update(Long id, TrazionistaRequest request) {
        Trazionista esistente = trazionistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trazionista non trovato con id: " + id));
        trazionistaMapper.updateFromRequest(request, esistente);
        return trazionistaMapper.toResponse(trazionistaRepository.save(esistente));
    }

    // Elimina un trazionista dal sistema
    public void delete(Long id) {
        Trazionista t = trazionistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trazionista non trovato con id: " + id));
        trazionistaRepository.delete(t);
    }
}
