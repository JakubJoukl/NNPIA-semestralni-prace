package com.example.nnui_sem_prace.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VypsanyTerminDTO {
    private int vypsanyTerminId;
    private LocalDateTime trvaniOd;
    private LocalDateTime trvaniDo;
    private String omezeniTerminu;
}
