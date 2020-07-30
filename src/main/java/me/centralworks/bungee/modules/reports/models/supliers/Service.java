package me.centralworks.bungee.modules.reports.models.supliers;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.centralworks.bungee.Main;
import me.centralworks.bungee.lib.Context;
import me.centralworks.bungee.lib.Contexts;
import me.centralworks.bungee.lib.General;
import me.centralworks.bungee.lib.Message;
import me.centralworks.bungee.modules.reports.ReportPlugin;
import me.centralworks.bungee.modules.reports.models.Report;
import me.centralworks.bungee.modules.reports.models.ReportedPlayer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Data
@RequiredArgsConstructor
public class Service implements Context {

    private String target;
    private String victim;
    private Long date = System.currentTimeMillis();
    private String reason = "";
    private List<String> evidences = Lists.newArrayList();

    public Service(String target, String victim, Long date, String reason, List<String> evidences) {
        this.target = target;
        this.victim = victim;
        this.date = date;
        this.reason = reason;
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

    public void applyOtherInformation(ProxiedPlayer victim) {
        Contexts.getInstance().add(this);
        new Message(ReportPlugin.getMessages().getString("Messages.write-evidences")).send(victim);
    }

    @Override
    public int getPlugin() {
        return 1;
    }

    @Override
    public String getWhoRequired() {
        return this.victim;
    }

    @Override
    public void run() {
        execute();
    }

    @Override
    public void execute() {
        final Request request = new Request(new ReportedPlayer(getTarget()));
        final ReportedPlayer rp;
        if (request.exists()) rp = request.request();
        else rp = request.get();
        final Report report = new Report();
        report.setDate(getDate());
        report.setReason(getReason());
        report.setEvidences(getEvidences());
        report.setVictim(getVictim());
        report.setId(ThreadLocalRandom.current().nextInt(0, 100000));
        rp.attachReport(report);
        new Request(rp).save();
        General.getGeneralLib().sendReport(rp, report.getId());
        new Message(ReportPlugin.getMessages().getString("Messages.reported").replace("{player}", getTarget())).send(Main.getInstance().getProxy().getPlayer(getVictim()));
        Delay.getInstance().add(getVictim());
    }

}
