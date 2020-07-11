package me.centralworks.modules.reports.models.supliers;

import com.google.common.collect.Lists;
import me.centralworks.lib.Message;
import me.centralworks.modules.reports.ReportPlugin;
import me.centralworks.modules.reports.models.supliers.cached.Contexts;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

public class Context {

    private String target;
    private String victim;
    private Long date = System.currentTimeMillis();
    private String reason = "";
    private List<String> evidences = Lists.newArrayList();

    public Context(String target, String victim, Long date, String reason, List<String> evidences) {
        this.target = target;
        this.victim = victim;
        this.date = date;
        this.reason = reason;
        this.evidences = evidences;
    }

    public Context() {
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getVictim() {
        return victim;
    }

    public void setVictim(String victim) {
        this.victim = victim;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<String> getEvidences() {
        return evidences;
    }

    public void setEvidences(List<String> evidences) {
        this.evidences = evidences;
    }

    public void applyOtherInformation(ProxiedPlayer victim) {
        Contexts.getInstance().add(this);
        new Message(ReportPlugin.getMessages().getString("Messages.write-evidences")).send(victim);
    }
}
