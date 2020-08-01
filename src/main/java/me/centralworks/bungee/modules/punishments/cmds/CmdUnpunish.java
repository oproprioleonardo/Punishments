package me.centralworks.bungee.modules.punishments.cmds;

import me.centralworks.bungee.lib.General;
import me.centralworks.bungee.lib.Message;
import me.centralworks.bungee.modules.punishments.PunishmentPlugin;
import me.centralworks.bungee.modules.punishments.enums.Permission;
import me.centralworks.bungee.modules.punishments.models.Punishment;
import me.centralworks.bungee.modules.punishments.models.supliers.Request;
import me.centralworks.bungee.modules.punishments.models.supliers.enums.PunishmentState;
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
        final General gnrlLib = General.get();
        if (!Permission.hasPermission(s, Permission.UNPUNISH)) return;
        new Message(PunishmentPlugin.getMessages().getString("Messages.wait")).send(s);
        try {
            final Punishment punishment = gnrlLib.easyInstance();
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
