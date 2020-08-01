package me.centralworks.bungee.modules.punishments.models.supliers;

import com.google.common.collect.Lists;
import me.centralworks.bungee.Main;
import me.centralworks.bungee.ModerationBot;
import me.centralworks.bungee.lib.LongMessage;
import me.centralworks.bungee.modules.punishments.models.Punishment;
import me.centralworks.bungee.modules.punishments.models.supliers.cached.AddressIP;
import me.centralworks.bungee.modules.punishments.models.supliers.cached.MutedPlayers;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class PFunctions {

    private static PFunctions instance;

    public static PFunctions get() {
        return instance == null ? instance = new PFunctions() : instance;
    }

    public Consumer<Punishment> getFunctionBanIfOn() {
        return punishment1 -> {
            final ProxiedPlayer punishmentPlayer = punishment1.getPlayer();
            final LongMessage longMessage = new LongMessage("Runners.ban-kick");
            final List<String> collect = new PlaceHolder(Lists.newArrayList(longMessage.getStringList()), punishment1).applyPlaceHolders();
            longMessage.setStringList(collect);
            final ComponentBuilder componentBuilder = new ComponentBuilder("");
            collect.forEach(componentBuilder::append);
            punishmentPlayer.disconnect(componentBuilder.create());
        };
    }

    public Consumer<Punishment> getFunctionMuteIfOn() {
        return punishment -> {
            final ProxiedPlayer punishmentPlayer = punishment.getPlayer();
            final List<String> collect = new PlaceHolder(Lists.newArrayList(new LongMessage("Runners.mute-alert").getStringList()), punishment).applyPlaceHolders();
            final ComponentBuilder componentBuilder = new ComponentBuilder("");
            collect.forEach(componentBuilder::append);
            punishmentPlayer.sendMessage(componentBuilder.create());
            if (!MutedPlayers.getInstance().exists(punishment.getPrimaryIdentifier()))
                new MutedPlayers.MuteObject(punishment.getPrimaryIdentifier(), punishment.getId(), punishment.getData().getStartedAt(), punishment.getData().getFinishAt(), punishment.getData().isPermanent(), punishment.getIp()).save();
        };
    }

    public Consumer<Punishment> getFunctionBanIfAddress() {
        return punishment1 -> {
            final AddressIP.AddressIPObject byAddress = AddressIP.getInstance().getByAddress(punishment1.getIp());
            byAddress.getAccounts().forEach(s -> {
                if (Main.getInstance().getProxy().getPlayer(s) != null) {
                    final ProxiedPlayer punishmentPlayer = Main.getInstance().getProxy().getPlayer(s);
                    final LongMessage longMessage = new LongMessage("Runners.ban-kick");
                    final List<String> collect = new PlaceHolder(Lists.newArrayList(longMessage.getStringList()), punishment1).applyPlaceHolders();
                    longMessage.setStringList(collect);
                    final ComponentBuilder componentBuilder = new ComponentBuilder("");
                    collect.forEach(componentBuilder::append);
                    punishmentPlayer.disconnect(componentBuilder.create());
                }
            });
        };
    }

    public Consumer<Punishment> getFunctionMuteIfAddress() {
        return punishment -> {
            final ProxyServer proxy = Main.getInstance().getProxy();
            final AddressIP.AddressIPObject byAddress = AddressIP.getInstance().getByAddress(punishment.getIp());
            final List<ProxiedPlayer> onlines = byAddress.getAccounts().stream().filter(s -> proxy.getPlayer(s) != null).map(proxy::getPlayer).collect(Collectors.toList());
            if (onlines.size() > 0) {
                if (!MutedPlayers.getInstance().exists(punishment.getPrimaryIdentifier()))
                    new MutedPlayers.MuteObject(punishment.getPrimaryIdentifier(), punishment.getId(), punishment.getData().getStartedAt(), punishment.getData().getFinishAt(), punishment.getData().isPermanent(), punishment.getIp()).save();
                onlines.forEach(p -> {
                    if (!p.getName().equalsIgnoreCase(punishment.getPrimaryIdentifier())) {
                        final List<String> collect = new PlaceHolder(Lists.newArrayList(new LongMessage("Runners.mute-alert").getStringList()), punishment).applyPlaceHolders();
                        final ComponentBuilder componentBuilder = new ComponentBuilder("");
                        collect.forEach(componentBuilder::append);
                        p.sendMessage(componentBuilder.create());
                    }
                });
            }
        };
    }

    public Consumer<Punishment> getFunctionAnnouncerBan() {
        return punishment1 -> {
            final LongMessage longMessage = new LongMessage("Announcements.ban");
            final List<String> collect = new PlaceHolder(longMessage.getStringList(), punishment1).applyPlaceHolders();
            longMessage.setStringList(collect);
            longMessage.getColorfulMessage().forEach(baseComponents -> Main.getInstance().getProxy().broadcast(baseComponents));
            final ModerationBot bot = ModerationBot.get();
            if (bot.isWorking()) {
                bot.announcement(punishment1);
            }
        };
    }

    public Consumer<Punishment> getFunctionAnnouncerMute() {
        return punishment1 -> {
            final LongMessage longMessage = new LongMessage("Announcements.mute");
            final List<String> collect = new PlaceHolder(longMessage.getStringList(), punishment1).applyPlaceHolders();
            longMessage.setStringList(collect);
            longMessage.getColorfulMessage().forEach(baseComponents -> Main.getInstance().getProxy().broadcast(baseComponents));
            final ModerationBot bot = ModerationBot.get();
            if (bot.isWorking()) {
                bot.announcement(punishment1);
            }
        };
    }
}
