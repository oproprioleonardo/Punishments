package me.centralworks.modules.reports.dao;

import me.centralworks.database.ConnectionFactory;
import me.centralworks.modules.reports.models.Report;

import java.sql.*;

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
            final PreparedStatement st = connection.prepareStatement("CREATE TABLE IF NOT EXISTS arcanth_reports_victims(ID INT NOT NULL AUTO_INCREMENT, victim VARCHAR(16), reason TEXT, day TIMESTAMP, evidences TEXT, PRIMARY KEY(ID))");
            st.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void save(Report report) {
        try {
            final PreparedStatement st;
            boolean new_report = false;
            if (report.getId() == 0) {
                st = connection.prepareStatement("INSERT INTO arcanth_reports_victims(DEFAULT,?,?,?,?)");
                st.setString(1, report.getVictim());
                st.setString(2, report.getReason());
                st.setTimestamp(3, new Timestamp(report.getDate()));
                st.setString(4, report.getEvidences().toString());
                new_report = true;
            } else {
                st = connection.prepareStatement("INSERT INTO arcanth_reports_victims(?,?,?,?,?) ON DUPLICATE KEY UPDATE id = ?, victim = ?, reason = ?, day = ?, evidence = ?");
                st.setInt(1, report.getId());
                st.setString(2, report.getVictim());
                st.setString(3, report.getReason());
                st.setTimestamp(4, new Timestamp(report.getDate()));
                st.setString(5, report.getEvidences().toString());
                st.setInt(6, report.getId());
                st.setString(7, report.getVictim());
                st.setString(8, report.getReason());
                st.setTimestamp(9, new Timestamp(report.getDate()));
                st.setString(10, report.getEvidences().toString());
            }
            st.executeUpdate();
            if (new_report) {
                final ResultSet rs = st.getGeneratedKeys();
                rs.next();
                report.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Report loadByID(Integer id) {
        try {
            final PreparedStatement st = connection.prepareStatement("SELECT * FROM arcanth_reports_victims WHERE ID = ?");
            st.setInt(1, id);
            final ResultSet rs = st.executeQuery();
            rs.next();
            final Report report = new Report();
            report.setId(id);
            report.setVictim(rs.getString("victim"));

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Report();
    }


}