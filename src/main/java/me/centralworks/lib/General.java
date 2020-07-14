package me.centralworks.lib;

import com.google.common.collect.Lists;
import me.centralworks.Main;
import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.punishments.dao.WarnDAO;
import me.centralworks.modules.punishments.enums.Permission;
import me.centralworks.modules.punishments.models.OfflinePunishment;
import me.centralworks.modules.punishments.models.OnlinePunishment;
import me.centralworks.modules.punishments.models.Punishment;
import me.centralworks.modules.punishments.models.Warn;
import me.centralworks.modules.punishments.models.supliers.Elements;
import me.centralworks.modules.punishments.models.supliers.Immune;
import me.centralworks.modules.punishments.models.supliers.PlaceHolder;
import me.centralworks.modules.punishments.models.supliers.Request;
import me.centralworks.modules.punishments.models.supliers.cached.AddressIP;
import me.centralworks.modules.punishments.models.supliers.cached.MutedPlayers;
import me.centralworks.modules.punishments.models.supliers.cached.Reasons;
import me.centralworks.modules.reports.ReportPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class with simple auxiliary functions, be careful when using.
 * not recommended to use without developer assistance
 */
public class General {

    protected static General instance;

    public static General getGeneralLib() {
        if (instance == null) instance = new General();
        return instance;
    }

    public void sendPunishments(ProxiedPlayer p, String target) {
        final Configuration msg = PunishmentPlugin.getMessages();
        final ComponentBuilder title = new ComponentBuilder(msg.getStringList("Messages.punish.title").stream().map(s -> s.replace("&", "§")).collect(Collectors.joining()));
        p.sendMessage(title.create());
        final ComponentBuilder reasons = new ComponentBuilder("");
        Reasons.getInstance().getReasons().forEach(reason -> {
            final List<String> list = Lists.newArrayList(
                    msg.getString("Messages.punish.line.hoverMessage")
                            .replace("{type}", reason.getPunishmentType().getIdentifier())
                            .replace("{toggle}", reason.getWithIP() ? "§aSim" : "§cNão")
                            .replace("{duration}", reason.isPermanent() ? "Permanente" : new FormatTime(reason.getDuration()).format())
                            .replace("{permission}", reason.getPermission()).replace("&", "§")
            );
            if (!p.hasPermission(reason.getPermission()) && !Permission.hasPermission(p, Permission.ADMIN))
                list.add(msg.getString("Messages.punish.line.hoverWithoutPermission").replace("&", "§"));
            new Message(msg.getString("Messages.punish.line.message").replace("&", "§").replace("{reason}", reason.getReason())).sendJson("/punir " + target + " " + reason.getReason(), String.join("", list), p);
        });
        p.sendMessage(reasons.create());
        final ComponentBuilder footer = new ComponentBuilder(msg.getStringList("Messages.punish.footer").stream().map(s -> s.replace("&", "§")).collect(Collectors.joining()));
        p.sendMessage(footer.create());
    }

    public void sendReportList(ProxiedPlayer p, String target) {
        final Configuration msg = ReportPlugin.getMessages();
        final ComponentBuilder title = new ComponentBuilder(msg.getStringList("Messages.report.title").stream().map(s -> s.replace("&", "§")).collect(Collectors.joining()));
        p.sendMessage(title.create());
        ReportPlugin.getReasons().forEach(s -> {
            final Message message = new Message(msg.getString("Messages.report.line.message").replace("{reason}", s));
            message.sendJson("/reportar " + target + " " + s, msg.getString("Messages.report.line.hoverMessage").replace("&", "§").replace("{user}", target).replace("{reason}", s), p);
        });
        final ComponentBuilder footer = new ComponentBuilder(msg.getStringList("Messages.report.footer").stream().map(s -> s.replace("&", "§")).collect(Collectors.joining()));
        p.sendMessage(footer.create());
    }

