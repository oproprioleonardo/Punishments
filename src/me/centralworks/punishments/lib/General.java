package me.centralworks.punishments.lib;

import com.google.common.collect.Lists;
import me.centralworks.punishments.Main;
import me.centralworks.punishments.punishs.OfflinePunishment;
import me.centralworks.punishments.punishs.OnlinePunishment;
import me.centralworks.punishments.punishs.Punishment;
import me.centralworks.punishments.punishs.supliers.cached.AddressIP;
import me.centralworks.punishments.punishs.supliers.cached.MutedPlayers;
import me.centralworks.punishments.punishs.supliers.enums.PunishmentState;
import me.centralworks.punishments.punishs.supliers.enums.PunishmentType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class with simple auxiliary functions, be careful when using.
 */
public class General {

    protected static General instance;

    public static General getGeneralLib() {
        if (instance == null) instance = new General();
        return instance;
    }

    public Consumer<Punishment> getFunctionBanIfOn() {
        return punishment1 -> {
            final ProxiedPlayer punishmentPlayer = punishment1.getPlayer();
            final LongMessage longMessage = new LongMessage("Runners.ban-kick");
            final List<String> collect = applyPlaceHolders(Lists.newArrayList(longMessage.getStringList()), punishment1);
            longMessage.setStringList(collect);
            final ComponentBuilder componentBuilder = new ComponentBuilder("");
            collect.forEach(componentBuilder::append);
            punishmentPlayer.disconnect(componentBuilder.create());
        };
    }

    public Consumer<Punishment> getFunctionMuteIfOn() {
        return punishment1 -> {
            final Punishment newInstance = punishment1.requireByInstance();
            final ProxiedPlayer punishmentPlayer = newInstance.getPlayer();
            final List<String> collect = applyPlaceHolders(Lists.newArrayList(new LongMessage("Runners.mute-alert").getStringList()), newInstance);
            final ComponentBuilder componentBuilder = new ComponentBuilder("");
            collect.forEach(componentBuilder::append);
            punishmentPlayer.sendMessage(componentBuilder.create());
            if (!MutedPlayers.getInstance().exists(punishment1.getPrimaryIdentifier()))
                new MutedPlayers.MuteObject(punishment1.getPrimaryIdentifier(), newInstance.getId(), newInstance.getData().getStartedAt(), newInstance.getData().getFinishAt(), newInstance.getData().isPermanent(), newInstance.getIp()).save();
        };
    }

    public Consumer<Punishment> getFunctionBanIfAddress() {
        return punishment1 -> {
            final AddressIP.AddressIPObject byAddress = AddressIP.getInstance().getByAddress(punishment1.getIp());
            byAddress.getAccounts().forEach(s -> {
                if (Main.getInstance().getProxy().getPlayer(s) != null) {
                    final ProxiedPlayer punishmentPlayer = Main.getInstance().getProxy().getPlayer(s);
                    final LongMessage longMessage = new LongMessage("Runners.ban-kick");
                    final List<String> collect = applyPlaceHolders(Lists.newArrayList(longMessage.getStringList()), punishment1);
                    longMessage.setStringList(collect);
                    final ComponentBuilder componentBuilder = new ComponentBuilder("");
                    collect.forEach(componentBuilder::append);
                    punishmentPlayer.disconnect(componentBuilder.create());
                }
            });
        };
    }

    public Consumer<Punishment> getFunctionMuteIfAddress() {
        return punishment1 -> {
            final AddressIP.AddressIPObject byAddress = AddressIP.getInstance().getByAddress(punishment1.getIp());
            final Punishment newInstance = punishment1.requireByInstance();
            byAddress.getAccounts().forEach(s -> {
                if (!s.equalsIgnoreCase(punishment1.getPrimaryIdentifier())) {
                    final ProxiedPlayer punishmentPlayer = Main.getInstance().getProxy().getPlayer(s);
                    final List<String> collect = applyPlaceHolders(Lists.newArrayList(new LongMessage("Runners.mute-alert").getStringList()), newInstance);
                    final ComponentBuilder componentBuilder = new ComponentBuilder("");
                    collect.forEach(componentBuilder::append);
                    punishmentPlayer.sendMessage(componentBuilder.create());
                }
            });
        };
    }

