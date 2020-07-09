package me.centralworks.modules.punishments.cmds;

import me.centralworks.Main;
import me.centralworks.lib.General;
import me.centralworks.lib.Message;
import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.punishments.enums.Permission;
import me.centralworks.modules.punishments.models.punishs.Punishment;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CmdPunishView extends Command {

    public CmdPunishView() {
        super("punishview", "", "pview");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        try {
            if (!(s instanceof ProxiedPlayer)) {
                new Message(PunishmentPlugin.getMessages().getString("Messages.only-player")).send(s);
                return;
            }
            if (!Permission.hasPermission(s, Permission.PUNISHVIEW)) {
                new Message(PunishmentPlugin.getMessages().getString("Messages.permission-error")).send(s);
                return;
            }
            final General lib = General.getGeneralLib();
            new Message(PunishmentPlugin.getMessages().getString("Messages.wait")).send(s);
            Main.getInstance().getProxy().getScheduler().runAsync(Main.getInstance(), () -> {
                final int id = Integer.parseInt(args[0]);
                final Punishment punishment = lib.easyInstance();
                punishment.setId(id);
                if (punishment.existsById()) {
                    final Punishment p = punishment.requireById();
                    lib.sendPunishmentStatus(s, p);
                }
            });
        } catch (Exception e) {
            new Message(PunishmentPlugin.getUsages().getString("Usages.punishview")).send(s);
        }
    }
}
