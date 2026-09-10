package br.com.fdrtec.repair_tips_api.service;

import br.com.fdrtec.repair_tips_api.dto.PartDto;
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
    public PartDto create(PartDto dto) {
        Part part = mapper.toEntity(dto);
        Part saved = repository.save(part);
        return mapper.toDto(saved);
    }

    @Transactional(readOnly = true)
    public PartDto findById(Long id) {
        return repository.findById(id)
            .map(mapper::toDto)
            .orElseThrow(() -> new ResourceNotFoundException("Part", id));
    }

    @Transactional(readOnly = true)
    public Page<PartDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional
    public PartDto update(Long id, PartDto dto) {
        Part part = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Part", id));
        mapper.updateFromDto(dto, part);
        return mapper.toDto(repository.save(part));
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Part", id);
        }
        repository.deleteById(id);
    }
}