    public void sendPunishmentStatus(CommandSender s, Punishment p) {
        final Configuration cfg = PunishmentPlugin.getMessages();
        final ComponentBuilder msg = new ComponentBuilder("");
        final Elements data = p.getData();
        for (String s1 : cfg.getStringList("Runners.punishment-view")) {
            msg.append(s1
                    .replace("&", "§")
                    .replace("{finishAt}", data.isPermanent() ? "§cPermanente." : new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(data.getFinishDate())
                            .replace("-", " às "))
                    .replace("{author}", data.getPunisher())
                    .replace("{id}", p.getId().toString())
                    .replace("{evidence}", p.getData().getEvidences().size() == 0 ? "§f§l• Nenhuma anexada" : formatEvidences(p))
                    .replace("{nickname}", p.getSecondaryIdentifier())
                    .replace("{startedAt}", new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(data.getStartDate())
                            .replace("-", " às "))
                    .replace("{reason}", data.getReason().getReason())
                    .replace("{identifier}", p.getPrimaryIdentifier())
                    .replace("{state}", data.getPunishmentState().getIdentifier())
                    .replace("{andressIP}", p.getIp().equals("") ? "Não informado." : p.getIp()));
        }
        s.sendMessage(msg.create());
    }

    public void kickPlayer(ProxiedPlayer p, String author, String reason) {
        final Configuration cfg = PunishmentPlugin.getMessages();
        final ComponentBuilder msg = new ComponentBuilder("");
        if (!Immune.canGo(author, p.getName())) return;
        for (String s1 : cfg.getStringList("Runners.kick")) {
            msg.append(s1
                    .replace("&", "§")
                    .replace("{author}", author)
                    .replace("{reason}", reason.equals("") ? "Não informado" : reason));
        }
        p.disconnect(msg.create());
        cfg.getStringList("Announcements.kick").forEach(s -> Main.getInstance().getProxy().broadcast(new TextComponent(s.replace("{author}", author).replace("&", "§").replace("{reason}", reason.equals("") ? "Não informado" : reason))));
    }

    public void sendHistory(CommandSender s, List<Punishment> punishments) {
        final Configuration cfg = PunishmentPlugin.getMessages();
        final StringBuilder builder = new StringBuilder();
        punishments.forEach(punishment -> builder.append(builder.toString().equals("") ? "§e#" + punishment.getId() : "§7, " + "§e#" + punishment.getId()));
        new Message(cfg.getString("Messages.history").replace("{nickname}", punishments.get(0).getSecondaryIdentifier()).replace("{ids}", builder.toString())).send(s);
    }

    public void sendHistoryWarn(CommandSender s, List<Warn> warns) {
        final Configuration cfg = PunishmentPlugin.getMessages();
        final StringBuilder builder = new StringBuilder();
        warns.forEach(w -> builder.append(builder.toString().equals("") ? "§e#" + w.getId() : "§7, " + "§e#" + w.getId()));
        new Message(cfg.getString("Messages.warns").replace("{nickname}", warns.get(0).getTarget()).replace("{ids}", builder.toString())).send(s);
    }

