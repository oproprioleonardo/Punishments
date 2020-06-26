package me.centralworks.punishments.punishs.supliers.runners;

import com.google.common.collect.Maps;

import java.util.HashMap;

public class Task {

    protected static Task instance;
    protected HashMap<String, Runner> hashMap = Maps.newHashMap();

    public static Task getInstance() {
        if (instance == null) instance = new Task();
        return instance;
    }

    public void add(String proxy, Runner runner) {
        hashMap.put(proxy, runner);
    }

    public void replace(String proxy, Runner runner) {
        hashMap.replace(proxy, runner);
    }

    public void remove(String proxy) {
        hashMap.remove(proxy);
    }

    public boolean exists(String proxy) {
        return hashMap.containsKey(proxy);
    }

    public Runner get(String proxy) {
        return hashMap.get(proxy);
    }

    public void clear() {
        hashMap.clear();
    }

}
