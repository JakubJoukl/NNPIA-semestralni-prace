package com.example.nnui_sem_prace.controller;

import com.example.nnui_sem_prace.Utils.DatumoveNastroje;
import com.example.nnui_sem_prace.Utils.RadiciNastroje;
import com.example.nnui_sem_prace.dto.RezervaceTerminuDTO;
import com.example.nnui_sem_prace.model.RezervaceTerminu;
import com.example.nnui_sem_prace.model.Uzivatel;
import com.example.nnui_sem_prace.model.VypsanyTermin;
import com.example.nnui_sem_prace.service.RezervaceTerminuService;
import com.example.nnui_sem_prace.service.UzivatelService;
import com.example.nnui_sem_prace.service.VypsaneTerminyService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.HashMap;
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

    @GetMapping("/")
    public ResponseEntity<?> dejRezervaceUzivatele(Authentication authentication, @RequestParam int pageNumber, @RequestParam boolean asc, @RequestParam(value = "sort", required = false) List<String> sortBy){
        String uzivatelskeJmeno = authentication.getName();
        Uzivatel uzivatel = uzivatelService.loadUserByUsername(uzivatelskeJmeno);
        if(uzivatel == null) return new ResponseEntity<>("Uzivatel nenalezen", HttpStatus.BAD_REQUEST);
        PageRequest pageRequest = PageRequest.of(pageNumber, 10, RadiciNastroje.getSort(sortBy));
        List<RezervaceTerminuDTO> rezervaceTerminuUzivatele = rezervaceTerminuService.prevedListRezervaciTerminuNaListDTO(rezervaceTerminuService.dejVeskereTerminyUzivatele(uzivatel.getUzivatelId(), pageRequest));
        HashMap<String, Object> vraceneParametry = rezervaceTerminuService.obalInformacemiOPoctu(uzivatel, rezervaceTerminuUzivatele, false);
        return new ResponseEntity<>(vraceneParametry, HttpStatus.OK);
    }

    @GetMapping("/budouci")
    public ResponseEntity<?> dejRezervaceUzivateleVPlatneAVBudoucnu(Authentication authentication, @RequestParam int pageNumber, @RequestParam boolean asc, @RequestParam(value = "sort", required = false) List<String> sortBy){
        String uzivatelskeJmeno = authentication.getName();

        if(sortBy != null) {
            for (int i = 0; i < sortBy.size(); i += 2) {
                if (sortBy.get(i).equals("trvaniOd") || sortBy.get(i).equals("trvaniDo")) {
                    sortBy.set(i, "t." + sortBy.get(i));
                }
            }
        }

        Uzivatel uzivatel = uzivatelService.loadUserByUsername(uzivatelskeJmeno);
        if(uzivatel == null) return new ResponseEntity<>("Uzivatel nenalezen", HttpStatus.BAD_REQUEST);
        PageRequest pageRequest = PageRequest.of(pageNumber, 10, RadiciNastroje.getSort(sortBy));
        List<RezervaceTerminuDTO> rezervaceTerminuUzivatele = rezervaceTerminuService.prevedListRezervaciTerminuNaListDTO(rezervaceTerminuService.dejVeskereTerminyUzivatelePlatneAVBudoucnu(uzivatel.getUzivatelId(), pageRequest));
        HashMap<String, Object> vraceneParametry = rezervaceTerminuService.obalInformacemiOPoctu(uzivatel, rezervaceTerminuUzivatele, true);
        return new ResponseEntity<>(vraceneParametry, HttpStatus.OK);
    }

    @GetMapping("/{denDDMMRRRR}")
    public ResponseEntity<?> dejRezervaceUzivateleNaDen(Authentication authentication, @PathVariable String denDDMMRRRR){
        String uzivatelskeJmeno = authentication.getName();
        Uzivatel uzivatel = uzivatelService.loadUserByUsername(uzivatelskeJmeno);
        if(uzivatel == null) return new ResponseEntity<>("Uzivatel nenalezen", HttpStatus.BAD_REQUEST);
        try {
            LocalDate den = DatumoveNastroje.prevedDatumNaLocalDate(denDDMMRRRR);
            List<RezervaceTerminuDTO> rezervaceTerminuUzivateleDto = rezervaceTerminuService.prevedListRezervaciTerminuNaListDTO(rezervaceTerminuService.dejVeskereTerminyUzivateleNaDen(uzivatel.getUzivatelId(), den));
            return new ResponseEntity<>(rezervaceTerminuUzivateleDto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Spatne vyplnene datum", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/zrusRezervaciTerminu")
    public ResponseEntity<?> smazRezervaciUzivatele(Authentication authentication, @RequestBody Integer rezervaceId){
        if(rezervaceId == null) return new ResponseEntity<>("Nevyplnene id rezervace", HttpStatus.BAD_REQUEST);
        String uzivatelskeJmeno = authentication.getName();
        Uzivatel uzivatel = uzivatelService.loadUserByUsername(uzivatelskeJmeno);
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
