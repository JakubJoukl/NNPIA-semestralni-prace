package com.example.nnui_sem_prace.dto;

import com.example.nnui_sem_prace.validation.VekConstraint;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegistraceUzivateleDTO {

    @NotBlank(message = "Uživatelské jméno musí být vyplněno")
    @Length(min = 3, max = 255, message = "Uživatelské jméno musí být v rozsahu 3 až 255")
    private String username;

    @NotBlank(message = "Heslo musí být vyplněno")
    @Length(min = 5, max = 255, message = "Heslo musí být v rozsahu 5 až 255")
    private String heslo;

    @NotBlank(message = "Jméno musí být vyplněno")
    @Length(min = 1, max = 255, message = "Jméno musí být v rozsahu 1 až 255")
    private String jmeno;

    @NotBlank(message = "Příjmení musí být vyplněno")
    @Length(min = 1, max = 255, message = "Příjmení musí být v rozsahu 1 až 255")
    private String prijmeni;

    @Past(message = "Datum narození musí být v minulosti")
    @VekConstraint
    private LocalDate datumNarozeni;
}
