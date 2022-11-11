package com.example.apiproject;

import java.util.Comparator;

public class  Sort {

    public static class SortByVolume implements Comparator<Mask>{
    @Override
    public int compare(Mask message, Mask t1) {
        return message.getVolume().compareTo(t1.getVolume());
    }
}
    public static class SortByCountry_of_origin implements Comparator<Mask> {

        @Override
        public int compare(Mask message, Mask t1) {
            return message.getCountry_of_origin().compareTo(t1.getCountry_of_origin());
        }
    }
    public static class SortByName_toothpaste implements Comparator<Mask> {
        @Override
        public int compare(Mask message, Mask t1) {
            return message.getName_toothpaste().compareTo(t1.getName_toothpaste());
        }
    }
}