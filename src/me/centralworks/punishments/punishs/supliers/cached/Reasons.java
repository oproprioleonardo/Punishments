package me.centralworks.punishments.punishs.supliers.cached;

import com.google.common.collect.Lists;
import me.centralworks.punishments.punishs.supliers.PunishmentReason;

import java.util.List;

public class Reasons {

    protected static Reasons instance;
    public List<PunishmentReason> reasons = Lists.newArrayList();

    public static Reasons getInstance() {
        if (instance == null) instance = new Reasons();
        return instance;
    }

    public List<PunishmentReason> getReasons() {
        return reasons;
    }

    public void setReasons(List<PunishmentReason> reasons) {
        this.reasons = reasons;
    }

    public boolean exists(String reason) {
        return getReasons().stream().anyMatch(pr -> pr.getReason().equalsIgnoreCase(reason));
    }

    public PunishmentReason getByReason(String reason) {
        return getReasons().stream().filter(pr -> pr.getReason().equalsIgnoreCase(reason)).findFirst().orElse(new PunishmentReason(reason));
    }

}
