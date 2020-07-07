package me.centralworks.modules.punishments.models.warns;

import com.google.common.collect.Lists;
import me.centralworks.Main;
import me.centralworks.lib.General;
import me.centralworks.modules.punishments.db.dao.WarnDAO;
import net.md_5.bungee.BungeeCord;

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
        BungeeCord.getInstance().getScheduler().runAsync(Main.getInstance(), () -> {
            final Warn warn = warns.get(0);
            General.getGeneralLib().getFunctionAnnouncerWarn().accept(warn);
            General.getGeneralLib().getFunctionWarnIfOn().accept(warn);
            WarnDAO.getInstance().save(warn);
            final List<Warn> w = Lists.newArrayList(warns);
            w.remove(warn);
            setWarns(w);
            purify(warn.getTarget());
            goSave();
        });
    }

    private void goSaveNow(Warn warn) {
        WarnDAO.getInstance().save(warn);
        final List<Warn> w = Lists.newArrayList(warns);
        w.remove(warn);
        setWarns(w);
        purify(warn.getTarget());
    }

    private void purify(String target) {
        final WarnDAO wd = WarnDAO.getInstance();
        final List<Warn> wl = General.getGeneralLib().updateAllWarns(wd.loadAllWarns(target));
        final List<WarnPunishment> wps = Main.getWps();
        wps.sort(Comparator.comparingInt(WarnPunishment::getAmount));
        Collections.reverse(wps);
        for (WarnPunishment wp : Main.getWps()) {
            if (wl.size() == wp.getAmount()) {
                BungeeCord.getInstance().getPluginManager().dispatchCommand(BungeeCord.getInstance().getConsole(), wp.getCommand().replace("{jogador}", target));
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
