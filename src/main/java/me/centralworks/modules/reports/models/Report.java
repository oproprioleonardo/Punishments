package me.centralworks.modules.reports.models;

import com.google.common.collect.Lists;

import java.util.List;

public class Report {

    private Integer id;
    private String victim;
    private String reason = "";
    private Long date = System.currentTimeMillis();
    private List<String> evidences = Lists.newArrayList();

    public Report(Integer id, String victim, String reason, Long date, List<String> evidences) {
        this.id = id;
        this.victim = victim;
        this.reason = reason;
        this.date = date;
        this.evidences = evidences;
    }

    public Report() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getDate() {
        return date;
    }

    public void setDate(Long date) {
        this.date = date;
    }

    public String getVictim() {
        return victim;
    }

    public void setVictim(String victim) {
        this.victim = victim;
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
}
