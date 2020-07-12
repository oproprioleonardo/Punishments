package me.centralworks.lib;

import com.google.common.collect.Lists;

import java.util.List;

public class Contexts {

    protected static Contexts instance;
    protected List<Context> list = Lists.newArrayList();

    public static Contexts getInstance() {
        if (instance == null) instance = new Contexts();
        return instance;
    }

    public void add(Context context) {
        list.add(context);
    }

    public void remove(String proxy) {
        list.remove(get(proxy));
    }

    public boolean exists(String proxy) {
        return list.stream().anyMatch(context -> context.getWhoRequired().equalsIgnoreCase(proxy));
    }

    public Context get(String proxy) {
        return list.stream().filter(context -> context.getWhoRequired().equalsIgnoreCase(proxy)).findFirst().get();
    }

    public void clear() {
        list.clear();
    }
}
