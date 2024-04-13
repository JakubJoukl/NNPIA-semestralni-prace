package com.example.nnui_sem_prace.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;

@Entity
@Data
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="roleId")
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int roleId;

    private String nazevRole;

    private String name;
    @ManyToMany(mappedBy = "roles")
    private List<Uzivatel> uzivatele;

    @Override
    public String getAuthority() {
        return nazevRole;
    }
}
