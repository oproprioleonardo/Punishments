package me.centralworks.bungee.modules.punishments.cmds;

import me.centralworks.bungee.lib.Message;
import me.centralworks.bungee.modules.punishments.PunishmentPlugin;
import me.centralworks.bungee.modules.punishments.enums.Permission;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class CmdCheck extends Command {

    public CmdCheck() {
        super("check", "", "scanner");
    }

    @Override
    public void execute(CommandSender s, String[] args) {
        try {
            if (!Permission.hasPermission(s, Permission.CHECK)) return;

        } catch (Exception e) {
            new Message(PunishmentPlugin.getUsages().getString("Usages.check")).send(s);
        }
    }
}
