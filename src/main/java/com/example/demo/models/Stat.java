package com.example.demo.models;

import java.util.*;

public class Stat {
    private int id;
    private Map<String, Double> info= new HashMap<>();

    private List<String> team = Arrays.asList(new String[] { "sorryihavenoname", "tygagamer", "ALExANDROss", "76561198138688894", "76561198160302549"});

    private long time;

    public Stat() { }

    public void add(String id, double rate) {
        if(team.contains(id)) {
            info.put(id, rate);
        }
    }

    public boolean check() {
        return info.size() >= 4;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean compare(long time) {
        return this.time == time;
    }
}
