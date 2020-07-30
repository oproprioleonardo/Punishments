package me.centralworks.bungee.modules.punishments.models.supliers;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.centralworks.bungee.modules.punishments.dao.PunishmentDAO;
import me.centralworks.bungee.modules.punishments.models.Punishment;
import me.centralworks.bungee.modules.punishments.models.supliers.enums.PunishmentState;

import java.util.List;
import java.util.stream.Collectors;

@Data
@RequiredArgsConstructor
public class Request {

    private Punishment p;

    public Request(Punishment p) {
        this.p = p;
    }

    public Punishment requireByPrimaryIdentifier() {
        return PunishmentDAO.getInstance().loadByPrimaryIdentifier(p.getPrimaryIdentifier()).update();
    }

    public Punishment requireBySecondaryIdentifier() {
        return PunishmentDAO.getInstance().loadBySecondaryIdentifier(p.getSecondaryIdentifier()).update();
    }

    public Punishment requireByInstance() {
        return PunishmentDAO.getInstance().loadByObject(p).update();
    }

    public List<Punishment> requireAllByAddress() {
        return PunishmentDAO.getInstance().loadByIP(p.getIp()).stream().map(Request::update).collect(Collectors.toList());
    }

    public Punishment requireById() {
        if (p.getId() == null || p.getId() == 0) return null;
        return PunishmentDAO.getInstance().loadByID(p.getId()).update();
    }

    public List<Punishment> requireAllByPrimaryIdentifier() {
        return PunishmentDAO.getInstance().loadAllByPrimaryIdentifier(p.getPrimaryIdentifier()).stream().map(Request::update).collect(Collectors.toList());
    }

    public List<Punishment> requireAllBySecondaryIdentifier() {
        return PunishmentDAO.getInstance().loadAllBySecondaryIdentifier(p.getSecondaryIdentifier()).stream().map(Request::update).collect(Collectors.toList());
    }

    public boolean existsPrimaryIdentifier() {
        return PunishmentDAO.getInstance().existsPrimaryIdentifier(p.getPrimaryIdentifier());
    }

    public boolean existsSecondaryIdentifier() {
        return PunishmentDAO.getInstance().existsSecondaryIdentifier(p.getSecondaryIdentifier());
    }

    public boolean existsById() {
        if (p.getId() == null || p.getId() == 0) return false;
        return PunishmentDAO.getInstance().existsId(p.getId());
    }

    public boolean existsByAddress() {
        return PunishmentDAO.getInstance().existsIp(p.getIp());
    }

    public Punishment update() {
        if (p.getData().getPunishmentState() == PunishmentState.ACTIVE && !p.getData().isPermanent() && p.getData().getFinishAt() < System.currentTimeMillis()) {
            p.getData().setPunishmentState(PunishmentState.FINISHED);
            new Request(p).save();
        }
        return p;
    }

    public void delete() {
        PunishmentDAO.getInstance().delete(p);
    }

    public void save() {
        PunishmentDAO.getInstance().save(p);
    }

    public <T> T requireById(Class<T> classe) {
        return classe.cast(requireById());
    }

    public <T> T requireByPrimaryIdentifier(Class<T> classe) {
        return classe.cast(requireByPrimaryIdentifier());
    }

    public Punishment get() {
        return p;
    }
}
