package me.centralworks.bungee.modules.punishments.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.centralworks.bungee.modules.punishments.models.supliers.Elements;
import me.centralworks.bungee.modules.punishments.models.supliers.Request;
import me.centralworks.bungee.modules.punishments.models.supliers.cached.MutedPlayers;
import me.centralworks.bungee.modules.punishments.models.supliers.enums.PunishmentState;
import me.centralworks.bungee.modules.punishments.models.supliers.enums.PunishmentType;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Data
@RequiredArgsConstructor
public abstract class Punishment implements Information, Identifier {

    private Elements data;
    private String ip = "";
    private String secondaryIdentifier;
    private Integer id = 0;

    public Punishment(Elements data, String ip, Integer id) {
        this.data = data;
        this.ip = ip;
        this.id = id;
    }

    public Punishment(String secondaryIdentifier) {
        this.secondaryIdentifier = secondaryIdentifier;
    }

    public void pardon() {
        getData().setPunishmentState(PunishmentState.REVOKED);
        if (getData().getPunishmentType() == PunishmentType.MUTE || getData().getPunishmentType() == PunishmentType.TEMPMUTE) {
            if (MutedPlayers.getInstance().exists(getPrimaryIdentifier()))
                MutedPlayers.getInstance().remove(getPrimaryIdentifier());
        }
        new Request(this).save();
    }

    public boolean isOnline() {
        return false;
    }

    public ProxiedPlayer getPlayer() {
        return null;
    }

    public boolean dataIsLoaded() {
        return data != null;
    }

    public boolean ipIsValid() {
        return ip != null && !ip.equalsIgnoreCase("");
    }

    public boolean idIsValid() {
        return id != null && id != 0;
    }

    public Request query() {
        return new Request(this);
    }

}
