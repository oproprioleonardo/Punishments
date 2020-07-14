package me.centralworks.modules.punishments.models.punishs.supliers;

import me.centralworks.modules.punishments.models.punishs.supliers.enums.PunishmentType;

public class Reason {

    protected boolean none = false;
    private String reason = "Não especificado.";
    private Long duration = 0L;
    private String permission = "";
    private Boolean withIP = false;
    private PunishmentType punishmentType;
    private Boolean permanent = false;

    public Reason(String reason, Long duration, String permission, Boolean withIP, PunishmentType punishmentType, Boolean permanent) {
        this.reason = reason;
        this.duration = duration;
        this.permission = permission;
        this.withIP = withIP;
        this.punishmentType = punishmentType;
        this.permanent = permanent;
    }

    public Reason(String reason, Long duration, String permission, PunishmentType punishmentType) {
        this.reason = reason;
        this.duration = duration;
        this.permission = permission;
        this.punishmentType = punishmentType;
    }

    public Reason(String reason) {
        this.reason = reason;
        none = true;
    }

    public Reason() {
    }

    public Boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(Boolean permanent) {
        this.permanent = permanent;
    }

    public boolean isNone() {
        return none;
    }

    public void setNone(boolean none) {
        this.none = none;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public Boolean getWithIP() {
        return withIP;
    }

    public void setWithIP(Boolean withIP) {
        this.withIP = withIP;
    }

    public PunishmentType getPunishmentType() {
        return punishmentType;
    }

    public void setPunishmentType(PunishmentType punishmentType) {
        this.punishmentType = punishmentType;
    }

}
