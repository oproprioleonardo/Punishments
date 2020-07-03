package me.centralworks.punishments.cmds;

import me.centralworks.punishments.Main;
import me.centralworks.punishments.db.dao.WarnDAO;
import me.centralworks.punishments.enums.Permission;
import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.lib.Message;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class CmdWarns extends Command {

    public CmdWarns() {
        super("warns", Permission.WARNS.getPermission(), "avisos");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        if (!Permission.hasPermission(s, Permission.UNWARN)) {
            new Message(Main.getMessages().getString("Messages.permission-error")).send(s);
            return;
        }
        final Configuration cfg = Main.getMessages();
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
            new Message(Main.getUsages().getString("Usages.warns")).send(s);
        }
    }
}
