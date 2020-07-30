package me.centralworks.bungee.modules.punishments.cmds;

import me.centralworks.bungee.Main;
import me.centralworks.bungee.lib.Message;
import me.centralworks.bungee.modules.punishments.PunishmentPlugin;
import me.centralworks.bungee.modules.punishments.dao.WarnDAO;
import me.centralworks.bungee.modules.punishments.enums.Permission;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class CmdUnwarn extends Command {

    public CmdUnwarn() {
        super("unwarn", "", "desavisar");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        if (!Permission.hasPermission(s, Permission.UNWARN)) return;
        final Configuration cfg = PunishmentPlugin.getMessages();
        try {
            final Integer id = Integer.valueOf(args[0]);
            new Message(cfg.getString("Messages.wait")).send(s);
            Main.getInstance().getProxy().getScheduler().runAsync(Main.getInstance(), () -> {
                if (WarnDAO.getInstance().existsID(id)) {
                    WarnDAO.getInstance().deleteById(id);
                    new Message(cfg.getString("Messages.warn-deleted")).send(s);
                } else new Message(cfg.getString("Messages.warn-not-found")).send(s);
            });
        } catch (NumberFormatException e) {
            final String target = args[0];
            new Message(cfg.getString("Messages.wait")).send(s);
            Main.getInstance().getProxy().getScheduler().runAsync(Main.getInstance(), () -> {
                if (WarnDAO.getInstance().existsNickname(target)) {
                    WarnDAO.getInstance().deleteAllByNickname(target);
                    new Message(cfg.getString("Messages.warn-deleted")).send(s);
                } else new Message(cfg.getString("Messages.warn-not-found")).send(s);
            });
        } catch (Exception ex) {
            new Message(PunishmentPlugin.getUsages().getString("Usages.unwarn")).send(s);
        }

    }
}
