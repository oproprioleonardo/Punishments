package com.leonardo.bungee.modules.punishments.dao;

import com.google.common.collect.Lists;
import com.leonardo.bungee.Main;
import com.leonardo.bungee.database.ConnectionFactory;
import com.leonardo.bungee.lib.Date;
import com.leonardo.bungee.modules.punishments.models.supliers.cached.AddressIP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AddressIPDAO {

    private static AddressIPDAO instance;
    private final Connection connection;

    private AddressIPDAO(Connection connection) {
        this.connection = connection;
    }

    public static void newConnection() {
        if (AddressIPDAO.getInstance() != null) {
            try {
                if (!AddressIPDAO.getInstance().getConnection().isClosed())
                    AddressIPDAO.getInstance().getConnection().close();
            } catch (SQLException ignored) {
            }
        }
        instance = new AddressIPDAO(ConnectionFactory.make());
    }

    public static AddressIPDAO getInstance() {
        if (instance == null) {
            instance = new AddressIPDAO(ConnectionFactory.make());
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void createTable() {
        try {
            final PreparedStatement st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS arcanth_addressip(addressIP VARCHAR(25) PRIMARY KEY, accounts TEXT, lastUsage TIMESTAMP)");
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(AddressIP.AddressIPObject addressIPObject) {
        try {
            final PreparedStatement st = connection.prepareStatement("INSERT INTO arcanth_addressip VALUES(?,?,?) ON DUPLICATE KEY UPDATE addressIP = ?, accounts = ?, lastUsage = ?");
            st.setString(1, addressIPObject.getHostName());
            st.setString(2, String.join(",", addressIPObject.getAccounts()));
            st.setTimestamp(3, addressIPObject.getLastUsageTime());
            st.setString(4, addressIPObject.getHostName());
            st.setString(5, String.join(",", addressIPObject.getAccounts()));
            st.setTimestamp(6, addressIPObject.getLastUsageTime());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveAll() {
        try {
            for (AddressIP.AddressIPObject addressIPObject : AddressIP.getInstance().getList()) {
                if (addressIPObject.getLastUsage() + 7 * Date.getInstance().getDays() > System.currentTimeMillis()) {
                    final PreparedStatement st = connection.prepareStatement("INSERT INTO arcanth_addressip VALUES(?,?,?) ON DUPLICATE KEY UPDATE addressIP = ?, accounts = ?, lastUsage = ?");
                    st.setString(1, addressIPObject.getHostName());
                    st.setString(2, String.join(",", addressIPObject.getAccounts()));
                    st.setTimestamp(3, addressIPObject.getLastUsageTime());
                    st.setString(4, addressIPObject.getHostName());
                    st.setString(5, String.join(",", addressIPObject.getAccounts()));
                    st.setTimestamp(6, addressIPObject.getLastUsageTime());
                    st.executeUpdate();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void loadAll() {
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM arcanth_addressip");
            final ResultSet rs = st.executeQuery();
            final AddressIP addressIP = AddressIP.getInstance();
            addressIP.setList(Lists.newArrayList());
            while (rs.next()) {
                final AddressIP.AddressIPObject addressIPObject = new AddressIP.AddressIPObject(
                        rs.getString("addressIP"),
                        Lists.newArrayList(rs.getString("accounts").split(",")),
                        rs.getTimestamp("lastUsage").getTime());
                if (addressIPObject.getLastUsage() + 7 * Date.getInstance().getDays() > System.currentTimeMillis()) {
                    addressIP.add(addressIPObject);
                } else {
                    Main.getInstance().getProxy().getScheduler().runAsync(Main.getInstance(), () -> {
                        try {
                            delete(addressIPObject.getHostName());
                        } catch (Exception ignored) {
                        }
                    });
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(String hostname) {
        try {
            final PreparedStatement st = connection.prepareStatement("DELETE FROM arcanth_addressip WHERE addressIP = ?");
            st.setString(1, hostname);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
