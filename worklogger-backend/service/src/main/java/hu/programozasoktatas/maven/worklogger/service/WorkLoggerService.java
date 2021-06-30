package hu.programozasoktatas.maven.worklogger.service;

import java.sql.Connection;
import java.util.List;
import org.joda.time.LocalTime;
import hu.programozasoktatas.maven.worklogger.dao.WorkLoggerRepository;
import hu.programozasoktatas.maven.worklogger.dao.impl.WorkLoggerRepositoryImpl;
import hu.programozasoktatas.maven.worklogger.model.WorkLog;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class WorkLoggerService {

    private final WorkLoggerRepository workLoggerDAO;

    public WorkLoggerService(WorkLoggerRepository workLoggerDAO) {
        this.workLoggerDAO = workLoggerDAO;
    }

    public WorkLoggerService(Connection conn) throws SQLException {
        workLoggerDAO = new WorkLoggerRepositoryImpl(conn);
    }

    public void start(LocalTime t, String message) throws SQLException {
        WorkLog kezd = new WorkLog();
        kezd.setMessage(message);
        LocalDate ma = LocalDate.now();
        LocalDateTime teljes = ma.toLocalDateTime(t);
        kezd.setStart(teljes);
        workLoggerDAO.save(kezd);
    }

    public void startNow(String message) throws SQLException {
        LocalTime t = LocalTime.now();
        start(t, message);
    }

    public void switchNow(String message) throws SQLException {
        endNow();
        startNow(message);
    }

    public void log(LocalTime fromTime, LocalTime toTime, String message) throws SQLException {
        WorkLog utolagosBejegyzes = new WorkLog();
        List<WorkLog> mindenBejegyzes = workLoggerDAO.findAll();
        LocalDate ma = LocalDate.now();
        LocalTime most = LocalTime.now();
        LocalDateTime teljes = ma.toLocalDateTime(most);
        LocalDateTime tol = ma.toLocalDateTime(fromTime);
        LocalDateTime ig = ma.toLocalDateTime(toTime);
        utolagosBejegyzes.setStart(tol);
        utolagosBejegyzes.setEnd(ig);
        utolagosBejegyzes.setMessage(message);
        if (utolagosBejegyzes.getEnd().isBefore(teljes)) {
            for (int i = 0; i < mindenBejegyzes.size(); i++) {
                if (utolagosBejegyzes.getStart().isEqual(mindenBejegyzes.get(i).getStart()) && utolagosBejegyzes.getEnd().isEqual(mindenBejegyzes.get(i).getEnd())) {
                    return;
                } else if (utolagosBejegyzes.getEnd().isAfter(mindenBejegyzes.get(i).getStart())
                        && utolagosBejegyzes.getEnd().isBefore(mindenBejegyzes.get(i).getEnd())) {
                    return;
                } else if (utolagosBejegyzes.getStart().isAfter(mindenBejegyzes.get(i).getStart())
                        && utolagosBejegyzes.getStart().isBefore(mindenBejegyzes.get(i).getEnd())) {
                    return;
                } else if (utolagosBejegyzes.getStart().isBefore(mindenBejegyzes.get(i).getStart())
                        && (utolagosBejegyzes.getEnd().isAfter(mindenBejegyzes.get(i).getEnd()))) {
                    return;
                } else if (mindenBejegyzes.get(i).getStart().isBefore(utolagosBejegyzes.getStart())
                        && (mindenBejegyzes.get(i).getEnd().isAfter(utolagosBejegyzes.getEnd()))) {
                    return;
                }
                workLoggerDAO.insert(utolagosBejegyzes);
            }
        }
    }

    public void end(LocalTime t) throws SQLException {
        LocalDate ma = LocalDate.now();
        LocalDateTime teljes = ma.toLocalDateTime(t);
        WorkLog vissza = workLoggerDAO.findUnclosed();
        vissza.setEnd(teljes);
        workLoggerDAO.save(vissza);
    }

    public void endNow() throws SQLException {
        LocalTime teljes = LocalTime.now();
        end(teljes);
        //workLoggerDAO.save(veg);
    }

    public WorkLog status() throws SQLException {
        List<WorkLog> utolsoMunka = workLoggerDAO.findAll();
        Comparator<WorkLog> byStart = Comparator.comparing(WorkLog::getStart);
        utolsoMunka.sort(byStart);
        return utolsoMunka.get(utolsoMunka.size() - 1);
    }

    public List<WorkLog> list() throws SQLException {
        List<WorkLog> ret = new ArrayList<>();
        ret = workLoggerDAO.findAll();
        return ret;
    }

}
