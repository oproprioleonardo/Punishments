package me.centralworks.modules.punishments.cmds;

import com.google.common.collect.Lists;
import me.centralworks.Main;
import me.centralworks.lib.Date;
import me.centralworks.lib.Message;
import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.punishments.enums.Permission;
import me.centralworks.modules.punishments.models.Warn;
import me.centralworks.modules.punishments.models.supliers.Immune;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.config.Configuration;

public class CmdTempWarn extends Command {

    public CmdTempWarn() {
        super("tempwarn", "", "tempavisar");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        if (!Permission.hasPermission(s, Permission.TEMPWARN)) return;
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
            warn.setFinishAt(Date.getInstance().convertPunishmentDuration(Lists.newArrayList(args[1].split(","))));
            if (args.length > 2) {
                warn.setReason(String.join(" ", Lists.newArrayList(args).subList(2, args.length)));
            }
            new Message(cfg.getString("Messages.wait")).send(s);
            warn.saveAsync();
        } catch (Exception e) {
            new Message(PunishmentPlugin.getUsages().getString("Usages.tempwarn")).send(s);
        }
    }
}
