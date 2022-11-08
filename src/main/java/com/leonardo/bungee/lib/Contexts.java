package com.leonardo.bungee.lib;

import com.google.common.collect.Lists;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class Contexts implements Listener {

    private static Contexts instance;
    private final List<Context> list = Lists.newArrayList();

    public static Contexts getInstance() {
        if (instance == null) instance = new Contexts();
        return instance;
    }

    public void add(Context context) {
        list.add(context);
    }

    public void remove(String proxy) {
        if (exists(proxy)) list.remove(get(proxy));
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

    @EventHandler
    public void exit(PlayerDisconnectEvent e) {
        remove(e.getPlayer().getName());
    }
}
