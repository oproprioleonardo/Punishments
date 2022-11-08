package com.leonardo.bungee.modules.punishments.listeners.withAddressIP.join;

import com.google.common.collect.Lists;
import com.leonardo.bungee.lib.Functionalities;
import com.leonardo.bungee.lib.LongMessage;
import com.leonardo.bungee.modules.punishments.models.Punishment;
import com.leonardo.bungee.modules.punishments.models.supliers.Request;
import com.leonardo.bungee.modules.punishments.models.supliers.CheckUp;
import com.leonardo.bungee.modules.punishments.models.supliers.Filter;
import com.leonardo.bungee.modules.punishments.models.supliers.PlaceHolder;
import com.leonardo.bungee.modules.punishments.models.supliers.cached.MutedPlayers;
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
        final Punishment punishment = Functionalities.easyInstance();
        punishment.setIp(connection.getAddress().getAddress().getHostAddress());
        final Request request = new Request(punishment);
        if (request.existsByAddress()) {
            final List<Punishment> instance = request.requireAllByAddress();
            final CheckUp checkUp = new CheckUp(instance);
            if (!checkUp.hasActivePunishment() || !checkUp.hasPunishmentMute()) return;
            final Punishment p = new Filter(instance).getAllMutedPActive().get(0);
            final List<String> collect = new PlaceHolder(Lists.newArrayList(new LongMessage("Runners.mute-alert").getStringList()), p).applyPlaceHolders();
            final ComponentBuilder componentBuilder = new ComponentBuilder("");
            collect.forEach(componentBuilder::append);
            connection.sendMessage(componentBuilder.create());
            if (!MutedPlayers.getInstance().exists(p.getPrimaryIdentifier()))
                new MutedPlayers.MuteObject(p.getPrimaryIdentifier(), p.getId(), p.getData().getStartedAt(), p.getData().getFinishAt(), p.getData().isPermanent(), p.getIp()).save();
        }

    }

}
