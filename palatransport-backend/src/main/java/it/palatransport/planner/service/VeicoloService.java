package it.palatransport.planner.service;

import it.palatransport.planner.dto.VeicoloRequest;
import it.palatransport.planner.dto.VeicoloResponse;
import it.palatransport.planner.mapper.VeicoloMapper;
import it.palatransport.planner.model.Veicolo;
import it.palatransport.planner.repository.VeicoloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VeicoloService {

    private final VeicoloRepository veicoloRepository;
    private final VeicoloMapper veicoloMapper;

    public List<VeicoloResponse> getAll() {
        return veicoloRepository.findAll()
                .stream()
                .map(veicoloMapper::toResponse)
                .collect(Collectors.toList());
    }

    public VeicoloResponse getById(Long id) {
        Veicolo v = veicoloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veicolo non trovato con id: " + id));
        return veicoloMapper.toResponse(v);
    }

    public VeicoloResponse create(VeicoloRequest request) {
        Veicolo v = veicoloMapper.fromRequest(request);
        return veicoloMapper.toResponse(veicoloRepository.save(v));
    }

    public VeicoloResponse update(Long id, VeicoloRequest request) {
        Veicolo esistente = veicoloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veicolo non trovato con id: " + id));
        veicoloMapper.updateFromRequest(request, esistente);
        return veicoloMapper.toResponse(veicoloRepository.save(esistente));
    }

    public void delete(Long id) {
        Veicolo v = veicoloRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veicolo non trovato con id: " + id));
        veicoloRepository.delete(v);
    }
}
