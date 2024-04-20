package com.example.nnui_sem_prace.Utils;

import org.springframework.data.domain.Sort;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RadiciNastroje {
    public static Sort getSort(List<String> sortBy) {
        if (sortBy == null || sortBy.isEmpty()) {
            return Sort.unsorted();
        }

        List<Sort.Order> smerRazeni = new ArrayList<>();
        for(int i = 0; i < sortBy.size(); i+=2){
            String property = sortBy.get(i);
            String direction = sortBy.get(i+1);
            smerRazeni.add(new Sort.Order(Sort.Direction.fromString(direction), property));
        }

        return Sort.by(smerRazeni);
    }

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
