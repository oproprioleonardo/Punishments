package com.leonardo.bungee.modules.reports.models.supliers;

import com.leonardo.bungee.modules.reports.dao.ReportDAO;
import com.leonardo.bungee.modules.reports.models.ReportedPlayer;

public class Request {

    private ReportedPlayer rp;

    public Request(ReportedPlayer rp) {
        this.rp = rp;
    }

    public void set(ReportedPlayer rp) {
        this.rp = rp;
    }

    public ReportedPlayer get() {
        return rp;
    }

    public boolean exists() {
        return ReportDAO.getInstance().exists(rp.getUser());
    }

    public ReportedPlayer request() {
        set(ReportDAO.getInstance().loadByUser(rp.getUser()));
        return get();
    }

    public void delete() {
        ReportDAO.getInstance().delete(rp.getUser());
    }

    public void save() {
        ReportDAO.getInstance().save(get());
    }


}
