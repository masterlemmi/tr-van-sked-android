package com.longganisacode.android.vanschedule.schedule;

import java.util.ArrayList;
import java.util.List;

public enum Direction {

    IN("office", "inbound", "in"), OUT("home", "outbound", "out"), BOTH("both");
    private List<String> synonyms = new ArrayList<>();

    Direction(String... synonyms) {
        for (String s : synonyms) {
            this.synonyms.add(s);
        }
    }

    public static Direction get(String s) {
        for (Direction d : values()) {
            if (d.synonyms.contains(s.toLowerCase()))
                return d;
        }
        throw new IllegalArgumentException(s + " is not a Known Direction");
    }
}
