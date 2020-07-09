package me.centralworks.modules.punishments.models.punishs;

import me.centralworks.Main;
import me.centralworks.modules.punishments.dao.PunishmentDAO;
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
    public String getPrimaryIdentifier() {
        return nickName;
    }

    @Override
    public void setPrimaryIdentifier(String identifier) {
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

    public OfflinePunishment requireByPrimaryIdentifier() {
        return (OfflinePunishment) super.requireByPrimaryIdentifier().update();
    }

    public OfflinePunishment requireById() {
        return (OfflinePunishment) super.requireById().update();
    }
}
