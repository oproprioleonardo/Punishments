package me.centralworks.punishments.punishs;

import me.centralworks.punishments.db.dao.PunishmentDAO;
import me.centralworks.punishments.lib.General;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public abstract class Punishment implements Data, Identifier, DAO {

    private PunishmentData punishmentData;
    private String ip;
    private Integer id = 0;

    public Punishment(PunishmentData punishmentData, String ip, Integer id) {
        this.punishmentData = punishmentData;
        this.ip = ip;
        this.id = id;
    }

    public Punishment() {
    }

    public boolean isOnline() {
        return false;
    }

    public ProxiedPlayer getPlayer() {
        return null;
    }

    @Override
    public PunishmentData getData() {
        return punishmentData;
    }

    @Override
    public void setData(PunishmentData punishmentData) {
        this.punishmentData = punishmentData;
    }

    @Override
    public boolean dataIsLoaded() {
        return punishmentData != null;
    }

    @Override
    public String getIp() {
        return ip;
    }

    @Override
    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public boolean ipIsValid() {
        return ip != null;
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public boolean idIsValid() {
        return id != null && id != 0;
    }

    @Override
    public Punishment require() {
        return PunishmentDAO.getInstance().loadByIdentifier(getIdentifier());
    }

    @Override
    public boolean exists() {
        return PunishmentDAO.getInstance().existsIdentifier(getIdentifier());
    }

    @Override
    public boolean existsById() {
        if (id == null || id == 0) return false;
        return PunishmentDAO.getInstance().existsId(getId());
    }

    @Override
    public Punishment requireById() {
        if (id == null || id == 0) return null;
        return PunishmentDAO.getInstance().loadByID(getId());
    }

    @Override
    public List<Punishment> requireAll() {
        return General.getGeneralLib().updateAll(PunishmentDAO.getInstance().loadAllByIdentifier(getIdentifier()));
    }
}
