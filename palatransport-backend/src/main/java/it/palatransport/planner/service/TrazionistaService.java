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

@Service
@RequiredArgsConstructor
public class TrazionistaService {

    private final TrazionistaRepository trazionistaRepository;
    private final TrazionistaMapper trazionistaMapper;

    public List<TrazionistaResponse> getAll() {
        return trazionistaRepository.findAll()
                .stream()
                .map(trazionistaMapper::toResponse)
                .collect(Collectors.toList());
    }

    public TrazionistaResponse getById(Long id) {
        Trazionista t = trazionistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trazionista non trovato con id: " + id));
        return trazionistaMapper.toResponse(t);
    }

    public TrazionistaResponse create(TrazionistaRequest request) {
        Trazionista t = trazionistaMapper.fromRequest(request);
        return trazionistaMapper.toResponse(trazionistaRepository.save(t));
    }

    public TrazionistaResponse update(Long id, TrazionistaRequest request) {
        Trazionista esistente = trazionistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trazionista non trovato con id: " + id));
        trazionistaMapper.updateFromRequest(request, esistente);
        return trazionistaMapper.toResponse(trazionistaRepository.save(esistente));
    }

    public void delete(Long id) {
        Trazionista t = trazionistaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Trazionista non trovato con id: " + id));
        trazionistaRepository.delete(t);
    }
}
