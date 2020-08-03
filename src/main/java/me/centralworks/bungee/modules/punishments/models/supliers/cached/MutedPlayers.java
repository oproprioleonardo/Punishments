package me.centralworks.bungee.modules.punishments.models.supliers.cached;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import me.centralworks.bungee.lib.Functionalities;
import me.centralworks.bungee.modules.punishments.models.Punishment;
import me.centralworks.bungee.modules.punishments.models.supliers.Request;
import me.centralworks.bungee.modules.punishments.models.supliers.enums.PunishmentState;

import java.util.List;

public class MutedPlayers {

    protected static MutedPlayers instance;
    public List<MuteObject> list = Lists.newArrayList();

    public static MutedPlayers getInstance() {
        if (instance == null) instance = new MutedPlayers();
        return instance;
    }

    public List<MuteObject> getList() {
        return list;
    }

    public void setList(List<MuteObject> list) {
        this.list = list;
    }

    protected List<MuteObject> copy() {
        return Lists.newArrayList(getList());
    }

    public void add(MuteObject mo) {
        final List<MuteObject> copy = copy();
        copy.add(mo);
        setList(copy);
    }

    public void remove(MuteObject mo) {
        final List<MuteObject> copy = copy();
        copy.remove(mo);
        setList(copy);
    }

    public void remove(String playerMuted) {
        final List<MuteObject> copy = copy();
        copy.removeIf(muteObject -> muteObject.getPlayerMuted().equals(playerMuted));
        setList(copy);
    }

    public MuteObject get(String playerMuted) {
        return getList().stream().filter(muteObject -> muteObject.getPlayerMuted().equalsIgnoreCase(playerMuted)).findFirst().get();
    }

    public boolean exists(String playerMuted) {
        return getList().stream().anyMatch(muteObject -> muteObject.getPlayerMuted().equalsIgnoreCase(playerMuted));
    }

    public boolean existsByAddress(String address) {
        return getList().stream().anyMatch(muteObject -> muteObject.getAddressIP().equalsIgnoreCase(address));
    }

    public MuteObject getByAddress(String address) {
        return getList().stream().filter(muteObject -> muteObject.getAddressIP().equalsIgnoreCase(address)).findFirst().get();
    }

    public void update(String playerMuted) {
        if (exists(playerMuted)) {
            final MuteObject muteObject = get(playerMuted);
            if (!muteObject.isPermanent() && muteObject.getFinishAt() < System.currentTimeMillis()) {
                final Punishment o = Functionalities.get().easyInstance(playerMuted, playerMuted);
                o.setId(muteObject.getId());
                final Request request = new Request(o);
                final Punishment punishment = request.requireById();
                punishment.getData().setPunishmentState(PunishmentState.FINISHED);
                request.save();
                this.remove(muteObject);
            }
        }
    }

    public void updateByAddress(String address) {
        if (existsByAddress(address)) {
            final MuteObject muteObject = getByAddress(address);
            if (!muteObject.isPermanent() && muteObject.getFinishAt() < System.currentTimeMillis()) {
                final Punishment o = Functionalities.get().easyInstance(muteObject.getPlayerMuted(), muteObject.getPlayerMuted());
                o.setId(muteObject.getId());
                final Request request = new Request(o);
                final Punishment punishment = request.requireById();
                punishment.getData().setPunishmentState(PunishmentState.FINISHED);
                request.save();
                this.remove(muteObject);
            }
        }
    }

    @Data
    @RequiredArgsConstructor
    public static class MuteObject {
        private String playerMuted;
        private Integer id;
        private Long finishAt;
        private Long startAt;
        private boolean permanent = false;
        private boolean ip = false;
        private String addressIP;

        public MuteObject(String playerMuted, Integer id, Long startAt, Long finishAt, boolean permanent, String addressIP) {
            this.playerMuted = playerMuted;
            this.id = id;
            this.finishAt = finishAt;
            this.startAt = startAt;
            this.permanent = permanent;
            this.addressIP = addressIP;
            if (!addressIP.equalsIgnoreCase("")) setIp(true);
        }

        public void save() {
            MutedPlayers.getInstance().add(this);
        }

        public void erase() {
            MutedPlayers.getInstance().remove(this);
        }
    }

}
