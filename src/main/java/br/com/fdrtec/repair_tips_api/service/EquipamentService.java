package br.com.fdrtec.repair_tips_api.service;

import br.com.fdrtec.repair_tips_api.dto.EquipamentDto;
import br.com.fdrtec.repair_tips_api.entity.Equipament;
import br.com.fdrtec.repair_tips_api.mapper.GenericMapper;
import br.com.fdrtec.repair_tips_api.repository.EquipamentRepository;
import br.com.fdrtec.repair_tips_api.repository.PartRepository;
import java.util.HashSet;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EquipamentService {

    private final EquipamentRepository repository;
    private final PartRepository partRepository;
    private final GenericMapper mapper;

    private static final String[] MANAGED_PROPERTIES = {"id", "createdAt", "updatedAt", "active", "parts", "partIds"};

    @Transactional
    public EquipamentDto create(EquipamentDto dto) {
        Equipament equipament = mapper.map(dto, Equipament.class, MANAGED_PROPERTIES);
        equipament.setParts(resolveParts(dto.partIds()));
        return mapper.map(repository.save(equipament), EquipamentDto.class);
    }

    @Transactional(readOnly = true)
    public EquipamentDto findById(Long id) {
        return repository.findById(id)
            .map(equipament -> mapper.map(equipament, EquipamentDto.class))
            .orElseThrow(() -> new ResourceNotFoundException("Equipament", id));
    }

    @Transactional(readOnly = true)
    public Page<EquipamentDto> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(equipament -> mapper.map(equipament, EquipamentDto.class));
    }

    @Transactional(readOnly = true)
    public long count() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public List<EquipamentDto> findByName(String name) {
        return repository.findByName(name).stream().map(equipament -> mapper.map(equipament, EquipamentDto.class)).toList();
    }

    @Transactional
    public EquipamentDto update(Long id, EquipamentDto dto) {
        Equipament equipament = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Equipament", id));
        mapper.update(dto, equipament, MANAGED_PROPERTIES);
        equipament.setParts(resolveParts(dto.partIds()));
        return mapper.map(repository.save(equipament), EquipamentDto.class);
    }

    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Equipament", id);
        }
        repository.deleteById(id);
    }

    private List<br.com.fdrtec.repair_tips_api.entity.Part> resolveParts(List<Long> partIds) {
        var parts = partRepository.findAllById(partIds);
        if (parts.size() != new HashSet<>(partIds).size()) {
            throw new ResourceNotFoundException("Part", partIds.toString());
        }
        return parts;
    }
}