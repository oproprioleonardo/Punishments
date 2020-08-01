package me.centralworks.bungee.modules.punishments.cmds;

import me.centralworks.bungee.lib.General;
import me.centralworks.bungee.lib.Message;
import me.centralworks.bungee.modules.punishments.PunishmentPlugin;
import me.centralworks.bungee.modules.punishments.enums.Permission;
import me.centralworks.bungee.modules.punishments.models.Punishment;
import me.centralworks.bungee.modules.punishments.models.supliers.CheckUp;
import me.centralworks.bungee.modules.punishments.models.supliers.Filter;
import me.centralworks.bungee.modules.punishments.models.supliers.Request;
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
        final General gnrlLib = General.get();
        if (!Permission.hasPermission(s, Permission.UNMUTE)) return;
        new Message(cfg.getString("Messages.wait")).send(s);
        try {
            final Punishment punishment = gnrlLib.easyInstance();
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
