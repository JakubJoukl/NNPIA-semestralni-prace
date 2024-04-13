package com.example.nnui_sem_prace.service;

import com.example.nnui_sem_prace.dto.VypsanyTerminDTO;
import com.example.nnui_sem_prace.model.VypsanyTermin;
import com.example.nnui_sem_prace.repository.VypsaneTerminyRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class VypsaneTerminyService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private VypsaneTerminyRepository vypsaneTerminyRepository;

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
}
