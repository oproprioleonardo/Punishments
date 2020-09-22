package me.centralworks.bungee.modules.punishments.models.supliers.warns;

import com.google.common.collect.Lists;
import net.md_5.bungee.config.Configuration;

import java.util.List;

public class WarnLoader {

    private static List<WarnPunishment> wps = Lists.newArrayList();

    public WarnLoader() {
    }

    public static List<WarnPunishment> getWps() {
        return wps;
    }

    public void clear() {
        wps = Lists.newArrayList();
    }

    public void init(Configuration configuration) {
        final List<WarnPunishment> wp = Lists.newArrayList();
        for (String warn : configuration.getSection("Warns").getKeys()) {
            final WarnPunishment wpo = new WarnPunishment();
            wpo.setId(warn);
            wpo.setAmount(configuration.getInt("Warns." + warn + ".warns"));
            wpo.setCommand(configuration.getString("Warns." + warn + ".command"));
            wp.add(wpo);
        }
        wps = wp;
    }

}
