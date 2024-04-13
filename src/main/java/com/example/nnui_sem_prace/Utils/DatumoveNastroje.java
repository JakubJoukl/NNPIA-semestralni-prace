package com.example.nnui_sem_prace.Utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class DatumoveNastroje {
    private static SimpleDateFormat parserDatumuddMMyyyy = new SimpleDateFormat("dd-MM-yyyy");
    private static SimpleDateFormat parserDatumuddMMyyyyHHmmss = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    public static LocalDate prevedDatumNaLocalDate(String denDDMMRRRR) throws ParseException {
        LocalDate den = parserDatumuddMMyyyy.parse(denDDMMRRRR).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
        return den;
    }
    public static LocalDateTime prevedDatumNaLocalDateTime(String denDDMMRRRR) throws ParseException {
        LocalDateTime den = parserDatumuddMMyyyyHHmmss.parse(denDDMMRRRR).toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return den;
    }
}
