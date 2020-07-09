package me.centralworks.modules.punishments.models.punishs.supliers;

import com.google.common.collect.Lists;
import me.centralworks.Main;
import me.centralworks.lib.General;
import me.centralworks.lib.Message;
import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.punishments.models.punishs.Punishment;
import me.centralworks.modules.punishments.models.punishs.PunishmentData;
import me.centralworks.modules.punishments.models.punishs.supliers.cached.Contexts;
import me.centralworks.modules.punishments.models.punishs.supliers.enums.PunishmentState;
import me.centralworks.modules.punishments.models.punishs.supliers.enums.PunishmentType;

import java.util.List;
import java.util.function.Consumer;

public class Context {

    private String target;
    private String punisher = "Sistema";
    private Boolean isPreDefined = false;
    private PunishmentType punishmentType;
    private PunishmentReason punishmentReason;
    private List<String> evidences = Lists.newArrayList();
    private Consumer<Punishment> announcer;
    private Consumer<Punishment> functionIfOnline;
    private Consumer<Punishment> functionIfAddress;
    private String ip = "";
    private boolean permanent = false;
    private String secondaryIdentifier;

    public Context(String target, String punisher, Boolean isPreDefined, PunishmentType punishmentType, PunishmentReason punishmentReason, List<String> evidences, Consumer<Punishment> announcer, Consumer<Punishment> functionIfOnline, Consumer<Punishment> functionIfAddress, String ip, boolean permanent, String secondaryIdentifier) {
        this.target = target;
        this.punisher = punisher;
        this.isPreDefined = isPreDefined;
        this.punishmentType = punishmentType;
        this.punishmentReason = punishmentReason;
        this.evidences = evidences;
        this.announcer = announcer;
        this.functionIfOnline = functionIfOnline;
        this.functionIfAddress = functionIfAddress;
        this.ip = ip;
        this.permanent = permanent;
        this.secondaryIdentifier = secondaryIdentifier;
    }

    public Context(PunishmentType pt) {
        this.punishmentType = pt;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public void setPermanent(boolean permanent) {
        this.permanent = permanent;
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

    public void setAnnouncer(Consumer<Punishment> announcer) {
        this.announcer = announcer;
    }

    public Consumer<Punishment> getFunctionIfAddress() {
        return functionIfAddress;
    }

    public void setFunctionIfAddress(Consumer<Punishment> functionIfAddress) {
        this.functionIfAddress = functionIfAddress;
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

    public String getSecondaryIdentifier() {
        return secondaryIdentifier;
    }

    public void setSecondaryIdentifier(String secondaryIdentifier) {
        this.secondaryIdentifier = secondaryIdentifier;
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

    public void setMuteFunctions(boolean ip) {
        final General generalLib = General.getGeneralLib();
        setFunctionIfOnline(generalLib.getFunctionMuteIfOn());
        setAnnouncer(generalLib.getFunctionAnnouncerMute());
        if (ip) setFunctionIfAddress(generalLib.getFunctionMuteIfAddress());
    }

    public void setBanFunctions(boolean ip) {
        final General generalLib = General.getGeneralLib();
        setFunctionIfOnline(generalLib.getFunctionBanIfOn());
        setAnnouncer(generalLib.getFunctionAnnouncerBan());
        if (ip) setFunctionIfAddress(generalLib.getFunctionBanIfAddress());
    }

    public void run() {
        if (getPunishmentType() == PunishmentType.BAN || getPunishmentType() == PunishmentType.TEMPBAN) {
            setBanFunctions(!getIp().equals(""));
        } else if (getPunishmentType() == PunishmentType.MUTE || getPunishmentType() == PunishmentType.TEMPMUTE) {
            setMuteFunctions(!getIp().equals(""));
        }
        execute();
    }

    public void addTask() {
        Contexts.getInstance().add(punisher, this);
    }

    private void execute() {
        if (!getTarget().equalsIgnoreCase("Sistema") && Main.getUsersImmune().stream().anyMatch(s -> s.equalsIgnoreCase(getTarget()))) {
            if (Main.getInstance().getProxy().getPlayer(getTarget()) != null)
                new Message(PunishmentPlugin.getMessages().getString("Messages.immune")).send(Main.getInstance().getProxy().getPlayer(getTarget()));
            return;
        }
        final General generalLib = General.getGeneralLib();
        final Punishment punishment = generalLib.easyInstance(getTarget(), getTarget());
        final PunishmentData pd = new PunishmentData();
        final long now = System.currentTimeMillis();
        punishment.setIp(getIp());
        punishment.setSecondaryIdentifier(getSecondaryIdentifier());
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
        if (punishment.isOnline()) {
            getFunctionIfOnline().accept(punishment);
        }
        if (punishment.ipIsValid()) {
            getFunctionIfAddress().accept(punishment);
        }
        getAnnouncer().accept(punishment);
    }

}
