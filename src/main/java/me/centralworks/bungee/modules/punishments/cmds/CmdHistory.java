package me.centralworks.bungee.modules.punishments.cmds;

import me.centralworks.bungee.lib.General;
import me.centralworks.bungee.lib.Message;
import me.centralworks.bungee.modules.punishments.PunishmentPlugin;
import me.centralworks.bungee.modules.punishments.enums.Permission;
import me.centralworks.bungee.modules.punishments.models.Punishment;
import me.centralworks.bungee.modules.punishments.models.supliers.Request;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.util.concurrent.CompletableFuture;

public class CmdHistory extends Command {

    public CmdHistory() {
        super("punishhistory", "", "phistory");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        final Configuration cfg = PunishmentPlugin.getMessages();
        final General gnrlLib = General.getGeneralLib();
        if (!Permission.hasPermission(s, Permission.PUNISHHISTORY)) return;
        new Message(cfg.getString("Messages.wait")).send(s);
        try {
            final Punishment punishment = gnrlLib.easyInstance();
            punishment.setSecondaryIdentifier(args[0]);
            CompletableFuture.supplyAsync(() -> new Request(punishment).requireAllBySecondaryIdentifier()).thenAcceptAsync(punishments -> {
                if (punishments.size() > 0) {
                    gnrlLib.sendHistory(s, punishments);
                } else new Message(cfg.getString("Messages.angel")).send(s);
            });
        } catch (Exception e) {
            new Message(PunishmentPlugin.getUsages().getString("Usages.punishhistory")).send(s);
        }
    }
}
