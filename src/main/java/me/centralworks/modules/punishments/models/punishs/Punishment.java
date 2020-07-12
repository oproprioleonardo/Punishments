package me.centralworks.modules.punishments.models.punishs;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.centralworks.lib.General;
import me.centralworks.modules.punishments.dao.PunishmentDAO;
import me.centralworks.modules.punishments.models.punishs.supliers.cached.MutedPlayers;
import me.centralworks.modules.punishments.models.punishs.supliers.enums.PunishmentState;
import me.centralworks.modules.punishments.models.punishs.supliers.enums.PunishmentType;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

@Data
@RequiredArgsConstructor
public abstract class Punishment implements Information, Identifier, DAO {

    private PunishmentData data;
    private String ip = "";
    private String secondaryIdentifier;
    private Integer id = 0;

    public Punishment(PunishmentData data, String ip, Integer id) {
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
        this.save();
    }

    public boolean isOnline() {
        return false;
    }

    public ProxiedPlayer getPlayer() {
        return null;
    }

    @Override
    public boolean dataIsLoaded() {
        return data != null;
    }

    @Override
    public boolean ipIsValid() {
        return ip != null && !ip.equalsIgnoreCase("");
    }

    @Override
    public boolean idIsValid() {
        return id != null && id != 0;
    }

    // CRUD

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
