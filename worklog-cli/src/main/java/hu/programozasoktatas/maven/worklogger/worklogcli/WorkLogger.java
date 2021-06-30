package hu.programozasoktatas.maven.worklogger.worklogcli;

import hu.programozasoktatas.maven.worklogger.model.WorkLog;
import hu.programozasoktatas.maven.worklogger.service.WorkLoggerService;
import hu.programozasoktatas.maven.worklogger.utility.ConfigUtility;
import hu.programozasoktatas.maven.worklogger.utility.ConnectionConfig;
import hu.programozasoktatas.maven.worklogger.utility.TimeUtility;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.LocalTime;


public class WorkLogger {

    ConnectionConfig connConfig = ConfigUtility.getConnectionConfig();
    Connection conn;
    WorkLoggerService wls;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            WorkLogger wl = new WorkLogger();
            wl.run(args);
        } catch (SQLException ex) {
            System.out.println("SQL Error: " + ex.getMessage());
            System.exit(1);
        }
    }

    public WorkLogger() throws SQLException {
        conn = DriverManager.getConnection(connConfig.getUrl(), connConfig.getUsername(), connConfig.getPassword());
        wls = new WorkLoggerService(conn);
    }

    private void run(String[] args) throws SQLException {
        if (args.length < 1) {
            System.out.println("Error: too few arguments.");
            System.exit(1);
        }
        if (args[0].equals("start")) {
            // start (now)
            // start 12:30
            if (args.length >= 2 && TimeUtility.isTime(args[1])) {
                LocalTime t = TimeUtility.parseTime(args[1]);
                wls.start(t, StringUtils.join(args, ' ', 2, args.length));
            } else {
                wls.startNow(StringUtils.join(args, ' ', 1, args.length));
            }
        } else if (args[0].equals("switch")) {
            wls.switchNow(StringUtils.join(args, ' ', 1, args.length));
        } else if (args[0].equals("log")) {
            if (args.length >= 3) {
                if (TimeUtility.isTime(args[1]) && TimeUtility.isTime(args[2])) {
                    LocalTime fromTime = TimeUtility.parseTime(args[1]);
                    LocalTime toTime = TimeUtility.parseTime(args[2]);
                    wls.log(fromTime, toTime, StringUtils.join(args, ' ', 3, args.length));
                } else {
                    System.out.println("2nd and 3rd arguments ahould be time.");
                }
            } else {
                System.out.println("Too few arguments.");
            }
        } else if (args[0].equals("end")) {
            if (args.length >= 2 && TimeUtility.isTime(args[1])) {
                LocalTime t = TimeUtility.parseTime(args[1]);
                wls.end(t);
            } else {
                wls.endNow();
            }

        } else if (args[0].equals("status")) {
            WorkLog log = wls.status();
            printStatus(log);
        } else if (args[0].equals("list")) {
            List<WorkLog> logs = wls.list();
            printWorkLogs(logs);
        } else {
            System.out.println("Command " + args[0] + " not understood.");
        }
    }

    private void printStatus(WorkLog log) {
        if (log.getEnd() == null) {
            System.out.println("Dolgozunk a " + log.getMessage() + "projekten " + log.getStart() + " óta");
        } else {
            System.out.println("Nem dolgozunk" + log.getEnd() + "  óta");
        }

        System.out.println(log.toString());
    }

    private void printWorkLogs(List<WorkLog> logs) {
        for (int i = 0; i < logs.size(); i++) {
            System.out.println(logs.get(i).toString());
        }
    }

}
