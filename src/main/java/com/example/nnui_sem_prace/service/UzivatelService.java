package com.example.nnui_sem_prace.service;

import com.example.nnui_sem_prace.dto.VypsanyTerminDTO;
import com.example.nnui_sem_prace.model.Uzivatel;
import com.example.nnui_sem_prace.model.VypsanyTermin;
import com.example.nnui_sem_prace.repository.UzivatelRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UzivatelService implements UserDetailsService {
    @Autowired
    private UzivatelRepository uzivatelRepository;

    public Uzivatel getUzivatelById(int id){
        return uzivatelRepository.getUzivatelByUzivatelId(id);
    }

    public Uzivatel getUzivatelByUsername(String username){
        return uzivatelRepository.getUzivatelByUsername(username);
    }

    public Uzivatel save(Uzivatel uzivatel){
        return uzivatelRepository.save(uzivatel);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Uzivatel uzivatel = getUzivatelByUsername(username);
        if (uzivatel == null) {
            throw new UsernameNotFoundException("Uzivatel nenalezen: " + username);
        }
        return uzivatel;
    }
}
