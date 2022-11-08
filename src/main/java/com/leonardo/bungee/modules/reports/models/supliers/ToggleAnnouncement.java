package com.leonardo.bungee.modules.reports.models.supliers;

import com.google.common.collect.Maps;
import com.leonardo.bungee.Main;
import com.leonardo.bungee.modules.reports.enums.Permission;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.HashMap;

public class ToggleAnnouncement implements Listener {

    private static ToggleAnnouncement instance;
    private final HashMap<String, Boolean> map = Maps.newHashMap();

    public static ToggleAnnouncement getInstance() {
        if (instance == null) instance = new ToggleAnnouncement();
        return instance;
    }

    @EventHandler
    public void join(PostLoginEvent e) {
        final ProxiedPlayer p = e.getPlayer();
        if (p.hasPermission(Permission.REPORTS.getPermission()) || p.hasPermission(Permission.ADMIN.getPermission())) {
            ToggleAnnouncement.getInstance().add(e.getPlayer().getName());
        }
    }

    @EventHandler
    public void remove(PlayerDisconnectEvent e) {
        ToggleAnnouncement.getInstance().remove(e.getPlayer().getName());
    }

    public HashMap<String, Boolean> getMap() {
        return map;
    }

    public void add(String name) {
        map.put(name.toLowerCase(), true);
    }

    public void remove(String name) {
        if (exists(name)) map.remove(name.toLowerCase());
    }

    public boolean exists(String name) {
        return map.containsKey(name.toLowerCase());
    }

    public boolean get(String name) {
        if (Permission.hasPermission(Main.getInstance().getProxy().getPlayer(name), Permission.REPORTS) && !exists(name)) {
            add(name);
        }
        return map.get(name.toLowerCase());
    }

    public void replace(String name, Boolean value) {
        map.replace(name.toLowerCase(), value);
    }

    public void update(String name) {
        if (exists(name.toLowerCase())) replace(name, !get(name));
        else add(name);
    }
}
