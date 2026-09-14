package br.com.fdrtec.repair_tips_api.repository;

import br.com.fdrtec.repair_tips_api.entity.Part;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartRepository extends JpaRepository<Part, Long> {

	List<Part> findByName(String name);
}
