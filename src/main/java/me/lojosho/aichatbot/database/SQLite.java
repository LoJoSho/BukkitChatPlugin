package me.lojosho.aichatbot.database;

import me.lojosho.aichatbot.Main;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLite {
    private final Main plugin;

    public SQLite(Main plugin) {
        this.plugin = plugin;
    }

    public static void DBConnect(File Folder) {
        Connection c;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + Folder.getAbsolutePath() + "/database.db");
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            Bukkit.broadcastMessage("Averting Shutdown... Error #1 | Karma has encountered an error. Contact LoJoSho on discord");
        }
        System.out.println("Created database successfully");
    }

    public static void DBSend(String sql, File Folder) {
        Connection c;
        Statement stmt ;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + Folder.getAbsolutePath() + "/database.db");
            //System.out.println("Sent database instructions successfully");
            stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            //Bukkit.broadcastMessage("Averting Shutdown... Error #2 | Karma has encountered an error. Contact LoJoSho on discord");
        }
    }
    public static ArrayList checkMessages(File Folder) {
        Connection c;
        Statement stmt;
        ArrayList worldlist = new ArrayList<String>();
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + Folder.getAbsolutePath() + "/database.db");
            c.setAutoCommit(false);
            //System.out.println("Opened database to look for Karma successfully");
            stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM MESSAGEDB;");
            while (rs.next()) {
                worldlist.add(rs.getString("MESSAGE"));
            }
            rs.close();
            stmt.close();
            c.close();
            return worldlist;
        } catch (Exception e) {
            System.err.println(e.getClass().getName() + ": " + e.getMessage());
            //Bukkit.broadcastMessage("Averting Shutdown... Error #4 | Karma has encountered an error. Contact LoJoSho on discord");
        }
        //System.out.println("Operation done successfully");
        return worldlist;
    }

}
