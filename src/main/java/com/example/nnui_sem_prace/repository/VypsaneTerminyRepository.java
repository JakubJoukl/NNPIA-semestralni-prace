package com.example.nnui_sem_prace.repository;

import com.example.nnui_sem_prace.model.VypsanyTermin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface VypsaneTerminyRepository extends JpaRepository<VypsanyTermin, Integer> {
    List<VypsanyTermin> findAllByTrvaniOdLessThanEqualAndTrvaniDoGreaterThanEqual(LocalDateTime datumOd, LocalDateTime datumDo);

    VypsanyTermin getVypsanyTerminByVypsanyTerminId(Integer id);

    Page<VypsanyTermin> findAll(Pageable pageable);
}
