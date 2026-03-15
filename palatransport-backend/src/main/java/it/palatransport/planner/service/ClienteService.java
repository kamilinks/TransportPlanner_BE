package it.palatransport.planner.service;

import it.palatransport.planner.dto.ClienteRequest;
import it.palatransport.planner.dto.ClienteResponse;
import it.palatransport.planner.mapper.ClienteMapper;
import it.palatransport.planner.model.Cliente;
import it.palatransport.planner.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

// Servizio gestione clienti
@Service
@Transactional
@RequiredArgsConstructor
@lombok.extern.slf4j.Slf4j
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    // Elenco tutti i clienti
    public List<ClienteResponse> getAll() {
        log.info("Recupero tutti i clienti");
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Elenco clienti attivi
    public List<ClienteResponse> getAttivi() {
        return clienteRepository.findByAttivoTrue().stream()
                .map(clienteMapper::toResponse)
                .collect(Collectors.toList());
    }

    // Ricerca cliente per ID
    public ClienteResponse getById(Long id) {
        log.info("Recupero cliente con id: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new it.palatransport.planner.exception.ResourceNotFoundException("Cliente non trovato con id: " + id));
        return clienteMapper.toResponse(cliente);
    }

    // Creazione cliente
    public ClienteResponse create(ClienteRequest request) {
        log.info("Creazione nuovo cliente: {}", request.getNome());
        Cliente cliente = clienteMapper.toEntity(request);
        Cliente saved = clienteRepository.save(cliente);
        return clienteMapper.toResponse(saved);
    }

    // Aggiornamento cliente
    public ClienteResponse update(Long id, ClienteRequest request) {
        log.info("Aggiornamento cliente con id: {}", id);
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new it.palatransport.planner.exception.ResourceNotFoundException("Cliente non trovato con id: " + id));
        clienteMapper.updateEntity(request, cliente);
        Cliente updated = clienteRepository.save(cliente);
        return clienteMapper.toResponse(updated);
    }

    // Eliminazione cliente
    public void delete(Long id) {
        log.warn("Eliminazione cliente con id: {}", id);
        if (!clienteRepository.existsById(id)) {
            throw new it.palatransport.planner.exception.ResourceNotFoundException("Cliente non trovato con id: " + id);
        }
        clienteRepository.deleteById(id);
    }
}
