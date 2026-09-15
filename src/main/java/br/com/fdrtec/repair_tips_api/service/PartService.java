package br.com.fdrtec.repair_tips_api.service;

import br.com.fdrtec.repair_tips_api.dto.PartDto;
import br.com.fdrtec.repair_tips_api.entity.Part;
import br.com.fdrtec.repair_tips_api.mapper.GenericMapper;
import br.com.fdrtec.repair_tips_api.repository.PartRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PartService {

    private final PartRepository repository;
    private final GenericMapper mapper;

    private static final String[] MANAGED_PROPERTIES = {"id", "createdAt", "updatedAt", "active"};

    @Transactional
    public PartDto create(PartDto dto) {
        Part part = mapper.map(dto, Part.class, MANAGED_PROPERTIES);
        Part saved = repository.save(part);
        return mapper.map(saved, PartDto.class);
    }

    @Transactional(readOnly = true)
    public PartDto findById(Long id) {
        return repository.findById(id)
            .map(part -> mapper.map(part, PartDto.class))
            .orElseThrow(() -> new ResourceNotFoundException("Part", id));
    }

    @Transactional(readOnly = true)
    public Page<PartDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(part -> mapper.map(part, PartDto.class));
    }

    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public List<PartDto> findByName(String name) {
        return repository.findByName(name).stream().map(part -> mapper.map(part, PartDto.class)).toList();
    }

    @Transactional
    public PartDto update(Long id, PartDto dto) {
        Part part = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Part", id));
        mapper.update(dto, part, MANAGED_PROPERTIES);
        return mapper.map(repository.save(part), PartDto.class);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Part", id);
        }
        repository.deleteById(id);
    }
}
