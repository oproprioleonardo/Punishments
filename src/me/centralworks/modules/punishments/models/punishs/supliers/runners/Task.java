package me.centralworks.modules.punishments.models.punishs.supliers.runners;

import com.google.common.collect.Maps;

import java.util.HashMap;

public class Task {

    protected static Task instance;
    protected HashMap<String, Run> hashMap = Maps.newHashMap();

    public static Task getInstance() {
        if (instance == null) instance = new Task();
        return instance;
    }

    public void add(String proxy, Run run) {
        hashMap.put(proxy, run);
    }

    public void replace(String proxy, Run run) {
        hashMap.replace(proxy, run);
    }

    public void remove(String proxy) {
        hashMap.remove(proxy);
    }

    public boolean exists(String proxy) {
        return hashMap.containsKey(proxy);
    }

    public Run get(String proxy) {
        return hashMap.get(proxy);
    }

    public void clear() {
        hashMap.clear();
    }

}
