package com.leonardo.bungee.modules.punishments.cmds;

import com.leonardo.bungee.modules.punishments.enums.Permission;
import com.leonardo.bungee.modules.punishments.models.Punishment;
import com.leonardo.bungee.modules.punishments.models.supliers.Request;
import com.leonardo.bungee.lib.Functionalities;
import com.leonardo.bungee.lib.Message;
import com.leonardo.bungee.modules.punishments.PunishmentPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.util.concurrent.CompletableFuture;

public class CmdPunishView extends Command {

    public CmdPunishView() {
        super("punishview", "", "pview");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        final Configuration msg = PunishmentPlugin.getMessages();
        if (!(s instanceof ProxiedPlayer)) {
            new Message(PunishmentPlugin.getMessages().getString("Messages.only-player")).send(s);
            return;
        }
        if (!Permission.hasPermission(s, Permission.PUNISHVIEW)) return;
        new Message(msg.getString("Messages.wait")).send(s);
        try {
            final Punishment punishment = Functionalities.easyInstance();
            punishment.setId(Integer.parseInt(args[0]));
            CompletableFuture.supplyAsync(() -> new Request(punishment).requireById()).exceptionally(error -> {
                new Message(msg.getString("Messages.punish-not-found")).send(s);
                return null;
            }).thenAcceptAsync(p -> Functionalities.sendPunishmentStatus(s, p));
        } catch (Exception e) {
            new Message(PunishmentPlugin.getUsages().getString("Usages.punishview")).send(s);
        }
    }
}
