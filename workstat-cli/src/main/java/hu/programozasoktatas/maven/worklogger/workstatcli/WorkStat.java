/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.programozasoktatas.maven.worklogger.workstatcli;

import hu.programozasoktatas.maven.worklogger.dto.WorkStatDTO;
import hu.programozasoktatas.maven.worklogger.service.WorkStatService;
import hu.programozasoktatas.maven.worklogger.utility.ConnectionConfig;
import hu.programozasoktatas.maven.worklogger.utility.ConfigUtility;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class WorkStat {

    private ConnectionConfig connConfig = ConfigUtility.getConnectionConfig();
    private Connection conn;
    private WorkStatService wss;
    private DateTimeFormatter hoFormatum;
    private DateTimeFormatter hetFormatum;
    private DateTimeFormatter napFormatum;

    public static void main(String[] args) {
        try {
            WorkStat ws = new WorkStat();
            ws.run(args);
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
            System.exit(1);
        }
    }

    public WorkStat() throws SQLException {
        conn = DriverManager.getConnection(connConfig.getUrl(), connConfig.getUsername(), connConfig.getPassword());
        wss = new WorkStatService(conn);
    }

    private void run(String[] args) throws SQLException {
        if (args.length < 1) {
            System.out.println("Error: too few arguments.");
            System.exit(1);
        }
        if (args[0].equals("daily")) {
            List<WorkStatDTO> dailyList = wss.getDailyStats();
            printDailyStats(dailyList);
        } else if (args[0].equals("weekly")) {
            List<WorkStatDTO> weeklyList = wss.getWeeklyStats();
            printWeeklyStats(weeklyList);
        } else if (args[0].equals("monthly")) {
            List<WorkStatDTO> monthlyList = wss.getMonthlyStats();
            printMonthlyStats(monthlyList);
        }
    }

    private void printDailyStats(List<WorkStatDTO> dailyList) {
        for (int i = 0; i < dailyList.size(); i++) {
            try {
                DateTime beolvasottDatum = DateTime.parse(dailyList.get(i).toString(), napFormatum);
                System.out.println(beolvasottDatum);
            } catch (IllegalArgumentException ex) {
                System.out.println("Nem megfelelő formátum.");
            }
        }
    }

    private void printWeeklyStats(List<WorkStatDTO> weeklyList) {
        for (int i = 0; i < weeklyList.size(); i++) {
            try {
                DateTime beolvasottDatum = DateTime.parse(weeklyList.get(i).toString(), hetFormatum);
                System.out.println(beolvasottDatum);
            } catch (IllegalArgumentException ex) {
                System.out.println("Nem megfelelő formátum.");
            }
        }
    }

    private void printMonthlyStats(List<WorkStatDTO> monthlyList) {
        for (int i = 0; i < monthlyList.size(); i++) {
            try {
                DateTime beolvasottDatum = DateTime.parse(monthlyList.get(i).toString(), hoFormatum);
                System.out.println(beolvasottDatum);
            } catch (IllegalArgumentException ex) {
                System.out.println("Nem megfelelő formátum.");
            }
        }
    }

    public class SimpleDateFormat {

        public SimpleDateFormat(String monthly, String weekly, String daily) {
        }

        public SimpleDateFormat getSimpleDateFormat() {
            Properties format = new Properties();
            try {
                format.load(SimpleDateFormat.class.getResourceAsStream("/formats.ini"));
                String monthly = format.getProperty("format.monthly");
                hoFormatum = DateTimeFormat.forPattern(monthly);
                String weekly = format.getProperty("format.weekly");
                hetFormatum = DateTimeFormat.forPattern(weekly);
                String daily = format.getProperty("format.daily");
                napFormatum = DateTimeFormat.forPattern(daily);
                return new SimpleDateFormat(monthly, weekly, daily);
            } catch (IOException ex) {
                Logger.getLogger(ConfigUtility.class.getName()).log(Level.SEVERE, null, ex);
                return new SimpleDateFormat("yyyy.MM: H'h'm''", "yyyy-'W'w: H'h'm''", "yyyy.MM.dd. H'h'm''");
            }
        }
    }

}
