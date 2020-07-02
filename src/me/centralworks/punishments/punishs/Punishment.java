package me.centralworks.punishments.punishs;

import me.centralworks.punishments.db.dao.PunishmentDAO;
import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.punishs.supliers.enums.PunishmentState;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public abstract class Punishment implements Data, Identifier, DAO {

    private PunishmentData punishmentData;
    private String ip = "";
    private String breakNick;
    private Integer id = 0;

    public Punishment(PunishmentData punishmentData, String ip, Integer id) {
        this.punishmentData = punishmentData;
        this.ip = ip;
        this.id = id;
    }

    /**
     * necessary evil
     *
     * @param breakNick nickname player.
     */
    public Punishment(String breakNick) {
        this.breakNick = breakNick;
    }

    public Punishment() {
    }

    public void pardon() {
        getPunishmentData().setPunishmentState(PunishmentState.REVOKED);
        this.save();
    }

    public PunishmentData getPunishmentData() {
        return punishmentData;
    }

    public void setPunishmentData(PunishmentData punishmentData) {
        this.punishmentData = punishmentData;
    }

    @Override
    public String getSecondaryIdentifier() {
        return breakNick;
    }

    @Override
    public void setSecondaryIdentifier(String identifier) {
        this.breakNick = identifier;
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
        return ip != null && !ip.equalsIgnoreCase("");
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

    // DAO METHODS

    @Override
    public Punishment requireByPrimaryIdentifier() {
        return PunishmentDAO.getInstance().loadByPrimaryIdentifier(getPrimaryIdentifier()).update();
    }

    @Override
    public Punishment requireBySecondaryIdentifier() {
        return PunishmentDAO.getInstance().loadBySecondaryIdentifier(getSecondaryIdentifier()).update();
    }

    @Override
    public Punishment requireByInstance() {
        return PunishmentDAO.getInstance().loadByObject(this).update();
    }

    @Override
    public List<Punishment> requireAllByAddress() {
        return General.getGeneralLib().updateAll(PunishmentDAO.getInstance().loadByIP(getIp()));
    }

    @Override
    public Punishment requireById() {
        if (id == null || id == 0) return null;
        return PunishmentDAO.getInstance().loadByID(getId()).update();
    }

    @Override
    public List<Punishment> requireAllByPrimaryIdentifier() {
        return General.getGeneralLib().updateAll(PunishmentDAO.getInstance().loadAllByPrimaryIdentifier(getPrimaryIdentifier()));
    }

    @Override
    public List<Punishment> requireAllBySecondaryIdentifier() {
        return General.getGeneralLib().updateAll(PunishmentDAO.getInstance().loadAllBySecondaryIdentifier(getSecondaryIdentifier()));
    }

    @Override
    public boolean existsPrimaryIdentifier() {
        return PunishmentDAO.getInstance().existsPrimaryIdentifier(getPrimaryIdentifier());
    }

    @Override
    public boolean existsSecondaryIdentifier() {
        return PunishmentDAO.getInstance().existsSecondaryIdentifier(getSecondaryIdentifier());
    }

    @Override
    public boolean existsById() {
        if (id == null || id == 0) return false;
        return PunishmentDAO.getInstance().existsId(getId());
    }


    @Override
    public boolean existsByAddress() {
        return PunishmentDAO.getInstance().existsIp(getIp());
    }

    @Override
    public Punishment update() {
        if (this.getData().getPunishmentState() == PunishmentState.ACTIVE && !this.getData().isPermanent() && this.getData().getFinishAt() < System.currentTimeMillis()) {
            this.getData().setPunishmentState(PunishmentState.FINISHED);
            this.save();
        }
        return this;
    }

    @Override
    public void delete() {
        PunishmentDAO.getInstance().delete(this);
    }
}
