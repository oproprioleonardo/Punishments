package me.centralworks.modules.reports.dao;

import me.centralworks.database.ConnectionFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ReportTargetDAO {

    protected static ReportTargetDAO instance;
    private final Connection connection;

    protected ReportTargetDAO(Connection connection) {
        this.connection = connection;
    }

    public static ReportTargetDAO getInstance() {
        if (instance == null) instance = new ReportTargetDAO(ConnectionFactory.make());
        return instance;
    }

    public static void newConnection() {
        if (ReportTargetDAO.getInstance() != null) {
            try {
                if (!ReportTargetDAO.getInstance().getConnection().isClosed())
                    ReportTargetDAO.getInstance().getConnection().close();
            } catch (SQLException ignored) {
            }
        }
        instance = new ReportTargetDAO(ConnectionFactory.make());
    }

    public Connection getConnection() {
        return connection;
    }

    public void createTable() {
        try {
            final PreparedStatement st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS arcanth_reports_victims(ID INT NOT NULL AUTO_INCREMENT, PRIMARY KEY(ID))");
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}