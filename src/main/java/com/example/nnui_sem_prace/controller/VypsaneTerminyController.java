package com.example.nnui_sem_prace.controller;

import com.example.nnui_sem_prace.Utils.DatumoveNastroje;
import com.example.nnui_sem_prace.Utils.RadiciNastroje;
import com.example.nnui_sem_prace.dto.VypsanyTerminDTO;
import com.example.nnui_sem_prace.model.RezervaceTerminu;
import com.example.nnui_sem_prace.model.Uzivatel;
import com.example.nnui_sem_prace.model.VypsanyTermin;
import com.example.nnui_sem_prace.service.RezervaceTerminuService;
import com.example.nnui_sem_prace.service.UzivatelService;
import com.example.nnui_sem_prace.service.VypsaneTerminyService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController()
@RequestMapping("/vypsaneTerminy")
@AllArgsConstructor
@NoArgsConstructor
public class VypsaneTerminyController {
    @Autowired
    private VypsaneTerminyService vypsaneTerminyService;

    @Autowired
    private RezervaceTerminuService rezervaceTerminuService;

    @Autowired
    private UzivatelService uzivatelService;

    @GetMapping("/")
    public ResponseEntity<?> dejVolneVypsaneTerminy(Authentication authentication, @RequestParam int pageNumber, @RequestParam(value = "sort", required = false) List<String> sortBy){
        String uzivatelskeJmeno = authentication.getName();
        Uzivatel uzivatel = uzivatelService.loadUserByUsername(uzivatelskeJmeno);
        if(uzivatel == null) return new ResponseEntity<>("Uzivatel nenalezen", HttpStatus.BAD_REQUEST);
        PageRequest pageRequest = PageRequest.of(pageNumber, 10, RadiciNastroje.getSort(sortBy));
        List<VypsanyTerminDTO> vypsaneTerminy = vypsaneTerminyService.prevedListVypsanyTerminNaDTO(vypsaneTerminyService.dejVeskerePlatneVypsaneTerminyStrankovatelne(pageRequest));
        HashMap<String, Object> vraceneParametry = vypsaneTerminyService.obalInformacemiOPoctuVolnychVypsanychTerminu(vypsaneTerminy);
        return new ResponseEntity<>(vraceneParametry, HttpStatus.OK);
    }

    @GetMapping("/{denDDMMRRRR}")
    public ResponseEntity<?> dejVolneVypsaneTerminyNaDen(Authentication authentication, @PathVariable String denDDMMRRRR, @RequestParam int pageNumber, @RequestParam(value = "sort", required = false) List<String> sortBy){
        String uzivatelskeJmeno = authentication.getName();
        Uzivatel uzivatel = uzivatelService.loadUserByUsername(uzivatelskeJmeno);
        if(uzivatel == null) return new ResponseEntity<>("Uzivatel nenalezen", HttpStatus.BAD_REQUEST);
        try {
            System.out.println("denDDMMRRRR " + denDDMMRRRR);
            LocalDate den = DatumoveNastroje.prevedDatumNaLocalDate(denDDMMRRRR);
            PageRequest pageRequest = PageRequest.of(pageNumber, 10, RadiciNastroje.getSort(sortBy));
            List<VypsanyTerminDTO> vypsaneTerminy = vypsaneTerminyService.prevedListVypsanyTerminNaDTO(vypsaneTerminyService.dejVeskerePlatneVypsaneTerminyNaDenStrankovatelne(pageRequest, den));
            HashMap<String, Object> vraceneParametry = vypsaneTerminyService.obalInformacemiOPoctuVolnychVypsanychTerminu(vypsaneTerminy, den);
            return new ResponseEntity<>(vraceneParametry, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Spatne vyplnene datum " + e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/vytvorRezervaci")
    public ResponseEntity<?> vytvorRezervaciNaTermin(Authentication authentication, @RequestBody Integer vypsanyTerminId){
        String uzivatelskeJmeno = authentication.getName();
        Uzivatel uzivatel = uzivatelService.loadUserByUsername(uzivatelskeJmeno);
        if(uzivatel == null) return new ResponseEntity<>("Uzivatel nenalezen", HttpStatus.BAD_REQUEST);
        VypsanyTermin vypsanyTermin = vypsaneTerminyService.getVypsanyTerminById(vypsanyTerminId);
        if(vypsanyTermin == null) return new ResponseEntity<>("Vypsany termin nenalezen", HttpStatus.BAD_REQUEST);
        if(rezervaceTerminuService.maUzivatelRezervaciNaDanyDen(uzivatel, vypsanyTermin.getTrvaniOd())) return new ResponseEntity<>("Rezervace nebyla vytvorena - uzivatel na dany termin jiz ma rezervaci", HttpStatus.BAD_REQUEST);
        RezervaceTerminu rezervaceTerminu;
        try {
            rezervaceTerminu = rezervaceTerminuService.vytvorRezervaci(uzivatel, vypsanyTermin);
        } catch (Exception ex){
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
        if(rezervaceTerminu == null) return new ResponseEntity<>("Rezervaci se nepodarilo vytvorit", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(rezervaceTerminuService.prevedRezervaciTerminuNaDTO(rezervaceTerminu), HttpStatus.CREATED);
    }
}
