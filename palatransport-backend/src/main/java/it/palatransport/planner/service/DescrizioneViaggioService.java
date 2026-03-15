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

// Servizio per la gestione delle descrizioni predefinite utilizzabili nei viaggi
@Service
@RequiredArgsConstructor
public class DescrizioneViaggioService {

    private final DescrizioneViaggioRepository descrizioneViaggioRepository;
    private final DescrizioneViaggioMapper descrizioneViaggioMapper;

    // Recupera tutte le descrizioni viaggio configurate
    public List<DescrizioneViaggioResponse> getAll() {
        return descrizioneViaggioRepository.findAll()
                .stream()
                .map(descrizioneViaggioMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Recupera solo le descrizioni viaggio attualmente attive
    public List<DescrizioneViaggioResponse> getAllAttive() {
        return descrizioneViaggioRepository.findByAttivo(true)
                .stream()
                .map(descrizioneViaggioMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Recupera una descrizione specifica tramite il suo ID
    public DescrizioneViaggioResponse getById(Long id) {
        DescrizioneViaggio d = descrizioneViaggioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DescrizioneViaggio non trovata con id: " + id));
        return descrizioneViaggioMapper.toResponse(d);
    }

    // Registra una nuova descrizione di viaggio nel sistema
    public DescrizioneViaggioResponse create(DescrizioneViaggioRequest request) {
        DescrizioneViaggio d = descrizioneViaggioMapper.fromRequest(request);
        return descrizioneViaggioMapper.toResponse(descrizioneViaggioRepository.save(d));
    }

    // Aggiorna una descrizione di viaggio esistente
    public DescrizioneViaggioResponse update(Long id, DescrizioneViaggioRequest request) {
        DescrizioneViaggio esistente = descrizioneViaggioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DescrizioneViaggio non trovata con id: " + id));
        descrizioneViaggioMapper.updateFromRequest(request, esistente);
        return descrizioneViaggioMapper.toResponse(descrizioneViaggioRepository.save(esistente));
    }

    // Rimuove una descrizione di viaggio dal sistema (soft-delete)
    public void delete(Long id) {
        DescrizioneViaggio d = descrizioneViaggioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("DescrizioneViaggio non trovata con id: " + id));
        descrizioneViaggioRepository.delete(d);
    }
}
