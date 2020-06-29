package me.centralworks.punishments.cmds;

import me.centralworks.punishments.Main;
import me.centralworks.punishments.enums.Permission;
import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.lib.Message;
import me.centralworks.punishments.punishs.Punishment;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.util.List;

public class CmdUnmute extends Command {

    public CmdUnmute() {
        super("unmute", Permission.UNMUTE.getPermission(), "desmutar");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        final Configuration cfg = Main.getMessages();
        final General gnrlLib = General.getGeneralLib();
        if (!Permission.hasPermission(s, Permission.UNMUTE)) {
            new Message(Main.getMessages().getString("Messages.permission-error")).send(s);
            return;
        }
        try {
            final Punishment punishment = gnrlLib.easyInstance();
            punishment.setBreakNick(args[0]);
            final List<Punishment> punishments = punishment.requireAllBySecondaryIdentifier();
            if (gnrlLib.hasActivePunishment(punishments) && gnrlLib.hasPunishmentMute(punishments)) {
                final List<Punishment> ps = gnrlLib.getAllMutedPActive(punishments);
                ps.forEach(Punishment::pardon);
                new Message(cfg.getString("Messages.mute-pardon")).send(s);
            } else new Message(cfg.getString("Messages.mute-not-found")).send(s);
        } catch (Exception e) {
            new Message(cfg.getString("Usages.unmute")).send(s);
        }
    }
}
