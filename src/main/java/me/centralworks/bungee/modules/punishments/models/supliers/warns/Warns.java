package me.centralworks.bungee.modules.punishments.models.supliers.warns;

import com.google.common.collect.Lists;
import me.centralworks.bungee.Main;
import me.centralworks.bungee.lib.General;
import me.centralworks.bungee.modules.punishments.dao.WarnDAO;
import me.centralworks.bungee.modules.punishments.models.Warn;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Warns {

    private static Warns instance;
    public List<Warn> warns = Lists.newArrayList();
    private boolean task = false;

    public static Warns getInstance() {
        if (instance == null) instance = new Warns();
        return instance;
    }

    public void add(Warn warn) {
        warns.add(warn);
        if (!canAdd()) goSaveNow(warn);
        else if (!task) goSave();
    }

    public boolean canAdd() {
        return warns.size() < 15;
    }

    private void goSave() {
        if (warns.size() == 0) {
            task = false;
            return;
        }
        task = true;
        Main.getInstance().getProxy().getScheduler().runAsync(Main.getInstance(), () -> {
            final Warn warn = warns.get(0);
            General.getGeneralLib().getFunctionAnnouncerWarn().accept(warn);
            General.getGeneralLib().getFunctionWarnIfOn().accept(warn);
            warn.saveSync();
            final List<Warn> w = Lists.newArrayList(warns);
            w.remove(warn);
            setWarns(w);
            purify(warn.getTarget());
            goSave();
        });
    }

    private void goSaveNow(Warn warn) {
        warn.saveSync();
        final List<Warn> w = Lists.newArrayList(warns);
        w.remove(warn);
        setWarns(w);
        purify(warn.getTarget());
    }

    private void purify(String target) {
        final WarnDAO wd = WarnDAO.getInstance();
        final List<Warn> wl = General.getGeneralLib().updateAllWarns(wd.loadAllWarns(target));
        final List<WarnPunishment> wps = WarnLoader.getWps();
        wps.sort(Comparator.comparingInt(WarnPunishment::getAmount));
        Collections.reverse(wps);
        for (WarnPunishment wp : WarnLoader.getWps()) {
            if (wl.size() == wp.getAmount()) {
                Main.getInstance().getProxy().getPluginManager().dispatchCommand(Main.getInstance().getProxy().getConsole(), wp.getCommand().replace("{jogador}", target));
                break;
            }
        }
    }

    public void setWarns(List<Warn> warns) {
        this.warns = warns;
    }

    public boolean exists(String target) {
        return warns.stream().anyMatch(warn -> warn.getTarget().equalsIgnoreCase(target));
    }

    public Warn get(String target) {
        return warns.stream().filter(warn -> warn.getTarget().equalsIgnoreCase(target)).findFirst().get();
    }

}
