package com.leonardo.bungee.modules.punishments.cmds;

import com.google.common.collect.Lists;
import com.leonardo.bungee.modules.punishments.enums.Permission;
import com.leonardo.bungee.Main;
import com.leonardo.bungee.lib.Functionalities;
import com.leonardo.bungee.lib.Message;
import com.leonardo.bungee.modules.punishments.PunishmentPlugin;
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
            final ProxiedPlayer p = Main.getInstance().getProxy().getPlayer(args[0]);
            if (args.length == 1) {
                Functionalities.kickPlayer(p, s instanceof ProxiedPlayer ? s.getName() : "Sistema", "");
            } else {
                final String reasonText = String.join(" ", Lists.newArrayList(args).subList(1, args.length));
                Functionalities.kickPlayer(p, s instanceof ProxiedPlayer ? s.getName() : "Sistema", reasonText);
            }
        } catch (Exception e) {
            new Message(PunishmentPlugin.getUsages().getString("Usages.kick")).send(s);
        }
    }
}