package com.example.nnui_sem_prace.repository;

import com.example.nnui_sem_prace.model.RezervaceTerminu;
import com.example.nnui_sem_prace.model.VypsanyTermin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface RezervaceTerminuRepository extends JpaRepository<RezervaceTerminu, Integer> {
    @Query("SELECT r FROM RezervaceTerminu r inner JOIN r.uzivatel u inner join r.vypsanyTermin t where r.uzivatel.uzivatelId = ?1 and r.stavRezervace in ('POTVRZENA', 'NEPOTVRZENA') and date(t.trvaniOd) = ?2")
    public List<RezervaceTerminu> DejVeskereRezervaceUzivateleNaDen(int uzivatelId, LocalDate den);

    @Query("SELECT r FROM RezervaceTerminu r inner JOIN r.uzivatel u inner join r.vypsanyTermin t where r.uzivatel.uzivatelId = :uzivatelId ORDER by t.trvaniOd asc")
    public Page<RezervaceTerminu> findRezervaceTerminuByUzivatelUzivatelIdAsc(Pageable pageable, @Param("uzivatelId") Integer uzivatelId);

    @Query("SELECT r FROM RezervaceTerminu r inner JOIN r.uzivatel u inner join r.vypsanyTermin t where r.uzivatel.uzivatelId = :uzivatelId ORDER by t.trvaniOd desc")
    public Page<RezervaceTerminu> findRezervaceTerminuByUzivatelUzivatelIdDesc(Pageable pageable, @Param("uzivatelId") Integer uzivatelId);

    public RezervaceTerminu getRezervaceTerminuByRezervaceTerminuId(Integer rezervaceTerminuId);

    Page<RezervaceTerminu> findAll(Pageable pageable);
}
