package me.centralworks.punishments.cmds;

import com.google.common.collect.Lists;
import me.centralworks.punishments.Main;
import me.centralworks.punishments.enums.Permission;
import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.lib.Message;
import me.centralworks.punishments.warns.Warn;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class CmdWarn extends Command {

    public CmdWarn() {
        super("warn", Permission.WARN.getPermission(), "avisar");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        if (!Permission.hasPermission(s, Permission.WARN)) {
            new Message(Main.getMessages().getString("Messages.permission-error")).send(s);
            return;
        }
        final ProxyServer proxy = Main.getInstance().getProxy();
        final General lib = General.getGeneralLib();
        final Configuration cfg = Main.getMessages();
        final ProxiedPlayer p = ((ProxiedPlayer) s);
        final String punisher = s instanceof ProxiedPlayer ? s.getName() : "Sistema";
        try {
            final String target = args[0];
            if (proxy.getPlayer(target) == null) {
                new Message(cfg.getString("Messages.only-player")).send(s);
                return;
            }
            final ProxiedPlayer t = proxy.getPlayer(target);
            final Warn warn = new Warn();
            warn.setTarget(t.getName());
            warn.setPunisher(punisher);
            if (args.length == 1) {
                warn.setPermanent(true);
            } else {
                warn.setReason(String.join(" ", Lists.newArrayList(args).subList(1, args.length)));
            }
            new Message(cfg.getString("Messages.wait")).send(s);
            warn.saveAsync();
        } catch (Exception e) {
            new Message(Main.getUsages().getString("Usages.warn")).send(s);
        }

    }
}
