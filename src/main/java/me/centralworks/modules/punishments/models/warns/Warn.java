package me.centralworks.modules.punishments.models.warns;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.centralworks.modules.punishments.dao.WarnDAO;
import me.centralworks.modules.punishments.models.warns.supliers.Warns;

@Data
@RequiredArgsConstructor
public class Warn {

    private Integer id = 0;
    private String target;
    private String punisher;
    private String reason = "";
    private Long startedAt = System.currentTimeMillis();
    private Long finishAt = System.currentTimeMillis();
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
