package com.leonardo.bungee.lib;

import net.md_5.bungee.api.connection.ProxiedPlayer;

public interface Context {

    String getWhoRequired();

    void run();

    void execute();

    void applyOtherInformation(ProxiedPlayer informer);

    int getPlugin();
}
