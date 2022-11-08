package com.leonardo.bungee.modules.punishments.cmds;

import com.leonardo.bungee.modules.punishments.enums.Permission;
import com.leonardo.bungee.modules.punishments.models.Punishment;
import com.leonardo.bungee.modules.punishments.models.supliers.Request;
import com.leonardo.bungee.lib.Functionalities;
import com.leonardo.bungee.lib.Message;
import com.leonardo.bungee.modules.punishments.PunishmentPlugin;
import com.leonardo.bungee.modules.punishments.models.supliers.CheckUp;
import com.leonardo.bungee.modules.punishments.models.supliers.Filter;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CmdUnmute extends Command {

    public CmdUnmute() {
        super("unmute", "", "desmutar");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        final Configuration cfg = PunishmentPlugin.getMessages();
        if (!Permission.hasPermission(s, Permission.UNMUTE)) return;
        new Message(cfg.getString("Messages.wait")).send(s);
        try {
            final Punishment punishment = Functionalities.easyInstance();
            punishment.setSecondaryIdentifier(args[0]);
            CompletableFuture.supplyAsync(() -> new Request(punishment).requireAllBySecondaryIdentifier()).thenAcceptAsync(punishments -> {
                final CheckUp checkUp = new CheckUp(punishments);
                if (checkUp.hasActivePunishment() && checkUp.hasPunishmentMute()) {
                    final List<Punishment> ps = new Filter(punishments).getAllMutedPActive();
                    ps.forEach(Punishment::pardon);
                    new Message(cfg.getString("Messages.mute-pardon")).send(s);
                } else new Message(cfg.getString("Messages.mute-not-found")).send(s);
            });
        } catch (Exception e) {
            new Message(cfg.getString("Usages.unmute")).send(s);
        }
    }
}
