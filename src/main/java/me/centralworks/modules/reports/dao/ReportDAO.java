package me.centralworks.modules.reports.dao;

import com.google.common.collect.Lists;
import com.google.gson.reflect.TypeToken;
import me.centralworks.database.ConnectionFactory;
import me.centralworks.modules.reports.ReportPlugin;
import me.centralworks.modules.reports.models.Report;
import me.centralworks.modules.reports.models.ReportedPlayer;

import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class ReportDAO {

    protected static ReportDAO instance;
    private final Connection connection;

    protected ReportDAO(Connection connection) {
        this.connection = connection;
    }

    public static void newConnection() {
        if (ReportDAO.getInstance() != null) {
            try {
                if (!ReportDAO.getInstance().getConnection().isClosed())
                    ReportDAO.getInstance().getConnection().close();
            } catch (SQLException ignored) {
            }
        }
        instance = new ReportDAO(ConnectionFactory.make());
    }

    public static ReportDAO getInstance() {
        if (instance == null) instance = new ReportDAO(ConnectionFactory.make());
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void createTable() {
        try {
            final PreparedStatement st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS arcanth_reports(user VARCHAR(16) PRIMARY KEY, data LONGTEXT)");
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ReportedPlayer> get() {
        final List<ReportedPlayer> list = Lists.newArrayList();
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM arcanth_reports LIMIT 0,100");
            final ResultSet rs = st.executeQuery();
            final Type type = new TypeToken<List<Report>>() {
            }.getType();
            while (rs.next()) {
                final ReportedPlayer rp = new ReportedPlayer();
                final String data = rs.getString("data");
                final List<Report> listr = ReportPlugin.getGson().fromJson(data, type);
                rp.setUser(rs.getString("user"));
                rp.setData(listr);
                list.add(rp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public boolean exists(String user) {
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM arcanth_reports WHERE user = ?");
            st.setString(1, user);
            final ResultSet rs = st.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean exists(String user, String victim) {
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM arcanth_reports WHERE user = ?");
            st.setString(1, user);
            final ResultSet rs = st.executeQuery();
            if (rs.next()) {
                final ReportedPlayer rp = loadByUser(user);
                return rp.getData().stream().anyMatch(report -> report.getVictim().equalsIgnoreCase(victim));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void save(ReportedPlayer reportedPlayer) {
        try {
            final PreparedStatement st = connection.prepareStatement("INSERT INTO arcanth_reports VALUES(?,?) ON DUPLICATE KEY UPDATE user = ?, data = ?");
            final Type type = new TypeToken<List<Report>>() {
            }.getType();
            final String json = ReportPlugin.getGson().toJson(reportedPlayer.getData(), type);
            st.setString(1, reportedPlayer.getUser());
            st.setString(2, json);
            st.setString(3, reportedPlayer.getUser());
            st.setString(4, json);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ReportedPlayer loadByUser(String user) {
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM arcanth_reports WHERE user = ?");
            st.setString(1, user);
            final ResultSet rs = st.executeQuery();
            rs.next();
            final Type type = new TypeToken<List<Report>>() {
            }.getType();
            final ReportedPlayer rp = new ReportedPlayer();
            final String data = rs.getString("data");
            final List<Report> list = ReportPlugin.getGson().fromJson(data, type);
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
            final PreparedStatement st = connection.prepareStatement("DELETE FROM arcanth_reports WHERE user = ?");
            st.setString(1, user);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        try {
            connection.prepareStatement("DELETE FROM arcanth_reports").executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
