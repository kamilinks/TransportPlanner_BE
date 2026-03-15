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

// Servizio gestione autisti
@Service
@RequiredArgsConstructor
public class AutistaService {

    private final AutistaRepository autistaRepository;
    private final AutistaMapper autistaMapper;

    // Elenco autisti
    public List<AutistaResponse> getAll() {
        return autistaRepository.findAll()
                .stream()
                .map(autistaMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Ricerca autista per ID
    public AutistaResponse getById(Long id) {
        Autista autista = autistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autista non trovato con id: " + id));
        return autistaMapper.toResponse(autista);
    }

    // Creazione autista
    public AutistaResponse create(AutistaRequest request) {
        Autista autista = autistaMapper.fromRequest(request);
        return autistaMapper.toResponse(autistaRepository.save(autista));
    }

    // Aggiornamento autista
    public AutistaResponse update(Long id, AutistaRequest request) {
        Autista esistente = autistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autista non trovato con id: " + id));
        autistaMapper.updateFromRequest(request, esistente);
        return autistaMapper.toResponse(autistaRepository.save(esistente));
    }

    // Eliminazione autista
    public void delete(Long id) {
        Autista autista = autistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Autista non trovato con id: " + id));
        autistaRepository.delete(autista);
    }
}
