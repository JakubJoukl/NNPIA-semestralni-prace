package com.example.nnui_sem_prace.repository;

import com.example.nnui_sem_prace.model.Uzivatel;
import com.example.nnui_sem_prace.model.VypsanyTermin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UzivatelRepository extends JpaRepository<Uzivatel, Integer> {

    public Uzivatel getUzivatelByUzivatelId(Integer id);

    public Uzivatel getUzivatelByUsername(String username);

    Page<Uzivatel> findAll(Pageable pageable);
}
