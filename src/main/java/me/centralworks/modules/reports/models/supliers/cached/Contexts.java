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
}

