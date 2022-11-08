package com.leonardo.bungee.modules.punishments.cmds;

import com.leonardo.bungee.modules.punishments.enums.Permission;
import com.leonardo.bungee.Main;
import com.leonardo.bungee.lib.Functionalities;
import com.leonardo.bungee.lib.Message;
import com.leonardo.bungee.modules.punishments.PunishmentPlugin;
import com.leonardo.bungee.modules.punishments.dao.WarnDAO;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class CmdWarns extends Command {

    public CmdWarns() {
        super("warns", "", "avisos");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        if (!Permission.hasPermission(s, Permission.WARNS)) return;
        final Configuration cfg = PunishmentPlugin.getMessages();
        try {
            final String target = args[0];
            new Message(cfg.getString("Messages.wait")).send(s);
            Main.getInstance().getProxy().getScheduler().runAsync(Main.getInstance(), () -> {
                if (WarnDAO.getInstance().existsNickname(target)) {
                    Functionalities.sendHistoryWarn(s, Functionalities.updateAllWarns(WarnDAO.getInstance().loadAllWarns(target)));
                } else new Message("Messages.warn-not-found").send(s);
            });
        } catch (Exception e) {
            new Message(PunishmentPlugin.getUsages().getString("Usages.warns")).send(s);
        }
    }
}
