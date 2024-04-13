package com.example.nnui_sem_prace;

import com.example.nnui_sem_prace.Utils.DatumoveNastroje;
import com.example.nnui_sem_prace.controller.RezervaceTerminuController;
import com.example.nnui_sem_prace.dto.RezervaceTerminuDTO;
import com.example.nnui_sem_prace.enums.STAV_REZERVACE;
import com.example.nnui_sem_prace.model.RezervaceTerminu;
import com.example.nnui_sem_prace.model.Uzivatel;
import com.example.nnui_sem_prace.model.VypsanyTermin;
import com.example.nnui_sem_prace.service.RezervaceTerminuService;
import com.example.nnui_sem_prace.service.UzivatelService;
import com.example.nnui_sem_prace.service.VypsaneTerminyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.TransactionalException;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = NnuiSemPraceApplication.class)
@AutoConfigureMockMvc
public class IntegracniTests {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private RezervaceTerminuService rezervaceTerminuService;

    @Autowired
    private UzivatelService uzivatelService;

    @Autowired
    private VypsaneTerminyService vypsaneTerminyService;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtDecoder jwtDecoder;


    @Test
    @Transactional
    public void vytvorRezervaciTerminuTest() throws Exception {
        int userId = 1;
        int vypsanyTerminId = 6;
        Uzivatel uzivatel = uzivatelService.getUzivatelById(userId);

        String requestBody = objectMapper.writeValueAsString(vypsanyTerminId);

        String authStr = "pacient1:pacient1password";
        String base64Creds = Base64.getEncoder().encodeToString(authStr.getBytes());

        // create headers
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + base64Creds);

        String jwtToken = mvc.perform(post("/security/login").with(csrf()).headers(headers)).andReturn().getResponse().getContentAsString();

        mvc.perform(
                post("/terminyUzivatele/"+ userId + "/vytvorRezervaci")
                        .with(csrf()).contentType(MediaType.APPLICATION_JSON).content(requestBody)
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uzivatel.username", is(uzivatel.getUsername())))
                .andExpect(jsonPath("$.uzivatel.uzivatelId", is(uzivatel.getUzivatelId())))
                .andExpect(jsonPath("$.uzivatel.rezervovaneTerminy.length()", is(uzivatel.getRezervovaneTerminy().size())))
                .andExpect(jsonPath("$.rezervaceTerminuId", is(uzivatel.getRezervovaneTerminy().get(uzivatel.getRezervovaneTerminy().size() - 1).getRezervaceTerminuId())))
                .andExpect(jsonPath("$.stavRezervace", is(uzivatel.getRezervovaneTerminy().get(uzivatel.getRezervovaneTerminy().size() - 1).getStavRezervace().toString())));
    }
}
