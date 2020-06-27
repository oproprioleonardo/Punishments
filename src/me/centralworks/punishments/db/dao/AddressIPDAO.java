package me.centralworks.punishments.db.dao;

import me.centralworks.punishments.db.ConnectionFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class AddressIPDAO {

    protected static AddressIPDAO instance;
    private final Connection connection;

    protected AddressIPDAO(Connection connection) {
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

}
