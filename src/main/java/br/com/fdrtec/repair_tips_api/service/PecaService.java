package br.com.fdrtec.repair_tips_api.service;

import br.com.fdrtec.repair_tips_api.dto.PecaRequest;
import br.com.fdrtec.repair_tips_api.dto.PecaResponse;
import br.com.fdrtec.repair_tips_api.entity.Peca;
import br.com.fdrtec.repair_tips_api.mapper.PecaMapper;
import br.com.fdrtec.repair_tips_api.repository.PecaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PecaService {

    private final PecaRepository repository;
    private final PecaMapper mapper;

    @Transactional
    public PecaResponse create(PecaRequest request) {
        Peca peca = mapper.toEntity(request);
        Peca saved = repository.save(peca);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PecaResponse findById(Long id) {
        return repository.findById(id)
            .map(mapper::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Peça", id));
    }

    @Transactional(readOnly = true)
    public Page<PecaResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional
    public PecaResponse update(Long id, PecaRequest request) {
        Peca peca = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Peça", id));
        mapper.updateFromRequest(request, peca);
        return mapper.toResponse(repository.save(peca));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Peça", id);
        }
        repository.deleteById(id);
    }
}
