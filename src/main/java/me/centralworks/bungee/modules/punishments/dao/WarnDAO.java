package me.centralworks.bungee.modules.punishments.dao;

import com.google.common.collect.Lists;
import me.centralworks.bungee.database.ConnectionFactory;
import me.centralworks.bungee.modules.punishments.models.Warn;

import java.sql.*;
import java.util.List;

public class WarnDAO {

    protected static WarnDAO instance;
    private final Connection connection;

    protected WarnDAO(Connection connection) {
        this.connection = connection;
    }

    public static void newConnection() {
        if (WarnDAO.getInstance() != null) {
            try {
                if (!WarnDAO.getInstance().getConnection().isClosed())
                    WarnDAO.getInstance().getConnection().close();
            } catch (SQLException ignored) {
            }
        }
        instance = new WarnDAO(ConnectionFactory.make());
    }

    public static WarnDAO getInstance() {
        if (instance == null) {
            instance = new WarnDAO(ConnectionFactory.make());
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void createTable() {
        try {
            final PreparedStatement st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS arcanth_warns(ID INT NOT NULL AUTO_INCREMENT, user VARCHAR(16), punisher VARCHAR(16), reason TEXT, startedAt TIMESTAMP, finishAt TIMESTAMP, permanent BOOLEAN, PRIMARY KEY (ID))");
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existsID(Integer id) {
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT ID FROM arcanth_warns WHERE ID = ?");
            st.setInt(1, id);
            return st.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean existsNickname(String nickname) {
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT user FROM arcanth_warns WHERE user = ?");
            st.setString(1, nickname);
            return st.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public Warn loadByID(Integer id) {
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM arcanth_warns WHERE ID = ?");
            st.setInt(1, id);
            final ResultSet rs = st.executeQuery();
            rs.next();
            final Warn warn = new Warn();
            warn.setId(id);
            warn.setTarget(rs.getString("user"));
            warn.setPunisher(rs.getString("punisher"));
            warn.setReason(rs.getString("reason"));
            warn.setPermanent(rs.getBoolean("permanent"));
            warn.setStartedAt(rs.getTimestamp("startedAt").getTime());
            warn.setFinishAt(rs.getTimestamp("finishAt").getTime());
            return warn;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Warn> loadAllWarns(String target) {
        final List<Warn> list = Lists.newArrayList();
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM arcanth_warns WHERE user = ?");
            st.setString(1, target);
            final ResultSet rs = st.executeQuery();
            while (rs.next()) {
                final Warn warn = new Warn();
                warn.setId(rs.getInt("id"));
                warn.setTarget(target);
                warn.setPunisher(rs.getString("punisher"));
                warn.setReason(rs.getString("reason"));
                warn.setPermanent(rs.getBoolean("permanent"));
                warn.setStartedAt(rs.getTimestamp("startedAt").getTime());
                warn.setFinishAt(rs.getTimestamp("finishAt").getTime());
                list.add(warn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void save(Warn warn) {
        try {
            PreparedStatement st;
            if (!warn.idIsValid()) {
                st = connection.prepareStatement("INSERT INTO arcanth_warns VALUES(DEFAULT,?,?,?,?,?,?)");
                st.setString(1, warn.getTarget());
                st.setString(2, warn.getPunisher());
                st.setString(3, warn.getReason());
                st.setTimestamp(4, new Timestamp(warn.getStartedAt()));
                st.setTimestamp(5, new Timestamp(warn.getFinishAt()));
                st.setBoolean(6, warn.isPermanent());
            } else {
                st = connection.prepareStatement("INSERT INTO arcanth_warns VALUES(?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE ID = ?, user = ?, punisher = ?, reason = ?, startedAt = ?, finishAt = ?, permanent = ?");
                st.setInt(1, warn.getId());
                st.setString(2, warn.getTarget());
                st.setString(3, warn.getPunisher());
                st.setString(4, warn.getReason());
                st.setTimestamp(5, new Timestamp(warn.getStartedAt()));
                st.setTimestamp(6, new Timestamp(warn.getFinishAt()));
                st.setBoolean(7, warn.isPermanent());
                st.setInt(8, warn.getId());
                st.setString(9, warn.getTarget());
                st.setString(10, warn.getPunisher());
                st.setString(11, warn.getReason());
                st.setTimestamp(12, new Timestamp(warn.getStartedAt()));
                st.setTimestamp(13, new Timestamp(warn.getFinishAt()));
                st.setBoolean(14, warn.isPermanent());
            }
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteAllByNickname(String target) {
        try {
            final PreparedStatement st = connection.prepareStatement("DELETE FROM arcanth_warns WHERE target = ?");
            st.setString(1, target);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteById(Integer id) {
        try {
            final PreparedStatement st = connection.prepareStatement("DELETE FROM arcanth_warns WHERE id = ?");
            st.setInt(1, id);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
