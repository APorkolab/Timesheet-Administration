/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.programozasoktatas.maven.worklogger.utility;

import org.joda.time.LocalTime;

import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

public class TimeUtility {

    // 12:30 or 13:34:56 (24-hour format)
    public static boolean isTime(String timeString) {
        String regex = "^(?:(?:([01]?\\d|2[0-3]):)?([0-5]?\\d):)?([0-5]?\\d)$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(timeString);
        return timeString.matches(regex);
    }

    public static LocalTime parseTime(String timeString) {
        LocalTime t = null;

        if (timeString.split(":").length == 3) {
            try {
                org.joda.time.format.DateTimeFormatter fmts = DateTimeFormat.forPattern("HH:mm:ss");
                t = fmts.parseLocalTime(timeString);
            } catch (DateTimeParseException | IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }
        } else {
            try {
                org.joda.time.format.DateTimeFormatter fmt = DateTimeFormat.forPattern("HH:mm");
                t = fmt.parseLocalTime(timeString);
            } catch (DateTimeParseException | IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
            }
        }
        return t;
    }
}

