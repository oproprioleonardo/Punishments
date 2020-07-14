package me.centralworks.modules.punishments.cmds;

import me.centralworks.Main;
import me.centralworks.lib.General;
import me.centralworks.lib.Message;
import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.punishments.enums.Permission;
import me.centralworks.modules.punishments.models.Punishment;
import me.centralworks.modules.punishments.models.supliers.Request;
import me.centralworks.modules.punishments.models.supliers.enums.PunishmentState;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class CmdUnpunish extends Command {

    public CmdUnpunish() {
        super("unpunish", "", "despunir", "revogar");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        final Configuration cfg = PunishmentPlugin.getMessages();
        final General gnrlLib = General.getGeneralLib();
        if (!Permission.hasPermission(s, Permission.UNPUNISH)) return;
        new Message(PunishmentPlugin.getMessages().getString("Messages.wait")).send(s);
        Main.getInstance().getProxy().getScheduler().runAsync(Main.getInstance(), () -> {
            try {
                final Punishment punishment = gnrlLib.easyInstance();
                punishment.setId(Integer.parseInt(args[0]));
                final Request request = new Request(punishment);
                if (request.existsById()) {
                    final Punishment p = request.requireById();
                    if (p.getData().getPunishmentState() == PunishmentState.FINISHED || p.getData().getPunishmentState() == PunishmentState.REVOKED) {
                        new Message(cfg.getString("Messages.already-revoked")).send(s);
                        return;
                    }
                    p.pardon();
                    new Message(cfg.getString("Messages.punish-pardon")).send(s);
                } else new Message(cfg.getString("Messages.punish-not-found")).send(s);
            } catch (Exception e) {
                new Message(PunishmentPlugin.getUsages().getString("Usages.unpunish")).send(s);
            }
        });

    }
}
