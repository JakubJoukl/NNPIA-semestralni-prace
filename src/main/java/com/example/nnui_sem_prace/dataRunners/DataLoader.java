package com.example.nnui_sem_prace.dataRunners;

import com.example.nnui_sem_prace.Utils.DatumoveNastroje;
import com.example.nnui_sem_prace.enums.STAV_REZERVACE;
import com.example.nnui_sem_prace.model.RezervaceTerminu;
import com.example.nnui_sem_prace.model.Uzivatel;
import com.example.nnui_sem_prace.model.VypsanyTermin;
import com.example.nnui_sem_prace.service.RezervaceTerminuService;
import com.example.nnui_sem_prace.service.UzivatelService;
import com.example.nnui_sem_prace.service.VypsaneTerminyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.util.ArrayList;

@Component
public class DataLoader implements ApplicationRunner {

    private RezervaceTerminuService rezervaceTerminuService;
    private UzivatelService uzivatelService;
    private VypsaneTerminyService vypsaneTerminyService;

    @Autowired
    private Environment env;

    @Autowired
    public DataLoader(RezervaceTerminuService rezervaceTerminuService, UzivatelService uzivatelService, VypsaneTerminyService vypsaneTerminyService) {
        this.rezervaceTerminuService = rezervaceTerminuService;
        this.uzivatelService = uzivatelService;
        this.vypsaneTerminyService = vypsaneTerminyService;
    }

    //1,2000-04-06 18:42:29.000000,pacient1password,Prvni,Pacient,pacient1
    //2,2010-06-01 15:42:29.000000,pacient2password,Druhy,Pacient2,pacient2
    public void run(ApplicationArguments args) {
        System.out.println("Spoustim runner");
        System.out.println("DB source: " + env.getProperty("spring.datasource.url"));

        try {
            Uzivatel uzivatel1 = uzivatelService.save(new Uzivatel("pacient1", "pacient1password", "Prvni", "Pacient", DatumoveNastroje.prevedDatumNaLocalDate("11-02-2000"), new ArrayList<>()));
            Uzivatel uzivatel2 = uzivatelService.save(new Uzivatel("pacient2", "pacient2password", "Druhy", "Pacient", DatumoveNastroje.prevedDatumNaLocalDate("22-05-2010"), new ArrayList<>()));
            VypsanyTermin vypsanyTermin1 = vypsaneTerminyService.save(new VypsanyTermin(DatumoveNastroje.prevedDatumNaLocalDateTime("11-05-2024 10:30:00"), DatumoveNastroje.prevedDatumNaLocalDateTime("11-05-2024 11:00:00"), "Terminy v jeden den 1", new ArrayList<>()));
            VypsanyTermin vypsanyTermin2 = vypsaneTerminyService.save(new VypsanyTermin(DatumoveNastroje.prevedDatumNaLocalDateTime("11-05-2024 11:00:00"), DatumoveNastroje.prevedDatumNaLocalDateTime("11-05-2024 11:30:00"), "Terminy v jeden den 2", new ArrayList<>()));
            VypsanyTermin vypsanyTermin3 = vypsaneTerminyService.save(new VypsanyTermin(DatumoveNastroje.prevedDatumNaLocalDateTime("11-05-2024 11:30:00"), DatumoveNastroje.prevedDatumNaLocalDateTime("11-05-2024 12:00:00"), "Terminy v jeden den 3", new ArrayList<>()));
            VypsanyTermin vypsanyTermin4 = vypsaneTerminyService.save(new VypsanyTermin(DatumoveNastroje.prevedDatumNaLocalDateTime("12-05-2024 11:00:00"), DatumoveNastroje.prevedDatumNaLocalDateTime("12-05-2024 11:30:00"), "Termin 2 omezeni", new ArrayList<>()));
            VypsanyTermin vypsanyTermin5 = vypsaneTerminyService.save(new VypsanyTermin(DatumoveNastroje.prevedDatumNaLocalDateTime("13-05-2024 12:30:00"), DatumoveNastroje.prevedDatumNaLocalDateTime("13-05-2024 13:00:00"), "Dalsi omezeni", new ArrayList<>()));
            VypsanyTermin vypsanyTermin6 = vypsaneTerminyService.save(new VypsanyTermin(DatumoveNastroje.prevedDatumNaLocalDateTime("14-05-2024 13:00:00"), DatumoveNastroje.prevedDatumNaLocalDateTime("14-05-2024 13:30:00"), "Leasi", new ArrayList<>()));
            RezervaceTerminu rezervaceTerminu1 = rezervaceTerminuService.save(new RezervaceTerminu(STAV_REZERVACE.POTVRZENA, "OK", uzivatel1, vypsanyTermin1));
            RezervaceTerminu rezervaceTerminu2 = rezervaceTerminuService.save(new RezervaceTerminu(STAV_REZERVACE.POTVRZENA, "OK", uzivatel2, vypsanyTermin2));
            RezervaceTerminu rezervaceTerminu3 = rezervaceTerminuService.save(new RezervaceTerminu(STAV_REZERVACE.NEPOTVRZENA, null, uzivatel2, vypsanyTermin4));
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println("Runner dobehl");
    }
}
