package me.centralworks.punishments.cmds;

import me.centralworks.punishments.Main;
import me.centralworks.punishments.enums.Permission;
import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.lib.Message;
import me.centralworks.punishments.punishs.Punishment;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.util.List;

public class CmdHistory extends Command {

    public CmdHistory() {
        super("punishhistory", Permission.PUNISHHISTORY.getPermission(), "phistory");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        final Configuration cfg = Main.getMessages();
        final General gnrlLib = General.getGeneralLib();
        if (!Permission.hasPermission(s, Permission.PUNISHHISTORY)) {
            new Message(Main.getMessages().getString("Messages.permission-error")).send(s);
            return;
        }
        new Message(Main.getMessages().getString("Messages.wait")).send(s);
        BungeeCord.getInstance().getScheduler().runAsync(Main.getInstance(), () -> {
            try {
                final Punishment punishment = gnrlLib.easyInstance();
                punishment.setSecondaryIdentifier(args[0]);
                final List<Punishment> punishments = punishment.requireAllBySecondaryIdentifier();
                if (punishments.size() > 0) {
                    gnrlLib.sendHistory(s, punishments);
                } else new Message(cfg.getString("Messages.angel")).send(s);
            } catch (Exception e) {
                new Message(Main.getUsages().getString("Usages.punishhistory")).send(s);
            }
        });
    }
}
