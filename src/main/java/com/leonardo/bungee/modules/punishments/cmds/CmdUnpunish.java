package com.leonardo.bungee.modules.punishments.cmds;

import com.leonardo.bungee.modules.punishments.enums.Permission;
import com.leonardo.bungee.modules.punishments.models.Punishment;
import com.leonardo.bungee.modules.punishments.models.supliers.Request;
import com.leonardo.bungee.lib.Functionalities;
import com.leonardo.bungee.lib.Message;
import com.leonardo.bungee.modules.punishments.PunishmentPlugin;
import com.leonardo.bungee.modules.punishments.models.supliers.enums.PunishmentState;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.util.concurrent.CompletableFuture;

public class CmdUnpunish extends Command {

    public CmdUnpunish() {
        super("unpunish", "", "despunir", "revogar");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        final Configuration cfg = PunishmentPlugin.getMessages();
        if (!Permission.hasPermission(s, Permission.UNPUNISH)) return;
        new Message(PunishmentPlugin.getMessages().getString("Messages.wait")).send(s);
        try {
            final Punishment punishment = Functionalities.easyInstance();
            punishment.setId(Integer.parseInt(args[0]));
            CompletableFuture.supplyAsync(() -> new Request(punishment).requireById()).exceptionally(throwable -> {
                new Message(cfg.getString("Messages.punish-not-found")).send(s);
                return null;
            }).thenAcceptAsync(p -> {
                if (p.getData().getPunishmentState() == PunishmentState.FINISHED || p.getData().getPunishmentState() == PunishmentState.REVOKED)
                    new Message(cfg.getString("Messages.already-revoked")).send(s);
                else {
                    p.pardon();
                    new Message(cfg.getString("Messages.punish-pardon")).send(s);
                }
            });
        } catch (Exception e) {
            new Message(PunishmentPlugin.getUsages().getString("Usages.unpunish")).send(s);
        }
    }
}
