package hu.programozasoktatas.maven.worklogger.model;

import org.joda.time.LocalDateTime;


public class WorkLog {

    private Integer id;
    private LocalDateTime start;
    private LocalDateTime end;
    private String message;

    public WorkLog(Integer id, LocalDateTime start, LocalDateTime end, String message) {
        this.id = id;
        this.start = start;
        this.end = end;
        this.message = message;
    }

    public WorkLog(LocalDateTime start, LocalDateTime end, String message) {
        this.start = start;
        this.end = end;
        this.message = message;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public WorkLog() {
    }

    @Override
    public String toString() {
        if (end == null) {
            return start + "-" + "present" + " " + message;
        } else {
            return start + "-" + end + " " + message;
        }
    }
}
