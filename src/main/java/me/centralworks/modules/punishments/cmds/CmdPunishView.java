package me.centralworks.modules.punishments.cmds;

import me.centralworks.Main;
import me.centralworks.lib.General;
import me.centralworks.lib.Message;
import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.punishments.enums.Permission;
import me.centralworks.modules.punishments.models.Punishment;
import me.centralworks.modules.punishments.models.supliers.Request;
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
            if (!Permission.hasPermission(s, Permission.PUNISHVIEW)) return;
            final General lib = General.getGeneralLib();
            new Message(PunishmentPlugin.getMessages().getString("Messages.wait")).send(s);
            Main.getInstance().getProxy().getScheduler().runAsync(Main.getInstance(), () -> {
                final int id = Integer.parseInt(args[0]);
                final Punishment punishment = lib.easyInstance();
                punishment.setId(id);
                final Request request = new Request(punishment);
                if (request.existsById()) {
                    final Punishment p = request.requireById();
                    lib.sendPunishmentStatus(s, p);
                }
            });
        } catch (Exception e) {
            new Message(PunishmentPlugin.getUsages().getString("Usages.punishview")).send(s);
        }
    }
}
