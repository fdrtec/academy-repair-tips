package br.com.fdrtec.repair_tips_api.repository;

import br.com.fdrtec.repair_tips_api.entity.Equipament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EquipamentRepository extends JpaRepository<Equipament, Long> {
}