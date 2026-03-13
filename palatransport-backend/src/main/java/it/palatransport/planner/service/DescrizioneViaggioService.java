package it.palatransport.planner.service;

import it.palatransport.planner.dto.DescrizioneViaggioRequest;
import it.palatransport.planner.dto.DescrizioneViaggioResponse;
import it.palatransport.planner.mapper.DescrizioneViaggioMapper;
import it.palatransport.planner.model.DescrizioneViaggio;
import it.palatransport.planner.repository.DescrizioneViaggioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DescrizioneViaggioService {

    private final DescrizioneViaggioRepository descrizioneViaggioRepository;
    private final DescrizioneViaggioMapper descrizioneViaggioMapper;

    public List<DescrizioneViaggioResponse> getAll() {
        return descrizioneViaggioRepository.findAll()
                .stream()
                .map(descrizioneViaggioMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<DescrizioneViaggioResponse> getAllAttive() {
        return descrizioneViaggioRepository.findByAttivo(true)
                .stream()
                .map(descrizioneViaggioMapper::toResponse)
                .collect(Collectors.toList());
    }

    public DescrizioneViaggioResponse getById(Long id) {
        DescrizioneViaggio d = descrizioneViaggioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DescrizioneViaggio non trovata con id: " + id));
        return descrizioneViaggioMapper.toResponse(d);
    }

    public DescrizioneViaggioResponse create(DescrizioneViaggioRequest request) {
        DescrizioneViaggio d = descrizioneViaggioMapper.fromRequest(request);
        return descrizioneViaggioMapper.toResponse(descrizioneViaggioRepository.save(d));
    }

    public DescrizioneViaggioResponse update(Long id, DescrizioneViaggioRequest request) {
        DescrizioneViaggio esistente = descrizioneViaggioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DescrizioneViaggio non trovata con id: " + id));
        descrizioneViaggioMapper.updateFromRequest(request, esistente);
        return descrizioneViaggioMapper.toResponse(descrizioneViaggioRepository.save(esistente));
    }

    public void delete(Long id) {
        DescrizioneViaggio d = descrizioneViaggioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DescrizioneViaggio non trovata con id: " + id));
        descrizioneViaggioRepository.delete(d);
    }
}
