package com.example.nnui_sem_prace.dataRunners;

import com.example.nnui_sem_prace.Utils.DatumoveNastroje;
import com.example.nnui_sem_prace.enums.STAV_REZERVACE;
import com.example.nnui_sem_prace.model.RezervaceTerminu;
import com.example.nnui_sem_prace.model.Role;
import com.example.nnui_sem_prace.model.Uzivatel;
import com.example.nnui_sem_prace.model.VypsanyTermin;
import com.example.nnui_sem_prace.service.RezervaceTerminuService;
import com.example.nnui_sem_prace.service.RoleService;
import com.example.nnui_sem_prace.service.UzivatelService;
import com.example.nnui_sem_prace.service.VypsaneTerminyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {

    private RezervaceTerminuService rezervaceTerminuService;
    private UzivatelService uzivatelService;
    private VypsaneTerminyService vypsaneTerminyService;
    private RoleService roleService;

    @Autowired
    private Environment env;

    @Autowired
    public DataLoader(RezervaceTerminuService rezervaceTerminuService, UzivatelService uzivatelService, VypsaneTerminyService vypsaneTerminyService, RoleService roleService) {
        this.rezervaceTerminuService = rezervaceTerminuService;
        this.uzivatelService = uzivatelService;
        this.vypsaneTerminyService = vypsaneTerminyService;
        this.roleService = roleService;
    }

    //1,2000-04-06 18:42:29.000000,pacient1password,Prvni,Pacient,pacient1
    //2,2010-06-01 15:42:29.000000,pacient2password,Druhy,Pacient2,pacient2
    public void run(ApplicationArguments args) {
        String ddlAuto = env.getProperty("spring.jpa.hibernate.ddl-auto");
        if (!"create".equals(ddlAuto)) {
            System.out.println("DDL auto není nastaveno na create, runner nepoběží");
            return;
        }
        System.out.println("Spoustim runner");
        System.out.println("DB source: " + env.getProperty("spring.datasource.url"));

        try {
            List<Uzivatel> uzivatele = new ArrayList<>();
            Uzivatel uzivatel1 = uzivatelService.save(new Uzivatel("pacient1", "pacient1password", "Prvni", "Pacient", DatumoveNastroje.prevedDatumNaLocalDate("11-02-2000"), new ArrayList<>()));
            Uzivatel uzivatel2 = uzivatelService.save(new Uzivatel("pacient2", "pacient2password", "Druhy", "Pacient", DatumoveNastroje.prevedDatumNaLocalDate("22-05-2010"), new ArrayList<>()));

            VypsanyTermin vypsanyTermin1 = vypsaneTerminyService.save(new VypsanyTermin(DatumoveNastroje.prevedDatumNaLocalDateTime("11-05-2024 10:30:00"), DatumoveNastroje.prevedDatumNaLocalDateTime("11-05-2024 11:00:00"), "Terminy v jeden den 1", new ArrayList<>()));
            VypsanyTermin vypsanyTermin2 = vypsaneTerminyService.save(new VypsanyTermin(DatumoveNastroje.prevedDatumNaLocalDateTime("11-05-2024 11:00:00"), DatumoveNastroje.prevedDatumNaLocalDateTime("11-05-2024 11:30:00"), "Terminy v jeden den 2", new ArrayList<>()));
            VypsanyTermin vypsanyTermin3 = vypsaneTerminyService.save(new VypsanyTermin(DatumoveNastroje.prevedDatumNaLocalDateTime("11-05-2024 11:30:00"), DatumoveNastroje.prevedDatumNaLocalDateTime("11-05-2024 12:00:00"), "Terminy v jeden den 3", new ArrayList<>()));
            VypsanyTermin vypsanyTermin4 = vypsaneTerminyService.save(new VypsanyTermin(DatumoveNastroje.prevedDatumNaLocalDateTime("12-05-2024 11:00:00"), DatumoveNastroje.prevedDatumNaLocalDateTime("12-05-2024 11:30:00"), "Termin 2 omezeni", new ArrayList<>()));
            VypsanyTermin vypsanyTermin5 = vypsaneTerminyService.save(new VypsanyTermin(DatumoveNastroje.prevedDatumNaLocalDateTime("13-05-2024 12:30:00"), DatumoveNastroje.prevedDatumNaLocalDateTime("13-05-2024 13:00:00"), "Dalsi omezeni", new ArrayList<>()));
            VypsanyTermin vypsanyTermin6 = vypsaneTerminyService.save(new VypsanyTermin(DatumoveNastroje.prevedDatumNaLocalDateTime("10-05-2024 13:00:00"), DatumoveNastroje.prevedDatumNaLocalDateTime("14-05-2024 13:30:00"), "Leasi", new ArrayList<>()));

            RezervaceTerminu rezervaceTerminu1 = rezervaceTerminuService.save(new RezervaceTerminu(STAV_REZERVACE.POTVRZENA, "OK", uzivatel1, vypsanyTermin1));
            RezervaceTerminu rezervaceTerminu4 = rezervaceTerminuService.save(new RezervaceTerminu(STAV_REZERVACE.NEPOTVRZENA, "OK", uzivatel1, vypsanyTermin6));
            RezervaceTerminu rezervaceTerminu2 = rezervaceTerminuService.save(new RezervaceTerminu(STAV_REZERVACE.POTVRZENA, "OK", uzivatel2, vypsanyTermin2));
            RezervaceTerminu rezervaceTerminu3 = rezervaceTerminuService.save(new RezervaceTerminu(STAV_REZERVACE.NEPOTVRZENA, null, uzivatel2, vypsanyTermin4));

            LocalDateTime startTime = DatumoveNastroje.prevedDatumNaLocalDateTime("14-05-2024 08:00:00");
            LocalDateTime endTime = startTime.plusMinutes(30);

            for (int i = 0; i < 500; i++) {
                VypsanyTermin vypsanyTermin = new VypsanyTermin(startTime, endTime, "Dalsi termin", new ArrayList<>());
                vypsaneTerminyService.save(vypsanyTermin);

                // Inkrementuj časy o 30 minut pro další termíny
                startTime = endTime;
                endTime = startTime.plusMinutes(30);

                if (endTime.getHour() >= 17 && endTime.getMinute() >= 30) {
                    startTime = startTime.plusDays(1).withHour(8).withMinute(0);
                    endTime = startTime.plusMinutes(30);
                }
            }

            uzivatele.add(uzivatel1);
            uzivatele.add(uzivatel2);

            Role role = new Role("app", uzivatele);
            uzivatel1.getRoles().add(role);
            uzivatel2.getRoles().add(role);
            role = roleService.save(role);
            uzivatel1 = uzivatelService.save(uzivatel1);
            uzivatel2 = uzivatelService.save(uzivatel2);
            System.out.println("");
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println("Runner dobehl");
    }
}
