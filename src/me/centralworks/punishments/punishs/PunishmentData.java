package me.centralworks.punishments.punishs;

import com.google.common.collect.Lists;
import me.centralworks.punishments.punishs.supliers.PunishmentReason;
import me.centralworks.punishments.punishs.supliers.cached.Reasons;
import me.centralworks.punishments.punishs.supliers.enums.PunishmentState;
import me.centralworks.punishments.punishs.supliers.enums.PunishmentType;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

public class PunishmentData {

    private Long startedAt;
    private Long finishAt;
    private String reason;
    private String punisher;
    private PunishmentState punishmentState;
    private List<String> evidences = Lists.newArrayList();
    private PunishmentType punishmentType;
    private boolean permanent;

    public PunishmentData() {
    }

    public PunishmentData(Long startedAt, Long finishAt, String reason, String punisher, PunishmentState punishmentState, List<String> evidences, PunishmentType punishmentType, boolean permanent) {
        this.startedAt = startedAt;
        this.finishAt = finishAt;
        this.reason = reason;
        this.punisher = punisher;
        this.punishmentState = punishmentState;
        this.evidences = evidences;
        this.punishmentType = punishmentType;
        this.permanent = permanent;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public PunishmentType getPunishmentType() {
        return punishmentType;
    }

    public void setPunishmentType(PunishmentType punishmentType) {
        this.punishmentType = punishmentType;
    }

    public Long getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Long startedAt) {
        this.startedAt = startedAt;
    }

    public Long getFinishAt() {
        return finishAt;
    }

    public void setFinishAt(Long finishAt) {
        this.finishAt = finishAt;
    }

    public String getReasonString() {
        return this.reason;
    }

    public PunishmentReason getReason() {
        return Reasons.getInstance().getByReason(getReasonString());
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getPunisher() {
        return punisher;
    }

    public void setPunisher(String punisher) {
        this.punisher = punisher;
    }

    public PunishmentState getPunishmentState() {
        return punishmentState;
    }

    public void setPunishmentState(PunishmentState punishmentState) {
        this.punishmentState = punishmentState;
    }

    public List<String> getEvidences() {
        return evidences;
    }

    public void setEvidences(List<String> evidences) {
        this.evidences = evidences;
    }

    public Date getStartDate() {
        return new Date(getStartedAt());
    }

    public Date getFinishDate() {
        return new Date(getFinishAt());
    }

    public Timestamp getStartDateSQL() {
        return new Timestamp(getStartedAt());
    }

    public Timestamp getFinishDateSQL() {
        return new Timestamp(getFinishAt());
    }

    public String getEvidencesFinally() {
        return getEvidences().isEmpty() ? "Nenhuma anexada" : evidences.get(0);
    }


}
