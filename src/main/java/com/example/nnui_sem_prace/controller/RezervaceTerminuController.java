package com.example.nnui_sem_prace.controller;

import com.example.nnui_sem_prace.Utils.DatumoveNastroje;
import com.example.nnui_sem_prace.dto.RezervaceTerminuDTO;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

@RestController()
@RequestMapping("/terminyUzivatele")
@AllArgsConstructor
@NoArgsConstructor
public class RezervaceTerminuController {

    @Autowired
    private RezervaceTerminuService rezervaceTerminuService;

    @Autowired
    private UzivatelService uzivatelService;

    @Autowired
    private VypsaneTerminyService vypsaneTerminyService;


    @GetMapping("/{idUzivatele}")
    public ResponseEntity<?> dejRezervaceUzivatele(@PathVariable Integer idUzivatele, @RequestParam int pageNumber, @RequestParam boolean asc){
        if(idUzivatele == null) return new ResponseEntity<>("Nevyplnene id uzivatele", HttpStatus.BAD_REQUEST);
        Uzivatel uzivatel = uzivatelService.getUzivatelById(idUzivatele);
        if(uzivatel == null) return new ResponseEntity<>("Uzivatel nenalezen", HttpStatus.BAD_REQUEST);
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, 10);
        List<RezervaceTerminuDTO> rezervaceTerminuUzivatele = rezervaceTerminuService.prevedListRezervaciTerminuNaListDTO(rezervaceTerminuService.dejVeskereTerminyUzivatele(idUzivatele, pageRequest, asc));
        return new ResponseEntity<>(rezervaceTerminuUzivatele, HttpStatus.OK);
    }

    @GetMapping("/{idUzivatele}/{denDDMMRRRR}")
    public ResponseEntity<?> dejRezervaceUzivateleNaDen(@PathVariable Integer idUzivatele, @PathVariable String denDDMMRRRR){
        if(idUzivatele == null) return new ResponseEntity<>("Nevyplnene id uzivatele", HttpStatus.BAD_REQUEST);
        try {
            LocalDate den = DatumoveNastroje.prevedDatumNaLocalDate(denDDMMRRRR);
            List<RezervaceTerminuDTO> rezervaceTerminuUzivateleDto = rezervaceTerminuService.prevedListRezervaciTerminuNaListDTO(rezervaceTerminuService.dejVeskereTerminyUzivateleNaDen(idUzivatele, den));
            return new ResponseEntity<>(rezervaceTerminuUzivateleDto, HttpStatus.OK);
        } catch (ParseException e) {
            return new ResponseEntity<>("Spatne vyplnene datum", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{idUzivatele}/vytvorRezervaci")
    public ResponseEntity<?> vytvorRezervaciNaTermin(@PathVariable Integer idUzivatele, @RequestBody Integer vypsanyTerminId){
        if(idUzivatele == null) return new ResponseEntity<>("Nevyplnene id uzivatele", HttpStatus.BAD_REQUEST);
        if(vypsanyTerminId == null) return new ResponseEntity<>("Nevyplnene id terminu rezervace", HttpStatus.BAD_REQUEST);
        Uzivatel uzivatel = uzivatelService.getUzivatelById(idUzivatele);
        if(uzivatel == null) return new ResponseEntity<>("Uzivatel nenalezen", HttpStatus.BAD_REQUEST);
        VypsanyTermin vypsanyTermin = vypsaneTerminyService.getVypsanyTerminById(vypsanyTerminId);
        if(vypsanyTermin == null) return new ResponseEntity<>("Vypsany termin nenalezen", HttpStatus.BAD_REQUEST);
        if(rezervaceTerminuService.maUzivatelRezervaciNaDanyDen(uzivatel, vypsanyTermin.getTrvaniOd())) return new ResponseEntity<>("Rezervace nebyla vytvorena - uzivatel na dany termin jiz ma rezervaci", HttpStatus.BAD_REQUEST);
        RezervaceTerminu rezervaceTerminu = rezervaceTerminuService.vytvorRezervaci(uzivatel, vypsanyTermin);
        if(rezervaceTerminu == null) return new ResponseEntity<>("Rezervaci se nepodarilo vytvorit", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(rezervaceTerminuService.prevedRezervaciTerminuNaDTO(rezervaceTerminu), HttpStatus.CREATED);
    }

    @PostMapping("/{idUzivatele}/zrusRezervaciTerminu")
    public ResponseEntity<?> smazRezervaciUzivatele(@PathVariable Integer idUzivatele, @RequestBody Integer rezervaceId){
        if(idUzivatele == null) return new ResponseEntity<>("Nevyplnene id uzivatele", HttpStatus.BAD_REQUEST);
        if(rezervaceId == null) return new ResponseEntity<>("Nevyplnene id rezervace", HttpStatus.BAD_REQUEST);
        Uzivatel uzivatel = uzivatelService.getUzivatelById(idUzivatele);
        if(uzivatel == null) return new ResponseEntity<>("Uzivatel nenalezen", HttpStatus.BAD_REQUEST);
        RezervaceTerminu rezervaceTerminu = rezervaceTerminuService.dejRezervaciTerminuDleId(rezervaceId);
        if(rezervaceTerminu == null) return new ResponseEntity<>("Rezervace terminu nenalezena", HttpStatus.BAD_REQUEST);
        if(!rezervaceTerminuService.patriRezervaceUzivateli(rezervaceTerminu, uzivatel)){
            return new ResponseEntity<>("Rezervace terminu nepatri danemu uzivateli", HttpStatus.BAD_REQUEST);
        }
        if(!rezervaceTerminuService.jeMozneZrusitRezervaci(rezervaceTerminu)) return new ResponseEntity<>("Rezervace terminu neni potvrzena", HttpStatus.BAD_REQUEST);
        rezervaceTerminu = rezervaceTerminuService.zrusRezervaciUzivatelem(rezervaceTerminu);
        return new ResponseEntity<>(rezervaceTerminuService.prevedRezervaciTerminuNaDTO(rezervaceTerminu), HttpStatus.OK);
    }
}
