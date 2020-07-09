package me.centralworks.modules.punishments.cmds;

import me.centralworks.Main;
import me.centralworks.lib.General;
import me.centralworks.lib.Message;
import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.punishments.dao.WarnDAO;
import me.centralworks.modules.punishments.enums.Permission;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class CmdWarns extends Command {

    public CmdWarns() {
        super("warns", "", "avisos");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        if (!Permission.hasPermission(s, Permission.WARNS)) {
            new Message(PunishmentPlugin.getMessages().getString("Messages.permission-error")).send(s);
            return;
        }
        final Configuration cfg = PunishmentPlugin.getMessages();
        try {
            final String target = args[0];
            new Message(cfg.getString("Messages.wait")).send(s);
            BungeeCord.getInstance().getScheduler().runAsync(Main.getInstance(), () -> {
                if (WarnDAO.getInstance().existsNickname(target)) {
                    final General lib = General.getGeneralLib();
                    lib.sendHistoryWarn(s, lib.updateAllWarns(WarnDAO.getInstance().loadAllWarns(target)));
                } else new Message("Messages.warn-not-found").send(s);
            });
        } catch (Exception e) {
            new Message(PunishmentPlugin.getUsages().getString("Usages.warns")).send(s);
        }
    }
}
