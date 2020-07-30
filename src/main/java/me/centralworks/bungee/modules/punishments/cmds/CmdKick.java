package me.centralworks.bungee.modules.punishments.cmds;

import com.google.common.collect.Lists;
import me.centralworks.bungee.Main;
import me.centralworks.bungee.lib.General;
import me.centralworks.bungee.lib.Message;
import me.centralworks.bungee.modules.punishments.PunishmentPlugin;
import me.centralworks.bungee.modules.punishments.enums.Permission;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CmdKick extends Command {

    public CmdKick() {
        super("kick", "", "expulsar");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        try {
            if (!Permission.hasPermission(s, Permission.KICK)) return;
            final General lib = General.getGeneralLib();
            final ProxiedPlayer p = Main.getInstance().getProxy().getPlayer(args[0]);
            if (args.length == 1) {
                lib.kickPlayer(p, s instanceof ProxiedPlayer ? s.getName() : "Sistema", "");
            } else {
                final String reasonText = String.join(" ", Lists.newArrayList(args).subList(1, args.length));
                lib.kickPlayer(p, s instanceof ProxiedPlayer ? s.getName() : "Sistema", reasonText);
            }
        } catch (Exception e) {
            new Message(PunishmentPlugin.getUsages().getString("Usages.kick")).send(s);
        }
    }
}