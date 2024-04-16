package com.example.nnui_sem_prace.service;

import com.example.nnui_sem_prace.dto.RezervaceTerminuDTO;
import com.example.nnui_sem_prace.enums.STAV_REZERVACE;
import com.example.nnui_sem_prace.model.RezervaceTerminu;
import com.example.nnui_sem_prace.model.Uzivatel;
import com.example.nnui_sem_prace.model.VypsanyTermin;
import com.example.nnui_sem_prace.repository.RezervaceTerminuRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RezervaceTerminuService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private RezervaceTerminuRepository rezervaceTerminuRepository;

    public RezervaceTerminuDTO prevedRezervaciTerminuNaDTO(RezervaceTerminu vypsanyTermin){
        return modelMapper.map(vypsanyTermin, RezervaceTerminuDTO.class);
    }

    public RezervaceTerminu prevedDTOnaRezervaciTerminu(RezervaceTerminuDTO vypsanyTerminDTO){
        return modelMapper.map(vypsanyTerminDTO, RezervaceTerminu.class);
    }

    public List<RezervaceTerminu> prevedListDTOnaListRezervaceTerminu(List<RezervaceTerminuDTO> vypsaneTerminyDTO){
        List<RezervaceTerminu> vypsaneTerminy = new ArrayList<>();
        for (RezervaceTerminuDTO vypsanyTerminDto : vypsaneTerminyDTO) {
            vypsaneTerminy.add(prevedDTOnaRezervaciTerminu(vypsanyTerminDto));
        }
        return vypsaneTerminy;
    }

    public List<RezervaceTerminuDTO> prevedListRezervaciTerminuNaListDTO(List<RezervaceTerminu> vypsaneTerminy){
        List<RezervaceTerminuDTO> vypsaneTerminyDto = new ArrayList<>();
        for (RezervaceTerminu vypsanyTermin : vypsaneTerminy) {
            vypsaneTerminyDto.add(prevedRezervaciTerminuNaDTO(vypsanyTermin));
        }
        return vypsaneTerminyDto;
    }

    public RezervaceTerminu vytvorRezervaci(Uzivatel uzivatel, VypsanyTermin vypsanyTermin) {
        LocalDateTime dnes = LocalDateTime.now();
        if(existujiPlatneRezervaceNaTermin(vypsanyTermin)) throw new RuntimeException("Na vypsany termin uz existuje platna rezervace");
        if(vypsanyTermin.getTrvaniOd().isBefore(dnes)) throw new RuntimeException("Termin, ktery se pokousite rezervovat, je v minulosti");
        RezervaceTerminu rezervaceTerminu = new RezervaceTerminu(STAV_REZERVACE.NEPOTVRZENA, null, uzivatel, vypsanyTermin);
        return rezervaceTerminuRepository.save(rezervaceTerminu);
    }

    private boolean existujiPlatneRezervaceNaTermin(VypsanyTermin vypsanyTermin){
        return vypsanyTermin.getRezervaceTerminu().stream().anyMatch(rezervaceTerminu -> rezervaceTerminu.getStavRezervace() == STAV_REZERVACE.POTVRZENA || rezervaceTerminu.getStavRezervace() == STAV_REZERVACE.NEPOTVRZENA);
    }

    public boolean maUzivatelRezervaciNaDanyDen(Uzivatel uzivatel, LocalDateTime datumRezervace){
        return rezervaceTerminuRepository.DejVeskereRezervaceUzivateleNaDen(uzivatel.getUzivatelId(), datumRezervace.toLocalDate()).size() > 0;
    }

    public List<RezervaceTerminu> dejVeskereTerminyUzivatele(int uzivatelId, PageRequest strankovani, boolean asc){
        if(asc) {
            return rezervaceTerminuRepository.findRezervaceTerminuByUzivatelUzivatelIdAsc(strankovani, uzivatelId).getContent();
        } else {
            return rezervaceTerminuRepository.findRezervaceTerminuByUzivatelUzivatelIdDesc(strankovani, uzivatelId).getContent();
        }
    }

    public List<RezervaceTerminu> dejVeskereTerminyUzivatelePlatneAVBudoucnu(int uzivatelId, PageRequest strankovani, boolean asc){
        LocalDateTime dnes = LocalDateTime.now();
        if(asc) {
            return rezervaceTerminuRepository.dejAktualniPlatneTerminyUzivateleAsc(strankovani, uzivatelId, dnes).getContent();
        } else {
            return rezervaceTerminuRepository.dejAktualniPlatneTerminyUzivateleDesc(strankovani, uzivatelId, dnes).getContent();
        }
    }

    public List<RezervaceTerminu> dejVeskereTerminyUzivateleNaDen(int uzivatelId, LocalDate den){
        return rezervaceTerminuRepository.DejVeskereRezervaceUzivateleNaDen(uzivatelId, den);
    }

    public RezervaceTerminu dejRezervaciTerminuDleId(int idRezervace){
        return rezervaceTerminuRepository.getRezervaceTerminuByRezervaceTerminuId(idRezervace);
    }

    public boolean patriRezervaceUzivateli(RezervaceTerminu rezervaceTerminu, Uzivatel uzivatel){
        return rezervaceTerminu.getUzivatel().equals(uzivatel);
    }

    public boolean jeMozneZrusitRezervaci(RezervaceTerminu rezervaceTerminu){
        return rezervaceTerminu.getStavRezervace() == STAV_REZERVACE.POTVRZENA || rezervaceTerminu.getStavRezervace() == STAV_REZERVACE.NEPOTVRZENA;
    }

    public RezervaceTerminu zrusRezervaciUzivatelem(RezervaceTerminu rezervaceTerminu){
        rezervaceTerminu.setStavRezervace(STAV_REZERVACE.ZRUSENA);
        return save(rezervaceTerminu);
    }

    public RezervaceTerminu save(RezervaceTerminu rezervaceTerminu){
        return rezervaceTerminuRepository.save(rezervaceTerminu);
    }

    public HashMap<String, Object> obalInformacemiOPoctu(Uzivatel uzivatel, List<RezervaceTerminuDTO> rezervaceTerminuUzivatele, boolean jenBudouciAPlatne) {
        HashMap<String, Object> vraceneParametry = new HashMap<>();
        LocalDateTime dnes = LocalDateTime.now();
        if(jenBudouciAPlatne) {
            vraceneParametry.put("pocet", uzivatel.getRezervovaneTerminy().stream().filter(rezervaceTerminu -> (rezervaceTerminu.getStavRezervace() == STAV_REZERVACE.POTVRZENA || rezervaceTerminu.getStavRezervace() == STAV_REZERVACE.NEPOTVRZENA) && rezervaceTerminu.getVypsanyTermin().getTrvaniOd().isAfter(dnes)).toList().size());
        } else {
            vraceneParametry.put("pocet", uzivatel.getRezervovaneTerminy().size());
        }
        vraceneParametry.put("data", rezervaceTerminuUzivatele);
        return vraceneParametry;
    }
}
