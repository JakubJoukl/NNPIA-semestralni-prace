package com.example.nnui_sem_prace.service;

import com.example.nnui_sem_prace.dto.VypsanyTerminDTO;
import com.example.nnui_sem_prace.enums.STAV_REZERVACE;
import com.example.nnui_sem_prace.model.Uzivatel;
import com.example.nnui_sem_prace.model.VypsanyTermin;
import com.example.nnui_sem_prace.repository.VypsaneTerminyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class VypsaneTerminyService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private VypsaneTerminyRepository vypsaneTerminyRepository;

    public List<VypsanyTermin> dejVeskerePlatneVypsaneTerminyStrankovatelne(PageRequest pageRequest){
        LocalDateTime dnes = LocalDateTime.now();
        return vypsaneTerminyRepository.dejVeskerePlatneVolneTerminyStrankovatelne(dnes, pageRequest).getContent();
    }

    public List<VypsanyTermin> dejVeskerePlatneVypsaneTerminyNaDenStrankovatelne(PageRequest pageRequest, LocalDate keDni){
        LocalDateTime dnes = LocalDateTime.now();
        return vypsaneTerminyRepository.dejVeskerePlatneVolneTerminyStrankovatelneANaDen(dnes, keDni, pageRequest).getContent();
    }

    public List<VypsanyTermin> dejVeskerePlatneVypsaneTerminyNaDen(LocalDate keDni){
        LocalDateTime dnes = LocalDateTime.now();
        return vypsaneTerminyRepository.dejVeskerePlatneVolneTerminyNaDen(dnes, keDni);
    }

    public List<VypsanyTermin> dejVeskerePlatneTerminy(){
        LocalDateTime dnes = LocalDateTime.now();
        return vypsaneTerminyRepository.dejVeskerePlatneVolneTerminy(dnes);
    }

    public VypsanyTerminDTO prevedVypsanyTerminNaDTO(VypsanyTermin vypsanyTermin){
        return modelMapper.map(vypsanyTermin, VypsanyTerminDTO.class);
    }

    public VypsanyTermin prevedDTOnaVypsanyTermin(VypsanyTerminDTO vypsanyTerminDTO){
        return modelMapper.map(vypsanyTerminDTO, VypsanyTermin.class);
    }

    public List<VypsanyTermin> prevedListDTOnaVypsanyTermin(List<VypsanyTerminDTO> vypsaneTerminyDTO){
        List<VypsanyTermin> vypsaneTerminy = new ArrayList<>();
        for (VypsanyTerminDTO vypsanyTerminDto : vypsaneTerminyDTO) {
            vypsaneTerminy.add(prevedDTOnaVypsanyTermin(vypsanyTerminDto));
        }
        return vypsaneTerminy;
    }

    public List<VypsanyTerminDTO> prevedListVypsanyTerminNaDTO(List<VypsanyTermin> vypsaneTerminy){
        List<VypsanyTerminDTO> vypsaneTerminyDto = new ArrayList<>();
        for (VypsanyTermin vypsanyTermin : vypsaneTerminy) {
            vypsaneTerminyDto.add(prevedVypsanyTerminNaDTO(vypsanyTermin));
        }
        return vypsaneTerminyDto;
    }

    public VypsanyTermin getVypsanyTerminById(int id){
        return vypsaneTerminyRepository.getVypsanyTerminByVypsanyTerminId(id);
    }

    public VypsanyTermin save(VypsanyTermin vypsanyTermin){
        return vypsaneTerminyRepository.save(vypsanyTermin);
    }

    public HashMap<String, Object> obalInformacemiOPoctuVolnychVypsanychTerminu(List<VypsanyTerminDTO> vypsaneTerminy) {
        return obalInformacemiOPoctuVolnychVypsanychTerminu(vypsaneTerminy, null);
    }

    public HashMap<String, Object> obalInformacemiOPoctuVolnychVypsanychTerminu(List<VypsanyTerminDTO> vypsaneTerminy, LocalDate den) {
        HashMap<String, Object> vraceneParametry = new HashMap<>();
        if(den == null) {
            vraceneParametry.put("pocet", dejVeskerePlatneTerminy().size());
        } else {
            vraceneParametry.put("pocet", dejVeskerePlatneVypsaneTerminyNaDen(den).size());
        }
        vraceneParametry.put("data", vypsaneTerminy);
        return vraceneParametry;
    }
}
