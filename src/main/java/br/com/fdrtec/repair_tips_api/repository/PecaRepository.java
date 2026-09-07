package br.com.fdrtec.repair_tips_api.repository;

import br.com.fdrtec.repair_tips_api.entity.Peca;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PecaRepository extends JpaRepository<Peca, Long> {
}
