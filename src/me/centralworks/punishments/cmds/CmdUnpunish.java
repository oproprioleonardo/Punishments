package me.centralworks.punishments.cmds;

import me.centralworks.punishments.Main;
import me.centralworks.punishments.enums.Permission;
import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.lib.Message;
import me.centralworks.punishments.punishs.Punishment;
import me.centralworks.punishments.punishs.supliers.enums.PunishmentState;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class CmdUnpunish extends Command {

    public CmdUnpunish() {
        super("unpunish", Permission.UNPUNISH.getPermission(), "despunir", "revogar");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        final Configuration cfg = Main.getMessages();
        final General gnrlLib = General.getGeneralLib();
        if (!Permission.hasPermission(s, Permission.UNPUNISH)) {
            new Message(Main.getMessages().getString("Messages.permission-error")).send(s);
            return;
        }
        new Message(Main.getMessages().getString("Messages.wait")).send(s);
        BungeeCord.getInstance().getScheduler().runAsync(Main.getInstance(), () -> {
            try {
                final Punishment punishment = gnrlLib.easyInstance();
                punishment.setId(Integer.parseInt(args[0]));
                if (punishment.existsById()) {
                    final Punishment p = punishment.requireById();
                    if (p.getData().getPunishmentState() == PunishmentState.FINISHED || p.getData().getPunishmentState() == PunishmentState.REVOKED) {
                        new Message(cfg.getString("Messages.already-revoked")).send(s);
                        return;
                    }
                    p.pardon();
                    new Message(cfg.getString("Messages.punish-pardon")).send(s);
                } else new Message(cfg.getString("Messages.punish-not-found")).send(s);
            } catch (Exception e) {
                new Message(cfg.getString("Usages.unpunish")).send(s);
            }
        });

    }
}
