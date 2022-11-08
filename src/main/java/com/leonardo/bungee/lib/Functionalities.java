package com.leonardo.bungee.lib;

import com.google.common.collect.Lists;
import com.leonardo.bungee.Main;
import com.leonardo.bungee.modules.punishments.PunishmentPlugin;
import com.leonardo.bungee.modules.punishments.dao.WarnDAO;
import com.leonardo.bungee.modules.punishments.models.OfflinePunishment;
import com.leonardo.bungee.modules.punishments.models.OnlinePunishment;
import com.leonardo.bungee.modules.punishments.models.Punishment;
import com.leonardo.bungee.modules.punishments.models.Warn;
import com.leonardo.bungee.modules.punishments.models.supliers.Immune;
import com.leonardo.bungee.modules.reports.ReportPlugin;
import com.leonardo.bungee.modules.reports.enums.Permission;
import com.leonardo.bungee.modules.reports.models.Report;
import com.leonardo.bungee.modules.reports.models.ReportedPlayer;
import com.leonardo.bungee.modules.reports.models.supliers.ToggleAnnouncement;
import com.leonardo.bungee.modules.punishments.models.supliers.Elements;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
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
 * Class with children without a place to live,
 * not recommended to use without developer assistance.
 */
public class Functionalities {

    public static void sendPunishmentStatus(CommandSender s, Punishment p) {
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
                    .replace("{evidence}", p.getData().getEvidences().size() == 0 ? "§f§l• Nenhuma anexada" : formatEvidences(p.getData().getEvidences()))
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

    public static void kickPlayer(ProxiedPlayer p, String author, String reason) {
        final Configuration cfg = PunishmentPlugin.getMessages();
        final ComponentBuilder msg = new ComponentBuilder("");
        if (!Immune.canGo(author, p.getName())) return;
        for (String s1 : cfg.getStringList("Runners.kick")) {
            msg.append(s1
                    .replace("&", "§")
                    .replace("{nickname}", author)
                    .replace("{reason}", reason.equals("") ? "Não informado" : reason));
        }
        p.disconnect(msg.create());
        cfg.getStringList("Announcements.kick").forEach(s -> Main.getInstance().getProxy().broadcast(new TextComponent(s.replace("{author}", author).replace("&", "§").replace("{reason}", reason.equals("") ? "Não informado" : reason))));
    }

    public static void sendHistory(CommandSender s, List<Punishment> punishments) {
        final Configuration cfg = PunishmentPlugin.getMessages();
        final StringBuilder builder = new StringBuilder();
        punishments.forEach(punishment -> builder.append(builder.toString().equals("") ? "§e#" + punishment.getId() : "§7, " + "§e#" + punishment.getId()));
        new Message(cfg.getString("Messages.history").replace("{nickname}", punishments.get(0).getSecondaryIdentifier()).replace("{ids}", builder.toString())).send(s);
    }

    public static void sendHistoryWarn(CommandSender s, List<Warn> warns) {
        final Configuration cfg = PunishmentPlugin.getMessages();
        final StringBuilder builder = new StringBuilder();
        warns.forEach(w -> builder.append(builder.toString().equals("") ? "§e#" + w.getId() : "§7, " + "§e#" + w.getId()));
        new Message(cfg.getString("Messages.warns").replace("{nickname}", warns.get(0).getTarget()).replace("{ids}", builder.toString())).send(s);
    }

    public static void sendReport(ReportedPlayer rp, int id) {
        final Configuration cfg = ReportPlugin.getMessages();
        final Report r = rp.getById(id);
        final TextComponent[] text;
        final String originalText = cfg.getStringList("Runners.alert").stream().map(s -> s
                .replace("&", "§")
                .replace("{player}", rp.getUser())
                .replace("{reason}", r.getReason())
                .replace("{victim}", r.getVictim())
                .replace("{evidences}", formatEvidences(r.getEvidences()))
                .replace("{id}", "" + id)).collect(Collectors.joining());
        if (originalText.contains("{aqui}")) {
            final String[] split = originalText.split(Pattern.quote("{aqui}"));
            final TextComponent a = new TextComponent(split[0]);
            final TextComponent c = new TextComponent(split[1]);
            final TextComponent b = new TextComponent(cfg.getString("Messages.aqui").replace("&", "§"));
            b.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/reportes " + rp.getUser()));
            text = new TextComponent[]{a, b, c};
        } else text = new TextComponent[]{new TextComponent(originalText)};
        ToggleAnnouncement.getInstance().getMap().forEach((key, value) -> {
            final ProxiedPlayer p = Main.getInstance().getProxy().getPlayer(key);
            if (value && (p.hasPermission(Permission.REPORTS.getPermission()) || p.hasPermission(Permission.ADMIN.getPermission())))
                p.sendMessage(text);
        });
    }

    public static String formatEvidences(List<String> evidences) {
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
            final StringBuilder string = new StringBuilder("§f§l• §a" + strings.get(0));
            if (strings.size() == 2) string.append("§7, §a").append(strings.get(1));
            string.append("§7.");
            return string.toString();
        }).collect(Collectors.joining("\n"));
    }

    public static Consumer<Warn> getFunctionAnnouncerWarn() {
        return warn -> {
            final LongMessage longMessage = new LongMessage("Announcements.warn");
            longMessage.setStringList(applyPlaceHoldersWarn(longMessage.getStringList(), warn));
            longMessage.getColorfulMessage().forEach(baseComponents -> Main.getInstance().getProxy().broadcast(baseComponents));
        };
    }

    public static Consumer<Warn> getFunctionWarnIfOn() {
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
    public static boolean isNotLink(String text) {
        return Stream.of(".com", ".br", "www.", "http", "https").anyMatch(text::contains);
    }

    public static List<String> applyPlaceHoldersWarn(List<String> list, Warn warn) {
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

    public static List<Warn> updateAllWarns(List<Warn> warns) {
        final List<Warn> ws = Lists.newCopyOnWriteArrayList(warns);
        for (Warn warn : ws) {
            if (!warn.isPermanent() && warn.getFinishAt() < System.currentTimeMillis()) {
                WarnDAO.getInstance().deleteById(warn.getId());
                ws.remove(warn);
            }
        }
        return ws;
    }

    public static boolean isAddressIP(String text) {
        return Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$").matcher(text).find();
    }

    public static Punishment easyInstance(String nick, String uuid) {
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

    public static Punishment easyInstance(String id) {
        Punishment punishment;
        if (Main.isOnlineMode()) {
            final OnlinePunishment onlinePunishment = new OnlinePunishment();
            onlinePunishment.setPrimaryIdentifier(id);
            punishment = onlinePunishment;
        } else {
            final OfflinePunishment offlinePunishment = new OfflinePunishment();
            offlinePunishment.setPrimaryIdentifier(id);
            punishment = offlinePunishment;
        }
        return punishment;
    }

    public static Punishment easyInstance() {
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
    public static String identifierCompare(String nick, String uuid) {
        return Main.isOnlineMode() ? uuid : nick;
    }


}
