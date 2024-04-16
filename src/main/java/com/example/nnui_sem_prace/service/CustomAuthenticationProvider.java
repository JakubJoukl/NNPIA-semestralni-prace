package com.example.nnui_sem_prace.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import com.example.nnui_sem_prace.service.UzivatelService;
import com.example.nnui_sem_prace.model.Uzivatel;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UzivatelService userDetailsService;

    @Override
    @Transactional
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        Uzivatel user = userDetailsService.loadUserByUsername(username);
        return checkPassword(user, password);
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(aClass);
    }

    @Transactional
    private Authentication checkPassword(Uzivatel user, String rawPassword) {
        if(user == null){
            throw new BadCredentialsException("Uzivatel neexistuje");
        }
        if (Objects.equals(rawPassword, user.getPassword())) {
            return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(), user.getAuthorities());
        } else {
            throw new BadCredentialsException("Spatne prihlasovaci udaje");
        }
    }
}
