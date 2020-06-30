package me.centralworks.punishments.lib;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Message {

    private final String message;

    public Message(String message) {
        this.message = message;
    }

    public void send(ProxiedPlayer p) {
        p.sendMessage(getColorfulMessage());
    }

    public void send(CommandSender cs) {
        cs.sendMessage(getColorfulMessage());
    }

    public String getMessage() {
        return message;
    }

    public void sendJson(final String runCmd, final String hoverMessage, final CommandSender s) {
        final TextComponent text = new TextComponent(TextComponent.fromLegacyText(message));
        if (!runCmd.equals("")) text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, runCmd));
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', hoverMessage)).create()));
        s.sendMessage(text);
    }

    public void sendJson(final String runCmd, final String hoverMessage, final ProxiedPlayer s) {
        final TextComponent text = new TextComponent(message);
        text.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, runCmd));
        text.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder(ChatColor.translateAlternateColorCodes('&', hoverMessage)).create()));
        s.sendMessage(text);
    }

    public BaseComponent[] getColorfulMessage() {
        return TextComponent.fromLegacyText(message.replace("&", "§"));
    }

}
