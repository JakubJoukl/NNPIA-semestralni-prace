package com.example.nnui_sem_prace.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="uzivatelId")
public class Uzivatel implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int uzivatelId;

    @Column
    private String username;

    @Column
    private String heslo;

    @Column
    private String jmeno;

    @Column
    private String prijmeni;

    @Column
    private LocalDate datumNarozeni;

    @OneToMany(mappedBy = "uzivatel")
    private List<RezervaceTerminu> rezervovaneTerminy;

    @ManyToMany
    @JoinTable(
            name = "uzivatele_role",
            joinColumns = @JoinColumn(
                    name = "uzivatel_id", referencedColumnName = "uzivatelId"),
            inverseJoinColumns = @JoinColumn(
                    name = "role_id", referencedColumnName = "roleId"))
    private List<Role> roles;

    public Uzivatel(String username, String heslo, String jmeno, String prijmeni, LocalDate datumNarozeni, List<RezervaceTerminu> rezervovaneTerminy) {
        this.username = username;
        this.heslo = heslo;
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.datumNarozeni = datumNarozeni;
        this.rezervovaneTerminy = rezervovaneTerminy;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return heslo;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
