package me.centralworks.punishments.cmds;

import me.centralworks.punishments.Main;
import me.centralworks.punishments.enums.Permission;
import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.lib.Message;
import me.centralworks.punishments.punishs.Punishment;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CmdPunishView extends Command {

    public CmdPunishView() {
        super("punishview", Permission.PUNISHVIEW.getPermission(), "pview");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        try {
            if (!(s instanceof ProxiedPlayer)) {
                new Message(Main.getMessages().getString("Messages.only-player")).send(s);
                return;
            }
            if (!Permission.hasPermission(s, Permission.PUNISHVIEW)) {
                new Message(Main.getMessages().getString("Messages.permission-error")).send(s);
                return;
            }
            final General lib = General.getGeneralLib();
            new Message(Main.getMessages().getString("Messages.wait")).send(s);
            BungeeCord.getInstance().getScheduler().runAsync(Main.getInstance(), () -> {
                final int id = Integer.parseInt(args[0]);
                final Punishment punishment = lib.easyInstance();
                punishment.setId(id);
                if (punishment.existsById()) {
                    final Punishment p = punishment.requireById();
                    lib.sendPunishmentStatus(s, p);
                }
            });
        } catch (Exception e) {
            new Message(Main.getUsages().getString("Usages.punishview")).send(s);
        }
    }
}
