package me.centralworks.modules.punishments.cmds;

import com.google.common.collect.Lists;
import me.centralworks.Main;
import me.centralworks.lib.General;
import me.centralworks.lib.Message;
import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.punishments.enums.Permission;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class CmdKick extends Command {

    public CmdKick() {
        super("kick", "", "expulsar");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        try {
            if (!Permission.hasPermission(s, Permission.KICK)) {
                new Message(PunishmentPlugin.getMessages().getString("Messages.permission-error")).send(s);
                return;
            }
            final ProxyServer proxy = Main.getInstance().getProxy();
            final General lib = General.getGeneralLib();
            final Configuration cfg = PunishmentPlugin.getMessages();
            final ProxiedPlayer p = BungeeCord.getInstance().getPlayer(args[0]);
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