package com.example.nnui_sem_prace.model;

import com.example.nnui_sem_prace.enums.STAV_REZERVACE;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="rezervaceTerminuId")
public class RezervaceTerminu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rezervaceTerminuId;

    @Enumerated(EnumType.STRING)
    @Column
    private STAV_REZERVACE stavRezervace;

    @Column
    private String poznamkaLekare;

    @ManyToOne
    @JoinColumn(name = "uzivatel_id")
    private Uzivatel uzivatel;

    @ManyToOne
    @JoinColumn(name = "vypsany_Termin_Id")
    private VypsanyTermin vypsanyTermin;

    public RezervaceTerminu(STAV_REZERVACE stavRezervace, String poznamkaLekare, Uzivatel uzivatel, VypsanyTermin vypsanyTermin) {
        this.stavRezervace = stavRezervace;
        this.poznamkaLekare = poznamkaLekare;
        this.uzivatel = uzivatel;
        this.vypsanyTermin = vypsanyTermin;
    }
}
