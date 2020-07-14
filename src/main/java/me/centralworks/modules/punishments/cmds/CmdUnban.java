package me.centralworks.modules.punishments.cmds;

import me.centralworks.Main;
import me.centralworks.lib.General;
import me.centralworks.lib.Message;
import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.punishments.enums.Permission;
import me.centralworks.modules.punishments.models.punishs.Punishment;
import me.centralworks.modules.punishments.models.punishs.supliers.CheckUp;
import me.centralworks.modules.punishments.models.punishs.supliers.Filter;
import me.centralworks.modules.punishments.models.punishs.supliers.Request;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

import java.util.List;

public class CmdUnban extends Command {

    public CmdUnban() {
        super("unban", "", "desbanir");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        final Configuration cfg = PunishmentPlugin.getMessages();
        final General gnrlLib = General.getGeneralLib();
        if (!Permission.hasPermission(s, Permission.UNBAN)) {
            new Message(PunishmentPlugin.getMessages().getString("Messages.permission-error")).send(s);
            return;
        }
        new Message(PunishmentPlugin.getMessages().getString("Messages.wait")).send(s);
        Main.getInstance().getProxy().getScheduler().runAsync(Main.getInstance(), () -> {
            try {
                final Punishment punishment = gnrlLib.easyInstance();
                punishment.setSecondaryIdentifier(args[0]);
                final List<Punishment> punishments = new Request(punishment).requireAllBySecondaryIdentifier();
                final CheckUp checkUp = new CheckUp(punishments);
                if (checkUp.hasActivePunishment() && checkUp.hasPunishmentBan()) {
                    final List<Punishment> ps = new Filter(punishments).getAllBannedPActive();
                    ps.forEach(Punishment::pardon);
                    new Message(cfg.getString("Messages.ban-pardon")).send(s);
                } else new Message(cfg.getString("Messages.ban-not-found")).send(s);
            } catch (Exception e) {
                new Message(PunishmentPlugin.getUsages().getString("Usages.unban")).send(s);
            }
        });

    }
}
