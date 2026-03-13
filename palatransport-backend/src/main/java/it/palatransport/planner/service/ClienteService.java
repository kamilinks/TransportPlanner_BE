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

@Service
@Transactional
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    public List<ClienteResponse> getAll() {
        return clienteRepository.findAll().stream()
                .map(clienteMapper::toResponse)
                .collect(Collectors.toList());
    }

    public List<ClienteResponse> getAttivi() {
        return clienteRepository.findByAttivoTrue().stream()
                .map(clienteMapper::toResponse)
                .collect(Collectors.toList());
    }

    public ClienteResponse getById(Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente non trovato con id: " + id));
        return clienteMapper.toResponse(cliente);
    }

    public ClienteResponse create(ClienteRequest request) {
        Cliente cliente = clienteMapper.toEntity(request);
        Cliente saved = clienteRepository.save(cliente);
        return clienteMapper.toResponse(saved);
    }

    public ClienteResponse update(Long id, ClienteRequest request) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente non trovato con id: " + id));
        clienteMapper.updateEntity(request, cliente);
        Cliente updated = clienteRepository.save(cliente);
        return clienteMapper.toResponse(updated);
    }

    public void delete(Long id) {
        clienteRepository.deleteById(id);
    }
}
