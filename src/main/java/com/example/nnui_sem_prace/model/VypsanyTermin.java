package com.example.nnui_sem_prace.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
//@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="vypsanyTerminId")
public class VypsanyTermin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int vypsanyTerminId;

    @Column
    private LocalDateTime trvaniOd;

    @Column
    private LocalDateTime trvaniDo;

    @Column(length = 4000)
    private String omezeniTerminu;

    @OneToMany(mappedBy = "vypsanyTermin")
    private List<RezervaceTerminu> rezervaceTerminu;

    public VypsanyTermin(LocalDateTime trvaniOd, LocalDateTime trvaniDo, String omezeniTerminu, List<RezervaceTerminu> rezervaceTerminu) {
        this.trvaniOd = trvaniOd;
        this.trvaniDo = trvaniDo;
        this.omezeniTerminu = omezeniTerminu;
        this.rezervaceTerminu = rezervaceTerminu;
    }
}
