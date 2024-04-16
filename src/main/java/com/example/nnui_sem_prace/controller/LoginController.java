package com.example.nnui_sem_prace.controller;

import com.example.nnui_sem_prace.dto.RegistraceUzivateleDTO;
import com.example.nnui_sem_prace.model.Uzivatel;
import com.example.nnui_sem_prace.service.UzivatelService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/security")
public class LoginController {
    @Autowired
    JwtEncoder encoder;

    @Autowired
    UzivatelService uzivatelService;

    @GetMapping("/test")
    public String test(){
        return "Hello!";
    }

    @PostMapping("/login")
    public String token(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 36000L;
        // @formatter:off
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        // @formatter:on
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @PostMapping(value = "/register", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> register(@Valid @RequestBody RegistraceUzivateleDTO registraceUzivateleDTO) {
        if(uzivatelService.loadUserByUsername(registraceUzivateleDTO.getUsername()) != null){
            return new ResponseEntity<>("Uzivatel jiz existuje", HttpStatus.BAD_REQUEST);
        }
        Uzivatel uzivatel = uzivatelService.prevedRegistraceUzivateleDTONaUzivatel(registraceUzivateleDTO);
        uzivatel = uzivatelService.save(uzivatel);
        RegistraceUzivateleDTO uspesneZaregistrovanyUzivatel = uzivatelService.prevedUzivatelNaRegistraceUzivateleDTO(uzivatel);
        return new ResponseEntity<>(uspesneZaregistrovanyUzivatel, HttpStatus.CREATED);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }
}
