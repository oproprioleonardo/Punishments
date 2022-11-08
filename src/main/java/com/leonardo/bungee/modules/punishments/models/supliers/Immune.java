package com.leonardo.bungee.modules.punishments.models.supliers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import com.leonardo.bungee.Main;
import com.leonardo.bungee.lib.Message;
import com.leonardo.bungee.modules.punishments.PunishmentPlugin;

import java.util.List;

@Data
@RequiredArgsConstructor
public class Immune {

    private static List<String> usersImmune;

    public Immune(List<String> usersImmune) {
        Immune.usersImmune = usersImmune;
    }

    public static List<String> getUsersImmune() {
        return usersImmune;
    }

    public static void setUsersImmune(List<String> usersImmune) {
        Immune.usersImmune = usersImmune;
    }

    public static boolean canGo(String author, String target) {
        if (!author.equalsIgnoreCase("Sistema") && getUsersImmune().stream().anyMatch(s -> s.equalsIgnoreCase(target))) {
            new Message(PunishmentPlugin.getMessages().getString("Messages.immune")).send(Main.getInstance().getProxy().getPlayer(author));
            return false;
        }
        return true;
    }
}