    public String formatEvidences(Punishment p) {
        final List<String> evidences = p.getData().getEvidences();
        final List<List<String>> pares = Lists.newArrayList();
        List<String> par = Lists.newArrayList();
        for (int i = 0; i < evidences.size(); i++) {
            final String evidence = evidences.get(i);
            if (par.size() < 2) {
                par.add(evidence);
                if (evidences.size() - i == 1) pares.add(par);
            } else {
                pares.add(par);
                par = Lists.newArrayList();
                par.add(evidence);
            }
        }
        return pares.stream().map(strings -> {
            final StringBuilder string = new StringBuilder("§f§l• §2" + strings.get(0));
            if (strings.size() == 2) string.append("§7, §2").append(strings.get(1));
            string.append("§7.");
            return string.toString();
        }).collect(Collectors.joining("\n"));
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
        return punishment1 -> {
            final Punishment newInstance = new Request(punishment1).requireByInstance();
            final ProxiedPlayer punishmentPlayer = newInstance.getPlayer();
            final List<String> collect = new PlaceHolder(Lists.newArrayList(new LongMessage("Runners.mute-alert").getStringList()), newInstance).applyPlaceHolders();
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
        return punishment1 -> {
            final ProxyServer proxy = Main.getInstance().getProxy();
            final AddressIP.AddressIPObject byAddress = AddressIP.getInstance().getByAddress(punishment1.getIp());
            final Punishment newInstance = new Request(punishment1).requireByInstance();
            final List<ProxiedPlayer> onlines = byAddress.getAccounts().stream().filter(s -> proxy.getPlayer(s) != null).map(proxy::getPlayer).collect(Collectors.toList());
            if (onlines.size() > 0) {
                if (!MutedPlayers.getInstance().exists(punishment1.getPrimaryIdentifier()))
                    new MutedPlayers.MuteObject(punishment1.getPrimaryIdentifier(), newInstance.getId(), newInstance.getData().getStartedAt(), newInstance.getData().getFinishAt(), newInstance.getData().isPermanent(), newInstance.getIp()).save();
                onlines.forEach(p -> {
                    if (!p.getName().equalsIgnoreCase(punishment1.getPrimaryIdentifier())) {
                        final List<String> collect = new PlaceHolder(Lists.newArrayList(new LongMessage("Runners.mute-alert").getStringList()), newInstance).applyPlaceHolders();
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
        };
    }

    public Consumer<Warn> getFunctionAnnouncerWarn() {
        return warn -> {
            final LongMessage longMessage = new LongMessage("Announcements.warn");
            longMessage.setStringList(applyPlaceHoldersWarn(longMessage.getStringList(), warn));
            longMessage.getColorfulMessage().forEach(baseComponents -> Main.getInstance().getProxy().broadcast(baseComponents));
        };
    }

    public Consumer<Punishment> getFunctionAnnouncerMute() {
        return punishment1 -> {
            final LongMessage longMessage = new LongMessage("Announcements.mute");
            final List<String> collect = new PlaceHolder(longMessage.getStringList(), punishment1).applyPlaceHolders();
            longMessage.setStringList(collect);
            longMessage.getColorfulMessage().forEach(baseComponents -> Main.getInstance().getProxy().broadcast(baseComponents));
        };
    }

    public Consumer<Warn> getFunctionWarnIfOn() {
        return warn -> {
            final ProxiedPlayer punishmentPlayer = Main.getInstance().getProxy().getPlayer(warn.getTarget());
            final List<String> collect = applyPlaceHoldersWarn(Lists.newArrayList(new LongMessage("Runners.warn-alert").getStringList()), warn);
            final ComponentBuilder componentBuilder = new ComponentBuilder("");
            collect.forEach(componentBuilder::append);
            punishmentPlayer.sendMessage(componentBuilder.create());
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

    public List<String> applyPlaceHoldersWarn(List<String> list, Warn warn) {
        return list.stream().map(s -> s
                .replace("&", "§")
                .replace("{nickname}", warn.getTarget())
                .replace("{author}", warn.getPunisher())
                .replace("{startedAt}", new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(warn.getStartedAt())
                        .replace("-", " às "))
                .replace("{finishAt}", warn.isPermanent() ? "§cPermanente." : new SimpleDateFormat("dd/MM/yyyy-HH:mm").format(warn.getFinishAt())
                        .replace("-", " às "))
                .replace("{reason}", warn.getReason())
                .replace("{id}", warn.getId().toString())
        ).collect(Collectors.toList());
    }

    public List<Warn> updateAllWarns(List<Warn> warns) {
        final List<Warn> ws = Lists.newCopyOnWriteArrayList(warns);
        for (Warn warn : ws) {
            if (!warn.isPermanent() && warn.getFinishAt() < System.currentTimeMillis()) {
                WarnDAO.getInstance().deleteById(warn.getId());
                ws.remove(warn);
            }
        }
        return ws;
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
