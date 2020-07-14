package me.centralworks.modules.punishments.dao;

import com.google.common.collect.Lists;
import me.centralworks.Main;
import me.centralworks.database.ConnectionFactory;
import me.centralworks.modules.punishments.models.punishs.OfflinePunishment;
import me.centralworks.modules.punishments.models.punishs.OnlinePunishment;
import me.centralworks.modules.punishments.models.punishs.Punishment;
import me.centralworks.modules.punishments.models.punishs.supliers.Elements;
import me.centralworks.modules.punishments.models.punishs.supliers.Request;
import me.centralworks.modules.punishments.models.punishs.supliers.enums.PunishmentState;
import me.centralworks.modules.punishments.models.punishs.supliers.enums.PunishmentType;

import java.sql.*;
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
            final PreparedStatement st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS arcanth_punishments(ID INT NOT NULL AUTO_INCREMENT, user VARCHAR(80), addressIP VARCHAR(20), type VARCHAR(25), reason TEXT, startedAt TIMESTAMP, finishAt TIMESTAMP, punisher VARCHAR(16), evidences TEXT, punishmentState VARCHAR(40), permanent BOOLEAN, secondaryIdentifier VARCHAR(16), PRIMARY KEY (ID));");
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean existsPrimaryIdentifier(String identifier) {
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

    public boolean existsSecondaryIdentifier(String identifier) {
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT secondaryIdentifier FROM arcanth_punishments WHERE secondaryIdentifier = ?");
            st.setString(1, identifier);
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

    public Request loadByObject(Punishment punishment) {
        return loadAllByPrimaryIdentifier(punishment.getPrimaryIdentifier()).stream().filter(p1 -> p1.get().equals(punishment)).findFirst().get();
    }

    public Request loadByID(Integer id) {
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM arcanth_punishments WHERE id = ?");
            st.setInt(1, id);
            final ResultSet rs = st.executeQuery();
            rs.next();
            final Punishment p;
            final String user = rs.getString("user");
            if (Main.isOnlineMode()) {
                final OnlinePunishment onlinePunishment = new OnlinePunishment();
                onlinePunishment.setPrimaryIdentifier(user);
                p = onlinePunishment;
            } else {
                final OfflinePunishment offlinePunishment = new OfflinePunishment();
                offlinePunishment.setPrimaryIdentifier(user);
                p = offlinePunishment;
            }
            p.setSecondaryIdentifier(rs.getString("secondaryIdentifier"));
            final Elements pd = new Elements();
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
            return new Request(p);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Request> loadByIP(String address) {
        final List<Request> list = Lists.newArrayList();
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM arcanth_punishments WHERE addressIP = ?");
            st.setObject(1, address);
            final ResultSet rs = st.executeQuery();
            while (rs.next()) {
                final Punishment p;
                final String user = rs.getString("user");
                if (Main.isOnlineMode()) {
                    final OnlinePunishment onlinePunishment = new OnlinePunishment();
                    onlinePunishment.setPrimaryIdentifier(user);
                    p = onlinePunishment;
                } else {
                    final OfflinePunishment offlinePunishment = new OfflinePunishment();
                    offlinePunishment.setPrimaryIdentifier(user);
                    p = offlinePunishment;
                }
                p.setSecondaryIdentifier(rs.getString("secondaryIdentifier"));
                final Elements pd = new Elements();
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
                list.add(new Request(p));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private List<Request> loadAllByIdentifier(String identifier, boolean isSecondary) {
        final List<Request> list = Lists.newArrayList();
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM arcanth_punishments WHERE " + (isSecondary ? "secondaryIdentifier" : "user") + " = ?");
            st.setObject(1, identifier);
            final ResultSet rs = st.executeQuery();
            while (rs.next()) {
                final Punishment p;
                final String user = rs.getString("user");
                if (Main.isOnlineMode()) {
                    final OnlinePunishment onlinePunishment = new OnlinePunishment();
                    onlinePunishment.setPrimaryIdentifier(user);
                    p = onlinePunishment;
                } else {
                    final OfflinePunishment offlinePunishment = new OfflinePunishment();
                    offlinePunishment.setPrimaryIdentifier(user);
                    p = offlinePunishment;
                }
                p.setSecondaryIdentifier(rs.getString("secondaryIdentifier"));
                final Elements pd = new Elements();
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
                list.add(p.query());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Request loadByIdentifier(String identifier, boolean isSecondary) {
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM arcanth_punishments WHERE " + (isSecondary ? "secondaryIdentifier" : "user") + " = ?");
            st.setString(1, identifier);
            final ResultSet rs = st.executeQuery();
            rs.next();
            final Punishment p;
            final String user = rs.getString("user");
            if (Main.isOnlineMode()) {
                final OnlinePunishment onlinePunishment = new OnlinePunishment();
                onlinePunishment.setPrimaryIdentifier(user);
                p = onlinePunishment;
            } else {
                final OfflinePunishment offlinePunishment = new OfflinePunishment();
                offlinePunishment.setPrimaryIdentifier(user);
                p = offlinePunishment;
            }
            p.setSecondaryIdentifier(rs.getString("secondaryIdentifier"));
            final Elements pd = new Elements();
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
            return new Request(p);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void save(Punishment p) {
        try {
            final Elements pd = p.getData();
            final PreparedStatement st;
            final StringBuilder stringBuilder = new StringBuilder();
            pd.getEvidences().forEach(s -> stringBuilder.append(stringBuilder.toString().equals("") ? s : "," + s));
            boolean isNew = false;
            if (!p.idIsValid()) {
                st = connection.prepareStatement("INSERT INTO arcanth_punishments VALUES(DEFAULT, ?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                st.setString(1, p.getPrimaryIdentifier());
                st.setString(2, !p.ipIsValid() ? "null" : p.getIp());
                st.setString(3, pd.getPunishmentType().getIdentifier());
                st.setString(4, pd.getReason().getReason());
                st.setTimestamp(5, pd.getStartDateSQL());
                st.setTimestamp(6, pd.getFinishDateSQL());
                st.setString(7, pd.getPunisher());
                st.setString(8, stringBuilder.toString());
                st.setString(9, pd.getPunishmentState().getIdentifier());
                st.setBoolean(10, pd.isPermanent());
                st.setString(11, p.getSecondaryIdentifier());
                isNew = true;
            } else {
                st = connection.prepareStatement("INSERT INTO arcanth_punishments VALUES(?,?,?,?,?,?,?,?,?,?,?,?) ON DUPLICATE KEY UPDATE ID = ?, User = ?, addressIP = ?, type = ?, reason = ?, startedAt = ?, finishAt = ?, punisher = ?, evidences = ?, punishmentState = ?, permanent = ?, secondaryIdentifier = ?");
                st.setInt(1, p.getId());
                st.setString(2, p.getPrimaryIdentifier());
                st.setString(3, !p.ipIsValid() ? "null" : p.getIp());
                st.setString(4, pd.getPunishmentType().getIdentifier());
                st.setString(5, pd.getReason().getReason());
                st.setTimestamp(6, pd.getStartDateSQL());
                st.setTimestamp(7, pd.getFinishDateSQL());
                st.setString(8, pd.getPunisher());
                st.setString(9, stringBuilder.toString());
                st.setString(10, pd.getPunishmentState().getIdentifier());
                st.setBoolean(11, pd.isPermanent());
                st.setString(12, p.getSecondaryIdentifier());
                st.setInt(13, p.getId());
                st.setString(14, p.getPrimaryIdentifier());
                st.setString(15, !p.ipIsValid() ? "null" : p.getIp());
                st.setString(16, pd.getPunishmentType().getIdentifier());
                st.setString(17, pd.getReason().getReason());
                st.setTimestamp(18, pd.getStartDateSQL());
                st.setTimestamp(19, pd.getFinishDateSQL());
                st.setString(20, pd.getPunisher());
                st.setString(21, stringBuilder.toString());
                st.setString(22, pd.getPunishmentState().getIdentifier());
                st.setBoolean(23, pd.isPermanent());
                st.setString(24, p.getSecondaryIdentifier());
            }
            st.executeUpdate();
            if (isNew) {
                final ResultSet generatedKeys = st.getGeneratedKeys();
                generatedKeys.next();
                p.setId(generatedKeys.getInt(1));
            }
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

    public Request loadByPrimaryIdentifier(String identifier) {
        return loadByIdentifier(identifier, false);
    }

    public Request loadBySecondaryIdentifier(String identifier) {
        return loadByIdentifier(identifier, true);
    }

    public List<Request> loadAllByPrimaryIdentifier(String identifier) {
        return loadAllByIdentifier(identifier, false);
    }

    public List<Request> loadAllBySecondaryIdentifier(String identifier) {
        return loadAllByIdentifier(identifier, true);
    }

    public void delete(Punishment punishment) {
        delete(punishment.getId(), "id");
    }

}
