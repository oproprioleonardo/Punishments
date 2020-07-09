package me.centralworks.modules.punishments.models.punishs.supliers.cached;

import com.google.common.collect.Maps;
import me.centralworks.modules.punishments.models.punishs.supliers.Context;

import java.util.HashMap;

public class Contexts {

    protected static Contexts instance;
    protected HashMap<String, Context> hashMap = Maps.newHashMap();

    public static Contexts getInstance() {
        if (instance == null) instance = new Contexts();
        return instance;
    }

    public void add(String proxy, Context context) {
        hashMap.put(proxy, context);
    }

    public void replace(String proxy, Context context) {
        hashMap.replace(proxy, context);
    }

    public void remove(String proxy) {
        hashMap.remove(proxy);
    }

    public boolean exists(String proxy) {
        return hashMap.containsKey(proxy);
    }

    public Context get(String proxy) {
        return hashMap.get(proxy);
    }

    public void clear() {
        hashMap.clear();
    }

}
