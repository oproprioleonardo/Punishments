package me.centralworks.punishments.warns;

public class Warn {

    private Integer id = 0;
    private String target;
    private String punisher;
    private String reason = "";
    private Long startedAt;
    private Long finishAt;
    private boolean permanent = false;

    public Warn(Integer id, String target, String punisher, String reason, Long startedAt, Long finishAt, boolean permanent) {
        this.id = id;
        this.target = target;
        this.punisher = punisher;
        this.reason = reason;
        this.startedAt = startedAt;
        this.finishAt = finishAt;
        this.permanent = permanent;
    }

    public Warn() {
    }

    public boolean idIsValid() {
        return id != null && id != 0;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getPunisher() {
        return punisher;
    }

    public void setPunisher(String punisher) {
        this.punisher = punisher;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
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

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }
}
