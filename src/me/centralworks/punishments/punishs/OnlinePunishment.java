package me.centralworks.punishments.punishs;

import me.centralworks.punishments.Main;
import me.centralworks.punishments.db.dao.PunishmentDAO;
import me.centralworks.punishments.lib.MojangAPI;
import me.centralworks.punishments.punishs.supliers.enums.PunishmentState;
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

    public OnlinePunishment() {
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public String getIdentifier() {
        return uuid.toString();
    }

    @Override
    public void setIdentifier(String identifier) {
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

    @Override
    public String getName() {
        return isOnline() ? getPlayer().getName() : MojangAPI.getInstance().getName(getUuid().toString());
    }

    @Override
    public Punishment update() {
        if (this.getData().getPunishmentState() == PunishmentState.ACTIVE && this.getData().getFinishAt() != 0L && this.getData().getFinishAt() < System.currentTimeMillis()) {
            this.getData().setPunishmentState(PunishmentState.FINISHED);
            this.save();
        }
        return this;
    }

    public OnlinePunishment require() {
        return (OnlinePunishment) super.require().update();
    }

    public OnlinePunishment requireById() {
        return (OnlinePunishment) super.requireById().update();
    }
}
