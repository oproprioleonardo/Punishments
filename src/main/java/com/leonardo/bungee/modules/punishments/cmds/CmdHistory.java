package com.leonardo.bungee.modules.punishments.cmds;

import com.leonardo.bungee.modules.punishments.enums.Permission;
import com.leonardo.bungee.modules.punishments.models.Punishment;
import com.leonardo.bungee.modules.punishments.models.supliers.Request;
import com.leonardo.bungee.lib.Functionalities;
import com.leonardo.bungee.lib.Message;
import com.leonardo.bungee.modules.punishments.PunishmentPlugin;
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
        if (!Permission.hasPermission(s, Permission.PUNISHHISTORY)) return;
        new Message(cfg.getString("Messages.wait")).send(s);
        try {
            final Punishment punishment = Functionalities.easyInstance();
            punishment.setSecondaryIdentifier(args[0]);
            CompletableFuture.supplyAsync(() -> new Request(punishment).requireAllBySecondaryIdentifier()).thenAcceptAsync(punishments -> {
                if (punishments.size() > 0) {
                    Functionalities.sendHistory(s, punishments);
                } else new Message(cfg.getString("Messages.angel")).send(s);
            });
        } catch (Exception e) {
            new Message(PunishmentPlugin.getUsages().getString("Usages.punishhistory")).send(s);
        }
    }
}
