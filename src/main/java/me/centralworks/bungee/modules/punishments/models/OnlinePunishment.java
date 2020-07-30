package me.centralworks.bungee.modules.punishments.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import me.centralworks.bungee.Main;
import me.centralworks.bungee.modules.punishments.models.supliers.Elements;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = false)
@RequiredArgsConstructor
public class OnlinePunishment extends Punishment {

    private UUID uuid;

    public OnlinePunishment(Elements elements, String ip, Integer id) {
        super(elements, ip, id);
    }

    public OnlinePunishment(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * necessary evil
     *
     * @param breakNick nickname player.
     */
    public OnlinePunishment(String breakNick) {
        super(breakNick);
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getPrimaryIdentifier() {
        return uuid.toString();
    }

    @Override
    public void setPrimaryIdentifier(String identifier) {
        this.uuid = UUID.fromString(identifier);
    }

    @Override
    public boolean isOnline() {
        return Main.getInstance().getProxy().getPlayer(getUuid()) != null;
    }

    @Override
    public ProxiedPlayer getPlayer() {
        return Main.getInstance().getProxy().getPlayer(getUuid());
    }

}
