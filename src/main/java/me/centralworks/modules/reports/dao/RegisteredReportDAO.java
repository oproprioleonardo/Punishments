package me.centralworks.modules.reports.dao;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.centralworks.database.ConnectionFactory;
import me.centralworks.modules.reports.models.ReportedPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class RegisteredReportDAO {

    protected static RegisteredReportDAO instance;
    private final Connection connection;

    protected RegisteredReportDAO(Connection connection) {
        this.connection = connection;
    }

    public static void newConnection() {
        if (RegisteredReportDAO.getInstance() != null) {
            try {
                if (!RegisteredReportDAO.getInstance().getConnection().isClosed())
                    RegisteredReportDAO.getInstance().getConnection().close();
            } catch (SQLException ignored) {
            }
        }
        instance = new RegisteredReportDAO(ConnectionFactory.make());
    }

    public static RegisteredReportDAO getInstance() {
        if (instance == null) instance = new RegisteredReportDAO(ConnectionFactory.make());
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void createTable() {
        try {
            final PreparedStatement st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS arcanth_reports_users(user VARCHAR(16) PRIMARY KEY, data TEXT)");
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean exists(String user) {
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM arcanth_reports_users WHERE user = ?");
            st.setString(1, user);
            final ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void save(ReportedPlayer reportedPlayer) {
        try {
            final PreparedStatement st = connection.prepareStatement("INSERT INTO arcanth_reports_users VALUES(?,?) ON DUPLICATE KEY UPDATE user = ?, data = ?");
            st.setString(1, reportedPlayer.getUser());
            st.setString(2, reportedPlayer.getData().toString());
            st.setString(3, reportedPlayer.getUser());
            st.setString(4, reportedPlayer.getData().toString());
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ReportedPlayer loadByUser(String user) {
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM arcanth_reports_users WHERE user = ?");
            st.setString(1, user);
            final ResultSet rs = st.executeQuery();
            rs.next();
            final ReportedPlayer rp = new ReportedPlayer();
            final String data = rs.getString("data");
            final JsonArray jsonArray = new JsonParser().parse(data).getAsJsonArray();
            final List<Integer> list = Lists.newArrayList();
            jsonArray.forEach(jsonElement -> list.add(jsonElement.getAsInt()));
            rp.setUser(user);
            rp.setData(list);
            return rp;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new ReportedPlayer();
    }

    public void delete(String user) {
        try {
            final PreparedStatement st = connection.prepareStatement("DELETE FROM arcanth_reports_users WHERE user = ?");
            st.setString(1, user);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