    public Consumer<Punishment> getFunctionAnnouncerBan() {
        return punishment1 -> {
            final LongMessage longMessage = new LongMessage("Announcements.ban");
            final List<String> collect = applyPlaceHolders(longMessage.getStringList(), punishment1);
            longMessage.setStringList(collect);
            longMessage.getColorfulMessage().forEach(baseComponents -> Main.getInstance().getProxy().broadcast(baseComponents));
        };
    }

    public Consumer<Punishment> getFunctionAnnouncerMute() {
        return punishment1 -> {
            final LongMessage longMessage = new LongMessage("Announcements.mute");
            final List<String> collect = applyPlaceHolders(longMessage.getStringList(), punishment1);
            longMessage.setStringList(collect);
            longMessage.getColorfulMessage().forEach(baseComponents -> Main.getInstance().getProxy().broadcast(baseComponents));
        };
    }

    /**
     * This method has simple checks.
     *
     * @param text any text.
     * @return if text is link.
     */
    public boolean isLink(String text) {
        return Stream.of(".com", ".br", "www.", "http", "https").anyMatch(text::contains);
    }

    /**
     * @param list       Message to apply placeholders
     * @param punishment Context punishments
     * @return new list with placeholders applied;
     */
    public List<String> applyPlaceHolders(List<String> list, Punishment punishment) {
        return list.stream().map(s -> s
                .replace("&", "§")
                .replace("{finishAt}", punishment.getData().isPermanent() ? "§cPermanente." : new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(punishment.getData().getFinishDate())
                        .replace("-", " às "))
                .replace("{author}", punishment.getData().getPunisher())
                .replace("{id}", punishment.getId().toString())
                .replace("{evidence}", punishment.getData().getEvidencesFinally())
                .replace("{nickname}", punishment.getSecondaryIdentifier())
                .replace("{startedAt}", new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(punishment.getData().getStartDate())
                        .replace("-", " às "))
                .replace("{reason}", punishment.getData().getReason().getReason()))
                .collect(Collectors.toList());
    }

    public boolean hasActivePunishment(List<Punishment> punishments) {
        return punishments.stream().anyMatch(punishment -> punishment.getData().getPunishmentState() == PunishmentState.ACTIVE);
    }

    public boolean hasPunishmentBan(List<Punishment> punishments) {
        return punishments.stream().anyMatch(punishment -> punishment.getData().getPunishmentType() == PunishmentType.BAN || punishment.getData().getPunishmentType() == PunishmentType.TEMPBAN);
    }

    public boolean hasPunishmentMute(List<Punishment> punishments) {
        return punishments.stream().anyMatch(punishment -> punishment.getData().getPunishmentType() == PunishmentType.MUTE || punishment.getData().getPunishmentType() == PunishmentType.TEMPMUTE);
    }

    public boolean hasPunishmentType(List<Punishment> punishments, PunishmentType punishmentType) {
        return punishments.stream().anyMatch(punishment -> punishment.getData().getPunishmentType() == punishmentType);
    }

    public List<Punishment> getAllActive(List<Punishment> punishments) {
        return getByState(punishments, PunishmentState.ACTIVE);
    }

    public List<Punishment> getAllFinished(List<Punishment> punishments) {
        return getByState(punishments, PunishmentState.FINISHED);
    }

    public List<Punishment> getAllRevoked(List<Punishment> punishments) {
        return getByState(punishments, PunishmentState.REVOKED);
    }

    public List<Punishment> getAllMutedPActive(List<Punishment> punishments) {
        return getAllMuteP(getAllActive(punishments));
    }

