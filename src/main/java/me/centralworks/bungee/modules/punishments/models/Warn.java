package me.centralworks.bungee.modules.punishments.models;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.centralworks.bungee.modules.punishments.dao.WarnDAO;
import me.centralworks.bungee.modules.punishments.models.supliers.warns.Warns;

@Data
@RequiredArgsConstructor
public class Warn {

    private Integer id = 0;
    private String target, punisher;
    private String reason = "";
    private Long startedAt, finishAt = System.currentTimeMillis();
    private boolean permanent = false;

    public boolean idIsValid() {
        return id != null && id != 0;
    }

    public void saveAsync() {
        Warns.getInstance().add(this);
    }

    public void saveSync() {
        WarnDAO.getInstance().save(this);
    }
}
