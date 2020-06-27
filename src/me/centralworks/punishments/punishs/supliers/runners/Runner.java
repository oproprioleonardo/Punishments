package me.centralworks.punishments.punishs.supliers.runners;

import com.google.common.collect.Lists;
import me.centralworks.punishments.lib.General;
import me.centralworks.punishments.punishs.Punishment;
import me.centralworks.punishments.punishs.PunishmentData;
import me.centralworks.punishments.punishs.supliers.PunishmentReason;
import me.centralworks.punishments.punishs.supliers.enums.PunishmentState;
import me.centralworks.punishments.punishs.supliers.enums.PunishmentType;

import java.util.List;
import java.util.function.Consumer;

public abstract class Runner implements Execute{

    private String target;
    private String punisher = "Sistema";
    private Boolean isPreDefined = false;
    private PunishmentType punishmentType;
    private PunishmentReason punishmentReason;
    private List<String> evidences = Lists.newArrayList();
    private Consumer<Punishment> announcer;
    private Consumer<Punishment> functionIfOnline;
    private String ip = "";
    private boolean permanent = false;

    public Runner(String target, String punisher, Boolean isPreDefined, PunishmentType punishmentType, PunishmentReason punishmentReason, List<String> evidences, Consumer<Punishment> announcer, Consumer<Punishment> functionIfOnline, String ip, boolean permanent) {
        this.target = target;
        this.punisher = punisher;
        this.isPreDefined = isPreDefined;
        this.punishmentType = punishmentType;
        this.punishmentReason = punishmentReason;
        this.evidences = evidences;
        this.announcer = announcer;
        this.functionIfOnline = functionIfOnline;
        this.ip = ip;
        this.permanent = permanent;
    }

    public Runner() {
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
    }

    public void setAnnouncer(Consumer<Punishment> announcer) {
        this.announcer = announcer;
    }

    public Consumer<Punishment> getFunctionIfOnline() {
        return functionIfOnline;
    }

    public void setFunctionIfOnline(Consumer<Punishment> functionIfOnline) {
        this.functionIfOnline = functionIfOnline;
    }

    public Consumer<Punishment> getAnnouncer() {
        return announcer;
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

    public Boolean getPreDefined() {
        return isPreDefined;
    }

    public void setPreDefined(Boolean preDefined) {
        isPreDefined = preDefined;
    }

    public PunishmentType getPunishmentType() {
        return punishmentType;
    }

    public void setPunishmentType(PunishmentType punishmentType) {
        this.punishmentType = punishmentType;
    }

    public PunishmentReason getPunishmentReason() {
        return punishmentReason;
    }

    public void setPunishmentReason(PunishmentReason punishmentReason) {
        this.punishmentReason = punishmentReason;
    }

    public List<String> getEvidences() {
        return evidences;
    }

    public void setEvidences(List<String> evidences) {
        this.evidences = evidences;
    }

    public void attachEvidence(String evidence) {
        final List<String> copy = Lists.newArrayList(getEvidences());
        copy.add(evidence);
        setEvidences(copy);
    }

    public void removeEvidence(String evidence) {
        final List<String> copy = Lists.newArrayList(getEvidences());
        copy.remove(evidence);
        setEvidences(copy);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    @Override
    public void execute() {
        final General generalLib = General.getGeneralLib();
        final Punishment punishment = generalLib.easyInstance(getTarget(), getTarget());
        final PunishmentData pd = new PunishmentData();
        final long now = System.currentTimeMillis();
        pd.setEvidences(Lists.newArrayList(getEvidences()));
        pd.setPunishmentType(getPunishmentType());
        pd.setPunishmentState(PunishmentState.ACTIVE);
        pd.setPunisher(getPunisher());
        pd.setReason(getPunishmentReason().getReason());
        pd.setStartedAt(now);
        pd.setFinishAt(isPermanent() ? now : now + getPunishmentReason().getDuration());
        pd.setPermanent(isPermanent());
        punishment.setData(pd);
        punishment.save();
        if (punishment.isOnline()) getFunctionIfOnline().accept(punishment);
        getAnnouncer().accept(punishment);
    }
}
