package me.centralworks.modules.punishments.models.punishs;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.centralworks.Main;
import me.centralworks.modules.punishments.dao.PunishmentDAO;
import net.md_5.bungee.api.connection.ProxiedPlayer;

@Data
@RequiredArgsConstructor
public class OfflinePunishment extends Punishment {

    private String primaryIdentifier;

    public OfflinePunishment(PunishmentData punishmentData, String ip, Integer id) {
        super(punishmentData, ip, id);
    }

    public OfflinePunishment(String nickName) {
        this.primaryIdentifier = nickName;
    }

    @Override
    public String getPrimaryIdentifier() {
        return primaryIdentifier;
    }

    @Override
    public void setPrimaryIdentifier(String identifier) {
        this.primaryIdentifier = identifier;
    }

    @Override
    public void save() {
        PunishmentDAO.getInstance().save(this);
    }

    @Override
    public boolean isOnline() {
        return Main.getInstance().getProxy().getPlayer(getPrimaryIdentifier()) != null;
    }

    @Override
    public ProxiedPlayer getPlayer() {
        return Main.getInstance().getProxy().getPlayer(getPrimaryIdentifier());
    }


    public OfflinePunishment requireByPrimaryIdentifier() {
        return (OfflinePunishment) super.requireByPrimaryIdentifier().update();
    }

    public OfflinePunishment requireById() {
        return (OfflinePunishment) super.requireById().update();
    }
}
