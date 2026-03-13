package it.palatransport.planner.service;

import it.palatransport.planner.dto.AutistaRequest;
import it.palatransport.planner.dto.AutistaResponse;
import it.palatransport.planner.mapper.AutistaMapper;
import it.palatransport.planner.model.Autista;
import it.palatransport.planner.repository.AutistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * SERVICE: AutistaService
 *
 * Refactored con MapStruct: i metodi toResponse() e fromRequest() scritti a mano
 * sono stati eliminati e sostituiti con le chiamate al mapper generato automaticamente.
 *
 * Prima (manuale):
 *   private AutistaResponse toResponse(Autista a) { return AutistaResponse.builder()... }
 *
 * Dopo (MapStruct):
 *   autistaMapper.toResponse(a)  ← MapStruct ha generato lo stesso codice per noi
 */
@Service
@RequiredArgsConstructor
public class AutistaService {

    private final AutistaRepository autistaRepository;
    private final AutistaMapper autistaMapper; // ← iniettato da Spring (generato da MapStruct)

    public List<AutistaResponse> getAll() {
        return autistaRepository.findAll()
                .stream()
                .map(autistaMapper::toResponse) // ← prima era: map(this::toResponse)
                .collect(Collectors.toList());
    }

    public AutistaResponse getById(Long id) {
        Autista autista = autistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autista non trovato con id: " + id));
        return autistaMapper.toResponse(autista);
    }

    public AutistaResponse create(AutistaRequest request) {
        Autista autista = autistaMapper.fromRequest(request); // ← prima: fromRequest(request, new Autista())
        return autistaMapper.toResponse(autistaRepository.save(autista));
    }

    public AutistaResponse update(Long id, AutistaRequest request) {
        Autista esistente = autistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autista non trovato con id: " + id));
        autistaMapper.updateFromRequest(request, esistente); // ← aggiorna l'entity esistente in-place
        return autistaMapper.toResponse(autistaRepository.save(esistente));
    }

    public void delete(Long id) {
        Autista autista = autistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autista non trovato con id: " + id));
        autistaRepository.delete(autista);
    }
}
