package me.centralworks.punishments.punishs;

import me.centralworks.punishments.Main;
import me.centralworks.punishments.db.dao.PunishmentDAO;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class OfflinePunishment extends Punishment {

    private String nickName;

    public OfflinePunishment(PunishmentData punishmentData, String ip, Integer id) {
        super(punishmentData, ip, id);
    }

    public OfflinePunishment(String nickName) {
        this.nickName = nickName;
    }

    public OfflinePunishment() {
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    @Override
    public String getIdentifier() {
        return nickName;
    }

    @Override
    public void setIdentifier(String identifier) {
        this.nickName = identifier;
    }

    @Override
    public void save() {
        PunishmentDAO.getInstance().save(this);
    }

    @Override
    public boolean isOnline() {
        return Main.getInstance().getProxy().getPlayer(getNickName()) != null;
    }

    @Override
    public ProxiedPlayer getPlayer() {
        return Main.getInstance().getProxy().getPlayer(getNickName());
    }

    @Override
    public String getName() {
        return getNickName();
    }

    public OfflinePunishment require() {
        return (OfflinePunishment) super.require().update();
    }

    public OfflinePunishment requireById() {
        return (OfflinePunishment) super.requireById().update();
    }
}
