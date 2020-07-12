package me.centralworks.modules.reports.models.supliers;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.centralworks.lib.Context;
import me.centralworks.lib.Contexts;
import me.centralworks.lib.Message;
import me.centralworks.modules.reports.ReportPlugin;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.List;

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
    public String getWhoRequired() {
        return this.victim;
    }

    @Override
    public void run() {

    }

    @Override
    public void execute() {

    }

}