    public List<Punishment> getAllBannedPActive(List<Punishment> punishments) {
        return getAllBannedP(getAllActive(punishments));
    }

    public List<Punishment> getByState(List<Punishment> punishments, PunishmentState punishmentState) {
        return punishments.stream().filter(punishment -> punishment.getData().getPunishmentState() == punishmentState).collect(Collectors.toList());
    }

    public List<Punishment> getByStates(List<Punishment> punishments, PunishmentState... punishmentStates) {
        return punishments.stream().filter(punishment -> Lists.newArrayList(punishmentStates).contains(punishment.getData().getPunishmentState())).collect(Collectors.toList());
    }

    public List<Punishment> getByStates(List<Punishment> punishments, List<PunishmentState> punishmentStates) {
        return punishments.stream().filter(punishment -> punishmentStates.contains(punishment.getData().getPunishmentState())).collect(Collectors.toList());
    }

    public List<Punishment> getAllBannedP(List<Punishment> punishments) {
        return getAllByTypes(punishments, PunishmentType.BAN, PunishmentType.TEMPBAN);
    }

    public List<Punishment> getAllMuteP(List<Punishment> punishments) {
        return getAllByTypes(punishments, PunishmentType.MUTE, PunishmentType.TEMPMUTE);
    }

    public List<Punishment> getAllByType(List<Punishment> punishments, PunishmentType punishmentType) {
        return punishments.stream().filter(punishment -> punishment.getData().getPunishmentType() == punishmentType).collect(Collectors.toList());
    }

    public List<Punishment> getAllByTypes(List<Punishment> punishments, PunishmentType... punishmentTypes) {
        final ArrayList<PunishmentType> types = Lists.newArrayList(punishmentTypes);
        return punishments.stream().filter(punishment -> types.contains(punishment.getData().getPunishmentType())).collect(Collectors.toList());
    }

    public List<Punishment> getAllByTypes(List<Punishment> punishments, List<PunishmentType> types) {
        return punishments.stream().filter(punishment -> types.contains(punishment.getData().getPunishmentType())).collect(Collectors.toList());
    }

    public List<Punishment> updateAll(List<Punishment> punishments) {
        return punishments.stream().peek(punishment -> {
            if (punishment.getData().getPunishmentState() == PunishmentState.ACTIVE && !punishment.getData().isPermanent() && punishment.getData().getFinishAt() < System.currentTimeMillis()) {
                punishment.getData().setPunishmentState(PunishmentState.FINISHED);
                punishment.save();
            }
        }).collect(Collectors.toList());
    }

    public boolean isAddressIP(String text) {
        return Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$").matcher(text).find();
    }


    /**
     * This method creates a new instance for a punishment.
     *
     * @param nick nickname player
     * @param uuid uuid player
     * @return instance onlinemode (uuid) or offlinemode (nick).
     */
    public Punishment easyInstance(String nick, String uuid) {
        Punishment punishment;
        if (Main.isOnlineMode()) {
            final OnlinePunishment onlinePunishment = new OnlinePunishment();
            onlinePunishment.setPrimaryIdentifier(uuid);
            punishment = onlinePunishment;
        } else {
            final OfflinePunishment offlinePunishment = new OfflinePunishment();
            offlinePunishment.setPrimaryIdentifier(nick);
            punishment = offlinePunishment;
        }
        return punishment;
    }

    public Punishment easyInstance() {
        Punishment punishment;
        if (Main.isOnlineMode()) punishment = new OnlinePunishment();
        else punishment = new OfflinePunishment();
        return punishment;
    }

    /**
     * @param nick nick player
     * @param uuid uuid player
     * @return uuid if onlinemode or nick if offlinemode
     */
    public String identifierCompare(String nick, String uuid) {
        return Main.isOnlineMode() ? uuid : nick;
    }


}
