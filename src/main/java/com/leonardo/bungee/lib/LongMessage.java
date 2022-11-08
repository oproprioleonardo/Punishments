package com.leonardo.bungee.lib;

import com.leonardo.bungee.modules.punishments.PunishmentPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.stream.Collectors;

public class LongMessage {

    private List<String> stringList;

    public LongMessage(List<String> stringList) {
        this.stringList = stringList;
    }

    public LongMessage(String pathMessage) {
        this.stringList = PunishmentPlugin.getMessages().getStringList(pathMessage);
    }

    public List<String> getStringList() {
        return stringList;
    }

    public void setStringList(List<String> stringList) {
        this.stringList = stringList;
    }

    public List<String> getColorfulList() {
        return stringList.stream().map(s -> s.replace("&", "§")).collect(Collectors.toList());
    }

    public List<BaseComponent[]> getColorfulMessage() {
        return stringList.stream().map(TextComponent::fromLegacyText).collect(Collectors.toList());
    }

    public void send(ProxiedPlayer proxiedPlayer) {
        getColorfulMessage().forEach(proxiedPlayer::sendMessage);
    }

    public void send(CommandSender commandSender) {
        getColorfulMessage().forEach(commandSender::sendMessage);
    }

}
