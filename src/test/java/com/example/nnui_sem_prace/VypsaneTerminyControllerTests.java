package com.example.nnui_sem_prace;

import com.example.nnui_sem_prace.Utils.DatumoveNastroje;
import com.example.nnui_sem_prace.controller.RezervaceTerminuController;
import com.example.nnui_sem_prace.controller.VypsaneTerminyController;
import com.example.nnui_sem_prace.dto.RezervaceTerminuDTO;
import com.example.nnui_sem_prace.enums.STAV_REZERVACE;
import com.example.nnui_sem_prace.model.RezervaceTerminu;
import com.example.nnui_sem_prace.model.Uzivatel;
import com.example.nnui_sem_prace.model.VypsanyTermin;
import com.example.nnui_sem_prace.service.RezervaceTerminuService;
import com.example.nnui_sem_prace.service.UzivatelService;
import com.example.nnui_sem_prace.service.VypsaneTerminyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = VypsaneTerminyController.class)
@Import(VypsaneTerminyController.class)
//@SpringBootTest(classes = TestSecurityConfig.class)
@WithMockUser(username = "pacient1", password = "pacient1Password")
public class VypsaneTerminyControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private RezervaceTerminuService rezervaceTerminuService;

    @MockBean
    private VypsaneTerminyService vypsaneTerminyService;

    @MockBean
    private UzivatelService uzivatelService;

    @MockBean
    JwtDecoder jwtDecoder;

    @Test
    void vytvorDuplicitniRezervaciNaDen() throws Exception {
        //AppUser mockUser = mock(AppUser.class);
        int userId = 1;
        int vypsanyTerminId = 1;
        Uzivatel mockUser = new Uzivatel("pacient1", "pacient1password", "Prvni", "Pacient", DatumoveNastroje.prevedDatumNaLocalDate("11-02-2000"), new ArrayList<>());
        mockUser.setUzivatelId(1);

        LocalDateTime datumRezervace = DatumoveNastroje.prevedDatumNaLocalDateTime("11-05-2024 10:30:00");

        VypsanyTermin vypsanyTermin1 = new VypsanyTermin(datumRezervace, DatumoveNastroje.prevedDatumNaLocalDateTime("11-05-2024 11:00:00"), "Terminy v jeden den 1", new ArrayList<>());
        RezervaceTerminu rezervaceTerminu1 = new RezervaceTerminu(STAV_REZERVACE.NEPOTVRZENA, null, mockUser, vypsanyTermin1);
        rezervaceTerminu1.setRezervaceTerminuId(vypsanyTerminId);
        mockUser.getRezervovaneTerminy().add(rezervaceTerminu1);

        List<RezervaceTerminuDTO> rezervaceTerminuDTOList = new ArrayList<>();
        rezervaceTerminuDTOList.add(new RezervaceTerminuDTO(vypsanyTerminId, STAV_REZERVACE.NEPOTVRZENA, null, mockUser, vypsanyTermin1));

        given(uzivatelService.getUzivatelById(userId)).willReturn(mockUser);
        given(vypsaneTerminyService.getVypsanyTerminById(vypsanyTerminId)).willReturn(vypsanyTermin1);
        given(rezervaceTerminuService.prevedListRezervaciTerminuNaListDTO(mockUser.getRezervovaneTerminy())).willReturn(rezervaceTerminuDTOList);
        given(rezervaceTerminuService.maUzivatelRezervaciNaDanyDen(mockUser, datumRezervace)).willReturn(true);
        given(uzivatelService.loadUserByUsername("pacient1")).willReturn(mockUser);

        String requestBody = objectMapper.writeValueAsString(vypsanyTerminId);

        mockMvc.perform(post("/vypsaneTerminy/vytvorRezervaci").with(csrf()).contentType(MediaType.APPLICATION_JSON).content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(new MediaType(MediaType.TEXT_PLAIN, StandardCharsets.UTF_8)))
                .andExpect(content().string("Rezervace nebyla vytvorena - uzivatel na dany termin jiz ma rezervaci"))
        ;
    }

    @Test
    void vytvorNeduplicitniRezervaciNaDen() throws Exception {
        //AppUser mockUser = mock(AppUser.class);
        int userId = 1;
        int vypsanyTerminId = 1;
        Uzivatel mockUser = new Uzivatel("pacient1", "pacient1password", "Prvni", "Pacient", DatumoveNastroje.prevedDatumNaLocalDate("11-02-2000"), new ArrayList<>());
        mockUser.setUzivatelId(1);

        LocalDateTime datumRezervace = DatumoveNastroje.prevedDatumNaLocalDateTime("11-05-2024 10:30:00");

        VypsanyTermin vypsanyTermin1 = new VypsanyTermin(datumRezervace, DatumoveNastroje.prevedDatumNaLocalDateTime("11-05-2024 11:00:00"), "Terminy v jeden den 1", new ArrayList<>());
        RezervaceTerminu rezervaceTerminu1 = new RezervaceTerminu(STAV_REZERVACE.NEPOTVRZENA, null, mockUser, vypsanyTermin1);
        rezervaceTerminu1.setRezervaceTerminuId(vypsanyTerminId);
        mockUser.getRezervovaneTerminy().add(rezervaceTerminu1);

        List<RezervaceTerminuDTO> rezervaceTerminuDTOList = new ArrayList<>();
        rezervaceTerminuDTOList.add(new RezervaceTerminuDTO(vypsanyTerminId, STAV_REZERVACE.NEPOTVRZENA, null, mockUser, vypsanyTermin1));

        given(uzivatelService.getUzivatelById(userId)).willReturn(mockUser);
        given(vypsaneTerminyService.getVypsanyTerminById(vypsanyTerminId)).willReturn(vypsanyTermin1);
        given(rezervaceTerminuService.prevedListRezervaciTerminuNaListDTO(mockUser.getRezervovaneTerminy())).willReturn(rezervaceTerminuDTOList);
        given(rezervaceTerminuService.maUzivatelRezervaciNaDanyDen(mockUser, datumRezervace)).willReturn(false);
        given(rezervaceTerminuService.prevedRezervaciTerminuNaDTO(rezervaceTerminu1)).willReturn(new RezervaceTerminuDTO(vypsanyTerminId, STAV_REZERVACE.NEPOTVRZENA, null, mockUser, vypsanyTermin1));
        given(rezervaceTerminuService.vytvorRezervaci(mockUser, vypsanyTermin1)).willReturn(rezervaceTerminu1);
        given(uzivatelService.loadUserByUsername("pacient1")).willReturn(mockUser);

        String requestBody = objectMapper.writeValueAsString(vypsanyTerminId);

        mockMvc.perform(
                        post("/vypsaneTerminy/vytvorRezervaci")
                                .with(csrf())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody)
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uzivatel.username", is(mockUser.getUsername())))
                .andExpect(jsonPath("$.uzivatel.uzivatelId", is(mockUser.getUzivatelId())))
                .andExpect(jsonPath("$.uzivatel.rezervovaneTerminy.length()", is(mockUser.getRezervovaneTerminy().size())))
                .andExpect(jsonPath("$.rezervaceTerminuId", is(mockUser.getRezervovaneTerminy().get(0).getRezervaceTerminuId())))
                .andExpect(jsonPath("$.stavRezervace", is(mockUser.getRezervovaneTerminy().get(0).getStavRezervace().toString())))
                .andExpect(jsonPath("$.rezervaceTerminuId", is(rezervaceTerminu1.getRezervaceTerminuId())))
                .andExpect(jsonPath("$.poznamkaLekare", is(rezervaceTerminu1.getPoznamkaLekare())))
        ;
    }
}
