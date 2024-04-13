package com.example.nnui_sem_prace.dto;

import com.example.nnui_sem_prace.enums.STAV_REZERVACE;
import com.example.nnui_sem_prace.model.Uzivatel;
import com.example.nnui_sem_prace.model.VypsanyTermin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RezervaceTerminuDTO {
    private int rezervaceTerminuId;
    private STAV_REZERVACE stavRezervace;
    private String poznamkaLekare;
    private Uzivatel uzivatel;
    private VypsanyTermin vypsanyTermin;
}
