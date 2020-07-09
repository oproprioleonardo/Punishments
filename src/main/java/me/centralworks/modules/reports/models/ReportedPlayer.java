package me.centralworks.modules.reports.models;

import java.util.List;

public class ReportedPlayer {

    private String user;
    private List<Integer> data;

    public ReportedPlayer(String user, List<Integer> data) {
        this.user = user;
        this.data = data;
    }

    public ReportedPlayer() {
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }
}
