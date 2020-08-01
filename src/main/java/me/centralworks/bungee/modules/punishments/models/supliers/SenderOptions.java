package me.centralworks.bungee.modules.punishments.models.supliers;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.centralworks.bungee.lib.FormatTime;
import me.centralworks.bungee.lib.Message;
import me.centralworks.bungee.modules.punishments.PunishmentPlugin;
import me.centralworks.bungee.modules.punishments.models.supliers.cached.Reasons;
import me.centralworks.bungee.modules.reports.ReportPlugin;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.config.Configuration;

import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class SenderOptions {

    private ProxiedPlayer p;
    private String target;

    public SenderOptions(ProxiedPlayer p, String target) {
        this.p = p;
        this.target = target;
    }

    public void sendPunishmentList() {
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
            new Message(msg.getString("Messages.punish.line.message").replace("&", "§").replace("{reason}", reason.getReason())).sendJson("/punir " + target + " " + reason.getReason(), String.join("", list), p);
        });
        p.sendMessage(reasons.create());
        final ComponentBuilder footer = new ComponentBuilder(msg.getStringList("Messages.punish.footer").stream().map(s -> s.replace("&", "§")).collect(Collectors.joining()));
        p.sendMessage(footer.create());
    }

    public void sendReportList() {
        final Configuration msg = ReportPlugin.getMessages();
        final ComponentBuilder title = new ComponentBuilder(msg.getStringList("Messages.report.title").stream().map(s -> s.replace("&", "§")).collect(Collectors.joining()));
        p.sendMessage(title.create());
        ReportPlugin.getReasons().forEach(s -> {
            final Message message = new Message(msg.getString("Messages.report.line.message").replace("&", "§").replace("{reason}", s));
            message.sendJson("/reportar " + target + " " + s, msg.getString("Messages.report.line.hoverMessage").replace("&", "§").replace("{user}", target).replace("{reason}", s), p);
        });
        final ComponentBuilder footer = new ComponentBuilder(msg.getStringList("Messages.report.footer").stream().map(s -> s.replace("&", "§")).collect(Collectors.joining()));
        p.sendMessage(footer.create());
    }
}
