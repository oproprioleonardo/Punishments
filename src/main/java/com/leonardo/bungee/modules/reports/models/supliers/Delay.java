package com.leonardo.bungee.modules.reports.models.supliers;

import com.google.common.collect.Lists;
import com.leonardo.bungee.Main;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Delay {

    private static Delay instance;
    public List<String> inDelay = Lists.newArrayList();

    public static Delay getInstance() {
        if (instance == null) instance = new Delay();
        return instance;
    }

    public void add(String name) {
        inDelay.add(name.toLowerCase());
        Main.getInstance().getProxy().getScheduler().schedule(Main.getInstance(), () -> remove(name.toLowerCase()), 5, TimeUnit.MINUTES);
    }

    public void remove(String name) {
        inDelay.remove(name.toLowerCase());
    }

    public boolean exists(String name) {
        return inDelay.contains(name.toLowerCase());
    }

}
