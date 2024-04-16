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
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RezervaceTerminuController.class)
@Import(RezervaceTerminuController.class)
//@SpringBootTest(classes = TestSecurityConfig.class)
@WithMockUser(username = "pacient1", password = "pacient1Password")
class RezervaceTerminuControllerTests {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private RezervaceTerminuService rezervaceTerminuService;

    @MockBean
    private UzivatelService uzivatelService;

    @MockBean
    JwtDecoder jwtDecoder;

    @Test
    void dejTerminyUzivateleTest() throws Exception {
        //AppUser mockUser = mock(AppUser.class);
        int userId = 1;
        Uzivatel mockUser = new Uzivatel("pacient1", "pacient1password", "Prvni", "Pacient", DatumoveNastroje.prevedDatumNaLocalDate("11-02-2000"), new ArrayList<>());
        mockUser.setUzivatelId(1);
        VypsanyTermin vypsanyTermin1 = new VypsanyTermin(DatumoveNastroje.prevedDatumNaLocalDateTime("11-05-2024 10:30:00"), DatumoveNastroje.prevedDatumNaLocalDateTime("11-05-2024 11:00:00"), "Terminy v jeden den 1", new ArrayList<>());
        RezervaceTerminu rezervaceTerminu1 = new RezervaceTerminu(STAV_REZERVACE.POTVRZENA, "OK", mockUser, vypsanyTermin1);
        rezervaceTerminu1.setRezervaceTerminuId(1);

        List<RezervaceTerminu> listRezervaceTerminu = new ArrayList<>();
        listRezervaceTerminu.add(rezervaceTerminu1);

        mockUser.getRezervovaneTerminy().add(rezervaceTerminu1);

        PageRequest pageRequest = PageRequest.of(0, 10);

        List<RezervaceTerminuDTO> rezervaceTerminuDTOList = new ArrayList<>();
        rezervaceTerminuDTOList.add(new RezervaceTerminuDTO(1, STAV_REZERVACE.POTVRZENA, "OK", mockUser, vypsanyTermin1));

        HashMap<String, Object> vracenaOdpoved = new HashMap<>();
        vracenaOdpoved.put("pocet", 1);
        vracenaOdpoved.put("data", rezervaceTerminuDTOList);

        given(uzivatelService.getUzivatelById(userId)).willReturn(mockUser);
        given(rezervaceTerminuService.prevedListRezervaciTerminuNaListDTO(mockUser.getRezervovaneTerminy())).willReturn(rezervaceTerminuDTOList);
        given(rezervaceTerminuService.dejVeskereTerminyUzivatele(userId, pageRequest, true)).willReturn(listRezervaceTerminu);
        given(rezervaceTerminuService.obalInformacemiOPoctu(mockUser, rezervaceTerminuDTOList, false)).willReturn(vracenaOdpoved);
        given(uzivatelService.loadUserByUsername("pacient1")).willReturn(mockUser);

        mockMvc.perform(get("/terminyUzivatele/?pageNumber=0&asc=true"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.data.[0].uzivatel.username", is(mockUser.getUsername())))
                .andExpect(jsonPath("$.data.[0].uzivatel.uzivatelId", is(mockUser.getUzivatelId())))
                .andExpect(jsonPath("$.data.[0].uzivatel.rezervovaneTerminy.length()", is(mockUser.getRezervovaneTerminy().size())))
                .andExpect(jsonPath("$.data.[0].rezervaceTerminuId", is(mockUser.getRezervovaneTerminy().get(0).getRezervaceTerminuId())))
                .andExpect(jsonPath("$.data.[0].stavRezervace", is(mockUser.getRezervovaneTerminy().get(0).getStavRezervace().toString())))
                .andExpect(jsonPath("$.pocet", is(1)))
        ;
    }
}
