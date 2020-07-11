package me.centralworks.modules.reports.models.supliers.cached;

import com.google.common.collect.Lists;
import me.centralworks.modules.reports.models.supliers.Context;

import java.util.List;

public class Contexts {

    protected static Contexts instance;
    public List<Context> contexts = Lists.newArrayList();

    public static Contexts getInstance() {
        if (instance == null) instance = new Contexts();
        return instance;
    }

    public void add(Context context) {
        contexts.add(context);
    }

    public void remove(Context context) {
        contexts.remove(context);
    }

    public Context get(String victim) {
        return contexts.stream().filter(context -> context.getVictim().equalsIgnoreCase(victim)).findFirst().get();
    }

    public boolean has(String victim) {
        return contexts.stream().anyMatch(context -> context.getVictim().equalsIgnoreCase(victim));
    }

    public void remove(String victim) {
        remove(get(victim));
    }
}

