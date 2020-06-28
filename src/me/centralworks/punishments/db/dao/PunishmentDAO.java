package me.centralworks.punishments.db.dao;

import com.google.common.collect.Lists;
import me.centralworks.punishments.Main;
import me.centralworks.punishments.db.ConnectionFactory;
import me.centralworks.punishments.punishs.OfflinePunishment;
import me.centralworks.punishments.punishs.OnlinePunishment;
import me.centralworks.punishments.punishs.Punishment;
import me.centralworks.punishments.punishs.PunishmentData;
import me.centralworks.punishments.punishs.supliers.enums.PunishmentState;
import me.centralworks.punishments.punishs.supliers.enums.PunishmentType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PunishmentDAO {

    protected static PunishmentDAO instance;
    private final Connection connection;

    protected PunishmentDAO(Connection connection) {
        this.connection = connection;
    }

    public static void newConnection() {
        if (PunishmentDAO.getInstance() != null) {
            try {
                if (!PunishmentDAO.getInstance().getConnection().isClosed())
                    PunishmentDAO.getInstance().getConnection().close();
            } catch (SQLException ignored) {
            }
        }
        instance = new PunishmentDAO(ConnectionFactory.make());
    }

    public static PunishmentDAO getInstance() {
        if (instance == null) {
            instance = new PunishmentDAO(ConnectionFactory.make());
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void createTable() {
        try {
            final PreparedStatement st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS arcanth_punishments(ID INT NOT NULL AUTO_INCREMENT, user VARCHAR(80), addressIP VARCHAR(20), type VARCHAR(25), reason TEXT, startedAt TIMESTAMP, finishAt TIMESTAMP, punisher VARCHAR(16), evidences TEXT, punishmentState VARCHAR(40), permanent BOOLEAN, PRIMARY KEY (ID));");
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Punishment loadByObject(Punishment punishment) {
        return loadAllByIdentifier(punishment.getIdentifier()).stream().filter(punishment1 ->
                punishment1.getData().getPunishmentType().equals(punishment.getData().getPunishmentType()) &&
                        punishment1.getData().getPunishmentState().equals(punishment.getData().getPunishmentState()) &&
                        punishment1.getData().getReasonString().equals(punishment.getData().getReasonString())
        ).findFirst().get();
    }

    public Punishment loadByIdentifier(String identifier) {
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM arcanth_punishments WHERE user = ?");
            st.setObject(1, identifier);
            final ResultSet rs = st.executeQuery();
            rs.next();
            final Punishment p;
            final String user = rs.getString("user");
            if (Main.isOnlineMode()) {
                final OnlinePunishment onlinePunishment = new OnlinePunishment();
                onlinePunishment.setIdentifier(user);
                p = onlinePunishment;
            } else {
                final OfflinePunishment offlinePunishment = new OfflinePunishment();
                offlinePunishment.setIdentifier(user);
                p = offlinePunishment;
            }
            final PunishmentData pd = new PunishmentData();
            p.setId(rs.getInt("id"));
            if (!rs.getString("addressIP").equals("null")) p.setIp(rs.getString("addressIP"));
            pd.setStartedAt(rs.getDate("startedAt").getTime());
            pd.setFinishAt(rs.getDate("finishAt").getTime());
            pd.setReason(rs.getString("reason"));
            pd.setPunisher(rs.getString("punisher"));
            pd.setPunishmentState(PunishmentState.getByIdentifier(rs.getString("punishmentState")));
            pd.setEvidences(rs.getString("evidences").equals("") ? Lists.newArrayList() : Lists.newArrayList(rs.getString("evidences").split(",")));
            pd.setPunishmentType(PunishmentType.getByIdentifier(rs.getString("type")));
            pd.setPermanent(rs.getBoolean("permanent"));
            p.setData(pd);
            return p;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Punishment> loadByIP(String address) {
        final List<Punishment> list = Lists.newArrayList();
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM arcanth_punishments WHERE addressIP = ?");
            st.setObject(1, address);
            final ResultSet rs = st.executeQuery();
            while (rs.next()) {
                final Punishment p;
                final String user = rs.getString("user");
                if (Main.isOnlineMode()) {
                    final OnlinePunishment onlinePunishment = new OnlinePunishment();
                    onlinePunishment.setIdentifier(user);
                    p = onlinePunishment;
                } else {
                    final OfflinePunishment offlinePunishment = new OfflinePunishment();
                    offlinePunishment.setIdentifier(user);
                    p = offlinePunishment;
                }
                final PunishmentData pd = new PunishmentData();
                p.setId(rs.getInt("id"));
                if (!rs.getString("addressIP").equals("null")) p.setIp(rs.getString("addressIP"));
                pd.setStartedAt(rs.getTimestamp("startedAt").getTime());
                pd.setFinishAt(rs.getTimestamp("finishAt").getTime());
                pd.setReason(rs.getString("reason"));
                pd.setPunisher(rs.getString("punisher"));
                pd.setPunishmentState(PunishmentState.getByIdentifier(rs.getString("punishmentState")));
                pd.setEvidences(rs.getString("evidences").equals("") ? Lists.newArrayList() : Lists.newArrayList(rs.getString("evidences").split(",")));
                pd.setPunishmentType(PunishmentType.getByIdentifier(rs.getString("type")));
                pd.setPermanent(rs.getBoolean("permanent"));
                p.setData(pd);
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<Punishment> loadAllByIdentifier(String identifier) {
        final List<Punishment> list = Lists.newArrayList();
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM arcanth_punishments WHERE user = ?");
            st.setObject(1, identifier);
            final ResultSet rs = st.executeQuery();
            while (rs.next()) {
                final Punishment p;
                final String user = rs.getString("user");
                if (Main.isOnlineMode()) {
                    final OnlinePunishment onlinePunishment = new OnlinePunishment();
                    onlinePunishment.setIdentifier(user);
                    p = onlinePunishment;
                } else {
                    final OfflinePunishment offlinePunishment = new OfflinePunishment();
                    offlinePunishment.setIdentifier(user);
                    p = offlinePunishment;
                }
                final PunishmentData pd = new PunishmentData();
                p.setId(rs.getInt("id"));
                if (!rs.getString("addressIP").equals("null")) p.setIp(rs.getString("addressIP"));
                pd.setStartedAt(rs.getTimestamp("startedAt").getTime());
                pd.setFinishAt(rs.getTimestamp("finishAt").getTime());
                pd.setReason(rs.getString("reason"));
                pd.setPunisher(rs.getString("punisher"));
                pd.setPunishmentState(PunishmentState.getByIdentifier(rs.getString("punishmentState")));
                pd.setEvidences(rs.getString("evidences").equals("") ? Lists.newArrayList() : Lists.newArrayList(rs.getString("evidences").split(",")));
                pd.setPunishmentType(PunishmentType.getByIdentifier(rs.getString("type")));
                pd.setPermanent(rs.getBoolean("permanent"));
                p.setData(pd);
                list.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public Punishment loadByID(Integer id) {
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM arcanth_punishments WHERE id = ?");
            st.setInt(1, id);
            final ResultSet rs = st.executeQuery();
            rs.next();
            final Punishment p;
            final String user = rs.getString("user");
            if (Main.isOnlineMode()) {
                final OnlinePunishment onlinePunishment = new OnlinePunishment();
                onlinePunishment.setIdentifier(user);
                p = onlinePunishment;
            } else {
                final OfflinePunishment offlinePunishment = new OfflinePunishment();
                offlinePunishment.setIdentifier(user);
                p = offlinePunishment;
            }
            final PunishmentData pd = new PunishmentData();
            p.setId(rs.getInt("id"));
            if (!rs.getString("addressIP").equals("null")) p.setIp(rs.getString("addressIP"));
            pd.setStartedAt(rs.getTimestamp("startedAt").getTime());
            pd.setFinishAt(rs.getTimestamp("finishAt").getTime());
            pd.setReason(rs.getString("reason"));
            pd.setPunisher(rs.getString("punisher"));
            pd.setPunishmentState(PunishmentState.getByIdentifier(rs.getString("punishmentState")));
            pd.setEvidences(rs.getString("evidences").equals("") ? Lists.newArrayList() : Lists.newArrayList(rs.getString("evidences").split(",")));
            pd.setPunishmentType(PunishmentType.getByIdentifier(rs.getString("type")));
            pd.setPermanent(rs.getBoolean("permanent"));
            p.setData(pd);
            return p;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save(Punishment p) {
        try {
            final PunishmentData pd = p.getData();
            final PreparedStatement st;
            final StringBuilder stringBuilder = new StringBuilder();
            pd.getEvidences().forEach(s -> stringBuilder.append(stringBuilder.toString().equals("") ? s : "," + s));
            if (!p.idIsValid()) {
                st = connection.prepareStatement("INSERT INTO arcanth_punishments VALUES(DEFAULT, ?,?,?,?,?,?,?,?,?,?)");
                st.setString(1, p.getIdentifier());
                st.setString(2, !p.ipIsValid() ? "null" : p.getIp());
                st.setString(3, pd.getPunishmentType().getIdentifier());
                st.setString(4, pd.getReason().getReason());
                st.setTimestamp(5, pd.getStartDateSQL());
                st.setTimestamp(6, pd.getFinishDateSQL());
                st.setString(7, pd.getPunisher());
                st.setString(8, stringBuilder.toString());
                st.setString(9, pd.getPunishmentState().getIdentifier());
                st.setBoolean(10, pd.isPermanent());
            } else {
                st = connection.prepareStatement("INSERT INTO arcanth_punishments VALUES(?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE ID = ?, User = ?, addressIP = ?, type = ?, reason = ?, startedAt = ?, finishAt = ?, punisher = ?, evidences = ?, punishmentState = ?, permanent = ?");
                st.setInt(1, p.getId());
                st.setString(2, p.getIdentifier());
                st.setString(3, !p.ipIsValid() ? "null" : p.getIp());
                st.setString(4, pd.getPunishmentType().getIdentifier());
                st.setString(5, pd.getReason().getReason());
                st.setTimestamp(6, pd.getStartDateSQL());
                st.setTimestamp(7, pd.getFinishDateSQL());
                st.setString(8, pd.getPunisher());
                st.setString(9, stringBuilder.toString());
                st.setString(10, pd.getPunishmentState().getIdentifier());
                st.setBoolean(11, pd.isPermanent());
                st.setInt(12, p.getId());
                st.setString(13, p.getIdentifier());
                st.setString(14, !p.ipIsValid() ? "null" : p.getIp());
                st.setString(15, pd.getPunishmentType().getIdentifier());
                st.setString(16, pd.getReason().getReason());
                st.setTimestamp(17, pd.getStartDateSQL());
                st.setTimestamp(18, pd.getFinishDateSQL());
                st.setString(19, pd.getPunisher());
                st.setString(20, stringBuilder.toString());
                st.setString(21, pd.getPunishmentState().getIdentifier());
                st.setBoolean(22, pd.isPermanent());
            }
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Object identifier, String table) {
        try {
            final PreparedStatement st = connection.prepareStatement("DELETE FROM arcanth_punishments WHERE " + table + " = ?");
            st.setObject(1, identifier);
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void delete(Punishment punishment) {
        delete(punishment.getId(), "id");
    }

    public boolean existsIdentifier(String identifier) {
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT user FROM arcanth_punishments WHERE user = ?");
            st.setString(1, identifier);
            return st.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existsId(Integer id) {
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT id FROM arcanth_punishments WHERE id = ?");
            st.setInt(1, id);
            return st.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean existsIp(String ip) {
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT addressIP FROM arcanth_punishments WHERE addressIP = ?");
            st.setString(1, ip);
            return st.executeQuery().next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

}
