package com.leonardo.bungee.modules.punishments.models.supliers;

import com.google.common.collect.Lists;
import com.leonardo.bungee.lib.Context;
import com.leonardo.bungee.lib.Contexts;
import com.leonardo.bungee.lib.Functionalities;
import com.leonardo.bungee.lib.Message;
import com.leonardo.bungee.modules.punishments.PunishmentPlugin;
import com.leonardo.bungee.modules.punishments.models.Punishment;
import com.leonardo.bungee.modules.punishments.models.supliers.enums.PunishmentState;
import com.leonardo.bungee.modules.punishments.models.supliers.enums.PunishmentType;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.function.Consumer;

@Data
@RequiredArgsConstructor
public class Service implements Context {

    private String target;
    private String punisher = "Sistema";
    private PunishmentType punishmentType;
    private Reason reason;
    private List<String> evidences = Lists.newArrayList();
    private Consumer<Punishment> announcer;
    private Consumer<Punishment> functionIfOnline;
    private Consumer<Punishment> functionIfAddress;
    private String ip = "";
    private boolean permanent = false;
    private String secondaryIdentifier;

    public Service(String target, String punisher, PunishmentType punishmentType, Reason reason, List<String> evidences, Consumer<Punishment> announcer, Consumer<Punishment> functionIfOnline, Consumer<Punishment> functionIfAddress, String ip, boolean permanent, String secondaryIdentifier) {
        this.target = target;
        this.punisher = punisher;
        this.punishmentType = punishmentType;
        this.reason = reason;
        this.evidences = evidences;
        this.announcer = announcer;
        this.functionIfOnline = functionIfOnline;
        this.functionIfAddress = functionIfAddress;
        this.ip = ip;
        this.permanent = permanent;
        this.secondaryIdentifier = secondaryIdentifier;
    }

    public Service(PunishmentType pt) {
        this.punishmentType = pt;
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

    public void setMuteFunctions(boolean ip) {
        final PFunctions functions = PFunctions.get();
        setFunctionIfOnline(functions.getFunctionMuteIfOn());
        setAnnouncer(functions.getFunctionAnnouncerMute());
        if (ip) setFunctionIfAddress(functions.getFunctionMuteIfAddress());
    }

    public void setBanFunctions(boolean ip) {
        final PFunctions functions = PFunctions.get();
        setFunctionIfOnline(functions.getFunctionBanIfOn());
        setAnnouncer(functions.getFunctionAnnouncerBan());
        if (ip) setFunctionIfAddress(functions.getFunctionBanIfAddress());
    }

    @Override
    public int getPlugin() {
        return 0;
    }

    @Override
    public String getWhoRequired() {
        return this.punisher;
    }

    @Override
    public void run() {
        if (getPunishmentType() == PunishmentType.BAN || getPunishmentType() == PunishmentType.TEMPBAN) {
            setBanFunctions(!getIp().equals(""));
        } else if (getPunishmentType() == PunishmentType.MUTE || getPunishmentType() == PunishmentType.TEMPMUTE) {
            setMuteFunctions(!getIp().equals(""));
        }
        execute();
    }

    @Override
    public void applyOtherInformation(ProxiedPlayer p) {
        Contexts.getInstance().add(this);
        new Message(PunishmentPlugin.getMessages().getString("Messages.write-evidences")).send(p);
    }

    @Override
    public void execute() {
        if (!Immune.canGo(getPunisher(), target)) return;
        final Punishment punishment = Functionalities.easyInstance(getTarget());
        final Elements pd = new Elements();
        final long now = System.currentTimeMillis();
        punishment.setIp(getIp());
        punishment.setSecondaryIdentifier(getSecondaryIdentifier());
        pd.setEvidences(Lists.newArrayList(getEvidences()));
        pd.setPunishmentType(getPunishmentType());
        pd.setPunishmentState(PunishmentState.ACTIVE);
        pd.setPunisher(getPunisher());
        pd.setReason(getReason().getReason());
        pd.setStartedAt(now);
        pd.setFinishAt(isPermanent() ? now : now + getReason().getDuration());
        pd.setPermanent(isPermanent());
        punishment.setData(pd);
        new Request(punishment).save();
        if (punishment.isOnline()) getFunctionIfOnline().accept(punishment);
        if (punishment.ipIsValid()) getFunctionIfAddress().accept(punishment);
        getAnnouncer().accept(punishment);
    }

}
