package me.centralworks.modules.punishments.models.punishs.supliers;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.centralworks.Main;
import me.centralworks.lib.Context;
import me.centralworks.lib.Contexts;
import me.centralworks.lib.General;
import me.centralworks.lib.Message;
import me.centralworks.modules.punishments.PunishmentPlugin;
import me.centralworks.modules.punishments.models.punishs.Punishment;
import me.centralworks.modules.punishments.models.punishs.PunishmentData;
import me.centralworks.modules.punishments.models.punishs.supliers.enums.PunishmentState;
import me.centralworks.modules.punishments.models.punishs.supliers.enums.PunishmentType;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.function.Consumer;

@Data
@RequiredArgsConstructor
public class Service implements Context {

    private String target;
    private String punisher = "Sistema";
    private PunishmentType punishmentType;
    private PunishmentReason punishmentReason;
    private List<String> evidences = Lists.newArrayList();
    private Consumer<Punishment> announcer;
    private Consumer<Punishment> functionIfOnline;
    private Consumer<Punishment> functionIfAddress;
    private String ip = "";
    private boolean permanent = false;
    private String secondaryIdentifier;

    public Service(String target, String punisher, PunishmentType punishmentType, PunishmentReason punishmentReason, List<String> evidences, Consumer<Punishment> announcer, Consumer<Punishment> functionIfOnline, Consumer<Punishment> functionIfAddress, String ip, boolean permanent, String secondaryIdentifier) {
        this.target = target;
        this.punisher = punisher;
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
