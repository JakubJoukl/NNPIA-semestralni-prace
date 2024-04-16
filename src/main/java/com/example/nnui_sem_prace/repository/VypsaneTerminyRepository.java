package com.example.nnui_sem_prace.repository;

import com.example.nnui_sem_prace.model.VypsanyTermin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface VypsaneTerminyRepository extends JpaRepository<VypsanyTermin, Integer> {
    List<VypsanyTermin> findAllByTrvaniOdLessThanEqualAndTrvaniDoGreaterThanEqual(LocalDateTime datumOd, LocalDateTime datumDo);

    VypsanyTermin getVypsanyTerminByVypsanyTerminId(Integer id);

    @Query("SELECT v from VypsanyTermin v where not exists (SELECT r from RezervaceTerminu r where r.vypsanyTermin = v and r.stavRezervace in ('POTVRZENA', 'NEPOTVRZENA')) and v.trvaniOd > :keDni order by v.trvaniOd ASC")
    Page<VypsanyTermin> dejVeskerePlatneVolneTerminyStrankovatelne(@Param("keDni") LocalDateTime keDni, Pageable pageable);

    @Query("SELECT v from VypsanyTermin v where not exists (SELECT r from RezervaceTerminu r where r.vypsanyTermin = v and r.stavRezervace in ('POTVRZENA', 'NEPOTVRZENA')) and v.trvaniOd > :keDni order by v.trvaniOd ASC")
    List<VypsanyTermin> dejVeskerePlatneVolneTerminy(@Param("keDni") LocalDateTime keDni);

    @Query("SELECT v from VypsanyTermin v where not exists (SELECT r from RezervaceTerminu r where r.vypsanyTermin = v and r.stavRezervace in ('POTVRZENA', 'NEPOTVRZENA')) and v.trvaniOd > :dnes and date(v.trvaniOd) = :denTerminu order by v.trvaniOd ASC")
    Page<VypsanyTermin> dejVeskerePlatneVolneTerminyStrankovatelneANaDen(@Param("dnes") LocalDateTime keDni, LocalDate denTerminu, Pageable pageable);

    @Query("SELECT v from VypsanyTermin v where not exists (SELECT r from RezervaceTerminu r where r.vypsanyTermin = v and r.stavRezervace in ('POTVRZENA', 'NEPOTVRZENA')) and v.trvaniOd > :dnes and date(v.trvaniOd) = :denTerminu order by v.trvaniOd ASC")
    List<VypsanyTermin> dejVeskerePlatneVolneTerminyNaDen(@Param("dnes") LocalDateTime keDni, LocalDate denTerminu);

    Page<VypsanyTermin> findAll(Pageable pageable);
}
