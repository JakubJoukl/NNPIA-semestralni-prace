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
import java.util.List;

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
        if(existujiPlatneRezervaceNaTermin(vypsanyTermin)) throw new RuntimeException("Na vypsany termin uz existuje platna rezervace");
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
}
