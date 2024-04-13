package com.example.nnui_sem_prace.Utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RadiciNastroje {
    //TODO funguje
    public static <K> List<K> vratSerazenyListDlePole(String nazevPoleProRazeni, List<K> prvkyKSerazeni, boolean asc){
        if(prvkyKSerazeni.size() <= 0) return prvkyKSerazeni;
        Class<?> kClass = prvkyKSerazeni.get(0).getClass();
        Field radiciPole;
        try {
            radiciPole = kClass.getField(nazevPoleProRazeni);
            if(radiciPole.get(0).getClass().isAssignableFrom(Comparable.class));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Trida " + kClass.getSimpleName() + " takove pole nema");
        } catch (IllegalAccessException | ClassCastException e) {
            throw new RuntimeException("Radici pole neimplementuje Comparable");
        }

        List<K> serazenePole = new ArrayList<>(prvkyKSerazeni);

        if(asc) {
            serazenePole.sort((o1, o2) -> {
                try {
                    return ((Comparable)radiciPole.get(o1)).compareTo(radiciPole.get(o2));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Pole neni dostupne");
                }
            });
        } else {
            serazenePole.sort((o1, o2) -> {
                try {
                    return ((Comparable)radiciPole.get(o2)).compareTo(radiciPole.get(o1));
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Pole neni dostupne");
                }
            });
        }

        return serazenePole;
    }
}
