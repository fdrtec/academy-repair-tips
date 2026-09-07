package br.com.fdrtec.repair_tips_api.service;

import br.com.fdrtec.repair_tips_api.dto.EquipamentRequest;
import br.com.fdrtec.repair_tips_api.dto.EquipamentResponse;
import br.com.fdrtec.repair_tips_api.entity.Equipament;
import br.com.fdrtec.repair_tips_api.mapper.EquipamentMapper;
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
    private final EquipamentMapper mapper;

    @Transactional
    public EquipamentResponse create(EquipamentRequest request) {
        Equipament equipament = mapper.toEntity(request);
        equipament.setParts(resolveParts(request.partIds()));
        return mapper.toResponse(repository.save(equipament));
    }

    @Transactional(readOnly = true)
    public EquipamentResponse findById(Long id) {
        return repository.findById(id)
            .map(mapper::toResponse)
            .orElseThrow(() -> new ResourceNotFoundException("Equipament", id));
    }

    @Transactional(readOnly = true)
    public Page<EquipamentResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toResponse);
    }

    @Transactional
    public EquipamentResponse update(Long id, EquipamentRequest request) {
        Equipament equipament = repository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Equipament", id));
        mapper.updateFromRequest(request, equipament);
        equipament.setParts(resolveParts(request.partIds()));
        return mapper.toResponse(repository.save(equipament));
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