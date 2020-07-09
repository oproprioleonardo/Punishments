package me.centralworks.modules.punishments.listeners.withAddressIP.join;

import com.google.common.collect.Lists;
import me.centralworks.lib.General;
import me.centralworks.lib.LongMessage;
import me.centralworks.modules.punishments.models.punishs.Punishment;
import me.centralworks.modules.punishments.models.punishs.supliers.cached.MutedPlayers;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class MuteIPListener implements Listener {

    @EventHandler
    public void js(PostLoginEvent e) {
        final ProxiedPlayer connection = e.getPlayer();
        final Punishment punishment = General.getGeneralLib().easyInstance();
        punishment.setIp(connection.getAddress().getAddress().getHostAddress());
        if (punishment.existsByAddress()) {
            final General generalLib = General.getGeneralLib();
            final List<Punishment> instance = punishment.requireAllByAddress();
            if (!generalLib.hasActivePunishment(instance) || !generalLib.hasPunishmentMute(instance)) return;
            final Punishment p = generalLib.getAllMutedPActive(instance).get(0);
            final List<String> collect = generalLib.applyPlaceHolders(Lists.newArrayList(new LongMessage("Runners.mute-alert").getStringList()), p);
            final ComponentBuilder componentBuilder = new ComponentBuilder("");
            collect.forEach(componentBuilder::append);
            connection.sendMessage(componentBuilder.create());
            if (!MutedPlayers.getInstance().exists(p.getPrimaryIdentifier()))
                new MutedPlayers.MuteObject(p.getPrimaryIdentifier(), p.getId(), p.getData().getStartedAt(), p.getData().getFinishAt(), p.getData().isPermanent(), p.getIp()).save();
        }

    }

}