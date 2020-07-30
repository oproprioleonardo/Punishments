package me.centralworks.bungee.modules.reports.models;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.centralworks.bungee.modules.reports.dao.ReportDAO;

import java.util.ArrayList;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ReportedPlayer {

    private String user;
    private List<Report> data = Lists.newArrayList();

    public ReportedPlayer(String user) {
        this.user = user;
    }

    public void attachReport(Report report) {
        final ArrayList<Report> reports = Lists.newArrayList(getData());
        reports.add(report);
        setData(reports);
    }

    public Report getById(int id) {
        return getData().stream().filter(report -> report.getId() == id).findFirst().get();
    }

    public void removeReport(Report report) {
        final ArrayList<Report> reports = Lists.newArrayList(getData());
        reports.remove(report);
        setData(reports);
    }

    public void delete() {
        ReportDAO.getInstance().delete(getUser());
    }

    public boolean exists() {
        return ReportDAO.getInstance().exists(getUser());
    }
}
