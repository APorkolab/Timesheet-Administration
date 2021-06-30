/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.programozasoktatas.maven.worklogger.service;

import java.sql.Connection;
import java.util.List;
import hu.programozasoktatas.maven.worklogger.dto.WorkStatDTO;
import hu.programozasoktatas.maven.worklogger.model.WorkLog;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

public class WorkStatService {

    private WorkLoggerService workLoggerService;

    public WorkStatService(Connection conn) throws SQLException {
        try {
            workLoggerService = new WorkLoggerService(conn);
        } catch (SQLException ex) {
            throw ex;
        }
    }

    /**
     * This method produces daily statistics for the whole timeframe.
     */
    public List<WorkStatDTO> getDailyStats() throws SQLException {

        List<WorkStatDTO> osszesitve = new ArrayList<>();
        List<WorkLog> napiStat = workLoggerService.list();
        Comparator<WorkLog> byStart = Comparator.comparing(WorkLog::getStart);
        napiStat.sort(byStart);

        WorkLog elsoLog = napiStat.get(0);
        LocalDate elsoLogNapja = elsoLog.getStart().toLocalDate();

        long kezdet = elsoLog.getStart().getMillisOfDay();
        long veg = elsoLog.getEnd().getMillisOfDay();
        Duration osszesIdo = new Duration(kezdet, veg);

        for (int j = 1; j < napiStat.size(); j++) {
            WorkLog nextLog = napiStat.get(j);
            LocalDate masodikLogNapja = nextLog.getStart().toLocalDate();

            kezdet = nextLog.getStart().getMillisOfDay();
            if (napiStat.get(j).getEnd() == null) {
                veg = LocalDateTime.now().getMillisOfDay();
            } else {
                veg = nextLog.getEnd().getMillisOfDay();
            }
            Duration aktualisIdotartam = new Duration(kezdet, veg);

            if (elsoLogNapja.equals(masodikLogNapja)) {
                osszesIdo = osszesIdo.plus(aktualisIdotartam);
            } else {
                WorkStatDTO masikNapi = new WorkStatDTO(elsoLogNapja.toString(), osszesIdo);
                osszesitve.add(masikNapi);

                osszesIdo = aktualisIdotartam;
                elsoLogNapja = masodikLogNapja;

            }
        }
        return osszesitve;
    }

    /**
     * This method produces weekly statistics for the whole timeframe.
     */
    public List<WorkStatDTO> getWeeklyStats() throws SQLException {
        List<WorkStatDTO> osszesitve = new ArrayList<>();
        List<WorkLog> hetiStat = workLoggerService.list();
        Comparator<WorkLog> byStart = Comparator.comparing(WorkLog::getStart);
        hetiStat.sort(byStart);

        WorkLog elsoLog = hetiStat.get(0);
        LocalDate elsoLogNapja = elsoLog.getStart().toLocalDate();
        int elsoLogEv = elsoLogNapja.getWeekyear();
        int elsoLogHetSzam = elsoLogNapja.getWeekOfWeekyear();

        long kezdet = elsoLog.getStart().getMillisOfDay();
        long veg = elsoLog.getEnd().getMillisOfDay();
        Duration osszesIdo = new Duration(kezdet, veg);

        for (int j = 1; j < hetiStat.size(); j++) {
            WorkLog nextLog = hetiStat.get(j);
            LocalDate masodikLogNapja = nextLog.getStart().toLocalDate();
            int masodikLogEv = masodikLogNapja.getWeekyear();
            int masodikLogHetSzam = masodikLogNapja.getWeekOfWeekyear();

            kezdet = nextLog.getStart().getMillisOfDay();
            if (hetiStat.get(j).getEnd() == null) {
                veg = LocalDateTime.now().getMillisOfDay();
            } else {
                veg = nextLog.getEnd().getMillisOfDay();
            }
            Duration aktualisIdotartam = new Duration(kezdet, veg);

            if (elsoLogNapja.equals(masodikLogNapja)) {
                osszesIdo = osszesIdo.plus(aktualisIdotartam);
            } else {
                WorkStatDTO masikNapi = new WorkStatDTO(elsoLogNapja.toString(), osszesIdo);
                osszesitve.add(masikNapi);

                osszesIdo = aktualisIdotartam;
                elsoLogNapja = masodikLogNapja;

            }
        }
        return osszesitve;
    }

    /**
     * This method produces monthly statistics for the whole timeframe. It
     * queries all the worklogs and creates workstatdto's out of the raw data.
     * It sums up all the worked hours and minutes and creates one number for
     * the period.
     */
    public List<WorkStatDTO> getMonthlyStats() throws SQLException {
        List<WorkStatDTO> osszesitve = new ArrayList<>();
        List<WorkLog> haviStat = workLoggerService.list();
        Comparator<WorkLog> byStart = Comparator.comparing(WorkLog::getStart);
        haviStat.sort(byStart);

        WorkLog elsoLog = haviStat.get(0);
        LocalDate elsoLogNapja = elsoLog.getStart().toLocalDate();
        int elsoLogEv = elsoLogNapja.getWeekyear();
        int elsoLogHoSzam = elsoLogNapja.getMonthOfYear();

        long kezdet = elsoLog.getStart().getMillisOfDay();
        long veg = elsoLog.getEnd().getMillisOfDay();
        Duration osszesIdo = new Duration(kezdet, veg);

        for (int j = 1; j < haviStat.size(); j++) {
            WorkLog nextLog = haviStat.get(j);
            LocalDate masodikLogNapja = nextLog.getStart().toLocalDate();
            int masodikLogEv = masodikLogNapja.getWeekyear();
            int masodikLogHetSzam = masodikLogNapja.getWeekOfWeekyear();

            kezdet = nextLog.getStart().getMillisOfDay();
            if (haviStat.get(j).getEnd() == null) {
                veg = LocalDateTime.now().getMillisOfDay();
            } else {
                veg = nextLog.getEnd().getMillisOfDay();
            }
            Duration aktualisIdotartam = new Duration(kezdet, veg);

            if (elsoLogNapja.equals(masodikLogNapja)) {
                osszesIdo = osszesIdo.plus(aktualisIdotartam);
            } else {
                WorkStatDTO masikNapi = new WorkStatDTO(elsoLogNapja.toString(), osszesIdo);
                osszesitve.add(masikNapi);

                osszesIdo = aktualisIdotartam;
                elsoLogNapja = masodikLogNapja;

            }
        }
        return osszesitve;
    }

}
