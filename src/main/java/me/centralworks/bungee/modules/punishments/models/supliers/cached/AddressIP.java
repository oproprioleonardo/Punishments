package me.centralworks.bungee.modules.punishments.models.supliers.cached;

import com.google.common.collect.Lists;
import me.centralworks.bungee.Main;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class AddressIP {

    protected static AddressIP instance;
    public List<AddressIPObject> list = Lists.newArrayList();

    public static AddressIP getInstance() {
        if (instance == null) instance = new AddressIP();
        return instance;
    }

    public String getHostAddress(String identifier) {
        return getByAccount(identifier).getHostName();
    }

    public List<AddressIPObject> getList() {
        return list;
    }

    public void setList(List<AddressIPObject> list) {
        this.list = list;
    }

    public void add(AddressIPObject addressIPObject) {
        final ArrayList<AddressIPObject> list = Lists.newArrayList(getList());
        list.add(addressIPObject);
        setList(list);
    }

    public void remove(AddressIPObject addressIPObject) {
        final ArrayList<AddressIPObject> list = Lists.newArrayList(getList());
        list.remove(addressIPObject);
        setList(list);
    }

    public boolean existsIP(String hostName) {
        return getList().stream().anyMatch(addressIPObject -> addressIPObject.getHostName().equals(hostName));
    }

    public boolean existsIPAndAccount(String host, String account) {
        return getList().stream().anyMatch(addressIPObject -> addressIPObject.getHostName().equals(host) && addressIPObject.exists(account));
    }

    public boolean existsPlayer(String account) {
        return getList().stream().anyMatch(addressIPObject -> addressIPObject.exists(account));
    }

    public AddressIPObject getByAddress(String hostName) {
        return getList().stream().filter(addressIPObject -> addressIPObject.getHostName().equals(hostName)).findFirst().get();
    }

    public AddressIPObject getByAddressAndAccount(String hostName, String account) {
        return getList().stream().filter(addressIPObject -> addressIPObject.exists(account) && addressIPObject.getHostName().equals(hostName)).findFirst().get();
    }

    public AddressIPObject getByAccount(String account) {
        return getList().stream().filter(addressIPObject -> addressIPObject.exists(account)).findFirst().get();
    }

    public static class AddressIPObject {
        private String hostName;
        private List<String> accounts = Lists.newArrayList();
        private Long lastUsage = System.currentTimeMillis();

        public AddressIPObject(String hostName, List<String> accounts, Long lastUsage) {
            this.hostName = hostName;
            this.accounts = accounts;
            this.lastUsage = lastUsage;
        }

        public AddressIPObject() {
        }

        public Long getLastUsage() {
            return lastUsage;
        }

        public void setLastUsage(Long lastUsage) {
            this.lastUsage = lastUsage;
        }

        public String getHostName() {
            return hostName;
        }

        public void setHostName(String hostName) {
            this.hostName = hostName;
        }

        public List<String> getAccounts() {
            return accounts;
        }

        public void setAccounts(List<String> accounts) {
            this.accounts = accounts;
        }

        public void add(String account) {
            final ArrayList<String> strings = Lists.newArrayList(getAccounts());
            strings.add(account);
            setAccounts(strings);
        }

        public void update() {
            setLastUsage(System.currentTimeMillis());
        }

        public Timestamp getLastUsageTime() {
            return new Timestamp(getLastUsage());
        }

        public boolean isOnline() {
            return accounts.stream().anyMatch(s -> Main.getInstance().getProxy().getPlayer(s) != null)
                    || Main.getInstance().getProxy().getPlayers().stream().anyMatch(p -> p.getAddress().getAddress().getHostAddress().equalsIgnoreCase(hostName));
        }

        public void remove(String account) {
            final ArrayList<String> strings = Lists.newArrayList(getAccounts());
            strings.remove(account);
            setAccounts(strings);
            if (getAccounts().size() == 0) AddressIP.getInstance().remove(this);
        }

        public boolean exists(String account) {
            return getAccounts().stream().anyMatch(s -> s.equalsIgnoreCase(account));
        }
    }
}
