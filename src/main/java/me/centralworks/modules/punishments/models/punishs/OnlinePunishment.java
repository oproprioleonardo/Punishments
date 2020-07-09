package me.centralworks.modules.punishments.models.punishs;

import me.centralworks.Main;
import me.centralworks.modules.punishments.dao.PunishmentDAO;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.UUID;

public class OnlinePunishment extends Punishment {

    private UUID uuid;

    public OnlinePunishment(PunishmentData punishmentData, String ip, Integer id) {
        super(punishmentData, ip, id);
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

    public OnlinePunishment() {
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
    public void save() {
        PunishmentDAO.getInstance().save(this);
    }

    @Override
    public boolean isOnline() {
        return Main.getInstance().getProxy().getPlayer(getUuid()) != null;
    }

    @Override
    public ProxiedPlayer getPlayer() {
        return Main.getInstance().getProxy().getPlayer(getUuid());
    }

    public OnlinePunishment requireByPrimaryIdentifier() {
        return (OnlinePunishment) super.requireByPrimaryIdentifier().update();
    }

    public OnlinePunishment requireById() {
        return (OnlinePunishment) super.requireById().update();
    }
}
