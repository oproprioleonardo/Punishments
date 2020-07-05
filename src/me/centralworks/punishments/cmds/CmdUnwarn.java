package me.centralworks.punishments.cmds;

import me.centralworks.punishments.Main;
import me.centralworks.punishments.db.dao.WarnDAO;
import me.centralworks.punishments.enums.Permission;
import me.centralworks.punishments.lib.Message;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class CmdUnwarn extends Command {

    public CmdUnwarn() {
        super("unwarn", Permission.UNWARN.getPermission(), "desavisar");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        if (!Permission.hasPermission(s, Permission.UNWARN)) {
            new Message(Main.getMessages().getString("Messages.permission-error")).send(s);
            return;
        }
        final Configuration cfg = Main.getMessages();
        try {
            final Integer id = Integer.valueOf(args[0]);
            new Message(cfg.getString("Messages.wait")).send(s);
            BungeeCord.getInstance().getScheduler().runAsync(Main.getInstance(), () -> {
                if (WarnDAO.getInstance().existsID(id)) {
                    WarnDAO.getInstance().deleteById(id);
                    new Message(cfg.getString("Messages.warn-deleted")).send(s);
                } else new Message(cfg.getString("Messages.warn-not-found")).send(s);
            });
        } catch (NumberFormatException e) {
            final String target = args[0];
            new Message(cfg.getString("Messages.wait")).send(s);
            BungeeCord.getInstance().getScheduler().runAsync(Main.getInstance(), () -> {
                if (WarnDAO.getInstance().existsNickname(target)) {
                    WarnDAO.getInstance().deleteAllByNickname(target);
                    new Message(cfg.getString("Messages.warn-deleted")).send(s);
                } else new Message(cfg.getString("Messages.warn-not-found")).send(s);
            });
        } catch (Exception ex) {
            new Message(Main.getUsages().getString("Usages.unwarn")).send(s);
        }

    }
}
