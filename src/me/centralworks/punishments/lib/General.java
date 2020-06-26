package me.centralworks.punishments.lib;

import com.google.common.collect.Lists;
import me.centralworks.punishments.Main;
import me.centralworks.punishments.punishs.OfflinePunishment;
import me.centralworks.punishments.punishs.OnlinePunishment;
import me.centralworks.punishments.punishs.Punishment;
import me.centralworks.punishments.punishs.supliers.cached.MutedPlayers;
import me.centralworks.punishments.punishs.supliers.enums.PunishmentState;
import me.centralworks.punishments.punishs.supliers.enums.PunishmentType;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class with simple auxiliary functions, be careful when using.
 */
public class General {

    protected static General instance;

    public Consumer<Punishment> getFunctionBanIfOn(){
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

    public Consumer<Punishment> getFunctionMuteIfOn(){
        return punishment1 -> {
            final ProxiedPlayer punishmentPlayer = punishment1.getPlayer();
            final List<String> collect = applyPlaceHolders(Lists.newArrayList(new LongMessage("Runners.mute-alert").getStringList()), punishment1);
            final ComponentBuilder componentBuilder = new ComponentBuilder("");
            collect.forEach(componentBuilder::append);
            punishmentPlayer.sendMessage(componentBuilder.create());
            final Punishment newInstance = punishment1.requireById();
            new MutedPlayers.MuteObject(punishment1.getIdentifier(), newInstance.getId(), punishment1.getData().getStartedAt(), punishment1.getData().getFinishAt()).save();
        };
    }

    public Consumer<Punishment> getFunctionAnnouncerBan(){
        return punishment1 -> {
            final LongMessage longMessage = new LongMessage("Announcements.ban");
            final List<String> collect = applyPlaceHolders(longMessage.getStringList(), punishment1);
            longMessage.setStringList(collect);
            longMessage.getColorfulMessage().forEach(baseComponents -> Main.getInstance().getProxy().broadcast(baseComponents));
        };
    }

    public Consumer<Punishment> getFunctionAnnouncerMute(){
        return punishment1 -> {
            final LongMessage longMessage = new LongMessage("Announcements.mute");
            final List<String> collect = applyPlaceHolders(longMessage.getStringList(), punishment1);
            longMessage.setStringList(collect);
            longMessage.getColorfulMessage().forEach(baseComponents -> Main.getInstance().getProxy().broadcast(baseComponents));
        };
    }

    public static General getGeneralLib() {
        if (instance == null) instance = new General();
        return instance;
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
     * Method for online servers. Not recommended to offline servers.
     *
     * @param name Nickname player.
     * @return UUID of player offline/online.
     */
    public UUID getPlayerUUID(String name) {
        final ProxyServer proxy = Main.getInstance().getProxy();
        if (proxy.getPlayer(name) != null) proxy.getPlayer(name).getUniqueId();
        return MojangAPI.getInstance().getPlayerUUID(name);
    }

    /**
     * Method for online servers. Not recommended to offline servers.
     *
     * @param uuid UUID player.
     * @return nickname of player offline/online.
     */
    public String getPlayerName(UUID uuid) {
        final ProxyServer proxy = Main.getInstance().getProxy();
        if (proxy.getPlayer(uuid) != null) proxy.getPlayer(uuid).getName();
        return MojangAPI.getInstance().getName(uuid.toString());
    }

    /**
     * @param list       Message to apply placeholders
     * @param punishment Context punishments
     * @return new list with placeholders applied;
     */
    public List<String> applyPlaceHolders(List<String> list, Punishment punishment) {
        return list.stream().map(s -> s
                .replace("&", "§")
                .replace("{finishAt}", punishment.getData().getFinishAt() == 0L ? "§cPermanente." : new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(punishment.getData().getFinishDate())
                        .replace("-", " às "))
                .replace("{author}", punishment.getData().getPunisher())
                .replace("{id}", punishment.getId().toString())
                .replace("{evidence}", punishment.getData().getEvidencesFinally())
                .replace("{nickname}", punishment.getName())
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

    public boolean hasPunishmentMute(List<Punishment> punishments){
        return punishments.stream().anyMatch(punishment -> punishment.getData().getPunishmentType() == PunishmentType.MUTE || punishment.getData().getPunishmentType() == PunishmentType.TEMPMUTE);
    }

    public boolean hasPunishmentType(List<Punishment> punishments, PunishmentType punishmentType) {
        return punishments.stream().anyMatch(punishment -> punishment.getData().getPunishmentType() == punishmentType);
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
            if (punishment.getData().getPunishmentState() == PunishmentState.ACTIVE && punishment.getData().getFinishAt() != 0L && punishment.getData().getFinishAt() < System.currentTimeMillis()) {
                punishment.getData().setPunishmentState(PunishmentState.FINISHED);
                punishment.save();
            }
        }).collect(Collectors.toList());
    }

    public boolean isAddressIP(String text) {
        return Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$").matcher(text).find();
    }


    /**
     *
     * This method creates a new instance for a punishment.
     * @param nick nickname player
     * @param uuid uuid player
     * @return instance onlinemode (uuid) or offlinemode (nick).
     */
    public Punishment easyInstance(String nick, String uuid){
        Punishment punishment;
        if (Main.isOnlineMode()) {
            final OnlinePunishment onlinePunishment = new OnlinePunishment();
            onlinePunishment.setIdentifier(uuid);
            punishment = onlinePunishment;
        } else {
            final OfflinePunishment offlinePunishment = new OfflinePunishment();
            offlinePunishment.setIdentifier(nick);
            punishment = offlinePunishment;
        }
        return punishment;
    }

    /**
     *
     * @param nick nick player
     * @param uuid uuid player
     * @return uuid if onlinemode or nick if offlinemode
     */
    public String identifierCompare(String nick, String uuid){
        return Main.isOnlineMode() ? uuid : nick;
    }


}
