package com.leonardo.bungee.modules.punishments.cmds;

import com.google.common.collect.Lists;
import com.leonardo.bungee.modules.punishments.enums.Permission;
import com.leonardo.bungee.modules.punishments.models.Warn;
import com.leonardo.bungee.modules.punishments.models.supliers.Immune;
import com.leonardo.bungee.Main;
import com.leonardo.bungee.lib.Message;
import com.leonardo.bungee.modules.punishments.PunishmentPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class CmdWarn extends Command {

    public CmdWarn() {
        super("warn", "", "avisar");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        if (!Permission.hasPermission(s, Permission.WARN)) return;
        final ProxyServer proxy = Main.getInstance().getProxy();
        final Configuration cfg = PunishmentPlugin.getMessages();
        final String punisher = s instanceof ProxiedPlayer ? s.getName() : "Sistema";
        try {
            final String target = args[0];
            if (proxy.getPlayer(target) == null) {
                new Message(cfg.getString("Messages.only-player")).send(s);
                return;
            }
            if (!Immune.canGo(punisher, target)) return;
            final ProxiedPlayer t = proxy.getPlayer(target);
            final Warn warn = new Warn();
            warn.setTarget(t.getName());
            warn.setPunisher(punisher);
            warn.setPermanent(true);
            if (args.length > 1) {
                warn.setReason(String.join(" ", Lists.newArrayList(args).subList(1, args.length)));
            }
            new Message(cfg.getString("Messages.wait")).send(s);
            warn.saveAsync();
        } catch (Exception e) {
            new Message(PunishmentPlugin.getUsages().getString("Usages.warn")).send(s);
        }

    }
}
