package br.com.fdrtec.repair_tips_api.service;

import br.com.fdrtec.repair_tips_api.dto.PartRequest;
import br.com.fdrtec.repair_tips_api.dto.PartResponse;
import br.com.fdrtec.repair_tips_api.entity.Part;
import br.com.fdrtec.repair_tips_api.mapper.PartMapper;
import br.com.fdrtec.repair_tips_api.repository.PartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartService {

    private final PartRepository repository;
    private final PartMapper mapper;

    @Transactional
    public PartResponse create(PartRequest request) {
        Part part = mapper.toEntity(request);
        Part saved = repository.save(part);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PartResponse findById(Long id) {
        return repository.findById(id)
            .map(mapper::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Part", id));
    }

    @Transactional(readOnly = true)
    public Page<PartResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional
    public PartResponse update(Long id, PartRequest request) {
        Part part = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Part", id));
        mapper.updateFromRequest(request, part);
        return mapper.toResponse(repository.save(part));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Part", id);
        }
        repository.deleteById(id);
    }
}
