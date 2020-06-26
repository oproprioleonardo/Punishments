package me.centralworks.punishments.lib;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
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

    public BaseComponent[] getColorfulMessage() {
        return TextComponent.fromLegacyText(message.replace("&", "§"));
    }

}
