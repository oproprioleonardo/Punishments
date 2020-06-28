package me.centralworks.punishments.listeners.withAddressIP.join;

import com.google.common.collect.Lists;
import me.centralworks.punishments.Main;
import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.lib.LongMessage;
import me.centralworks.punishments.punishs.Punishment;
import me.centralworks.punishments.punishs.supliers.cached.MutedPlayers;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.util.List;

public class MuteIPListener implements Listener {

    @EventHandler
    public void js(PostLoginEvent e) {
        BungeeCord.getInstance().getScheduler().runAsync(Main.getInstance(), () -> {
            try {
                final ProxiedPlayer connection = e.getPlayer();
                final Punishment punishment = General.getGeneralLib().easyInstance(connection.getName(), connection.getUniqueId().toString());
                punishment.setIp(connection.getAddress().getAddress().getHostAddress());
                if (punishment.existsByAddress()) {
                    final General generalLib = General.getGeneralLib();
                    final List<Punishment> instance = punishment.requireAllByAddress();
                    if (!generalLib.hasActivePunishment(instance) || !generalLib.hasPunishmentMute(instance)) return;
                    final Punishment p = generalLib.getAllMuteP(instance).get(0);
                    generalLib.getFunctionMuteIfOn().accept(p);

                    final List<String> collect = generalLib.applyPlaceHolders(Lists.newArrayList(new LongMessage("Runners.mute-alert").getStringList()), p);
                    final ComponentBuilder componentBuilder = new ComponentBuilder("");
                    collect.forEach(componentBuilder::append);
                    connection.sendMessage(componentBuilder.create());
                    if (!MutedPlayers.getInstance().exists(p.getIdentifier()))
                        new MutedPlayers.MuteObject(p.getIdentifier(), p.getId(), p.getData().getStartedAt(), p.getData().getFinishAt(), p.getData().isPermanent(), p.getIp()).save();
                }
            } catch (NullPointerException ignored) {
            }
        });
    }

}
