package me.centralworks.punishments.cmds;

import me.centralworks.punishments.enums.Permission;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class CmdUnwarn extends Command {

    public CmdUnwarn() {
        super("unwarn", Permission.UNWARN.getPermission(), "desavisar");
    }

    @Override
    public void execute(CommandSender s, String[] args) {

    }
}
