package me.centralworks.bungee.database;

import me.centralworks.bungee.Main;
import net.md_5.bungee.config.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {

    public static Connection make() {
        try {
            final Configuration configuration = Main.getDataConfiguration();
            String password = configuration.getString("MySQL.Password");
            String user = configuration.getString("MySQL.User");
            String host = configuration.getString("MySQL.Host");
            String port = configuration.getString("MySQL.Port");
            String database = configuration.getString("MySQL.Database");
            String type = "jdbc:mysql://";
            String url = type + host + ":" + port + "/" + database;
            return DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            System.out.print("Ocorreu um erro ao se conectar com o seu banco de dados MySQL, verifique a sua config.yml.");
        }
        return null;
    }

}
