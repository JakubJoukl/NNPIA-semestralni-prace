package com.example.nnui_sem_prace;

import com.example.nnui_sem_prace.service.RezervaceTerminuService;
import com.example.nnui_sem_prace.service.UzivatelService;
import com.example.nnui_sem_prace.service.VypsaneTerminyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.MOCK,
        classes = NnuiSemPraceApplication.class)
@AutoConfigureMockMvc
class NnuiSemPraceApplicationTests {

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
    void contextLoads() {

    }
}
