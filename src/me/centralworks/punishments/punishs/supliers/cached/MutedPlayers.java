package me.centralworks.punishments.punishs.supliers.cached;

import com.google.common.collect.Lists;

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

    public static class MuteObject {
        private String playerMuted;
        private Integer id;
        private Long finishAt;
        private Long startAt;

        public MuteObject(String playerMuted, Integer id, Long finishAt, Long startAt) {
            this.playerMuted = playerMuted;
            this.id = id;
            this.finishAt = finishAt;
            this.startAt = startAt;
        }

        public MuteObject() {
        }

        public void save(){
            MutedPlayers.getInstance().add(this);
        }

        public void erase(){
            MutedPlayers.getInstance().remove(this);
        }

        public String getPlayerMuted() {
            return playerMuted;
        }

        public void setPlayerMuted(String playerMuted) {
            this.playerMuted = playerMuted;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public Long getFinishAt() {
            return finishAt;
        }

        public void setFinishAt(Long finishAt) {
            this.finishAt = finishAt;
        }

        public Long getStartAt() {
            return startAt;
        }

        public void setStartAt(Long startAt) {
            this.startAt = startAt;
        }
    }

}
