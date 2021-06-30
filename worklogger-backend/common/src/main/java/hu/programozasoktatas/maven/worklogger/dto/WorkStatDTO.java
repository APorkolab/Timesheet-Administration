/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.programozasoktatas.maven.worklogger.dto;

import org.joda.time.Duration;

/**
 * This is a Data Transfer Object class to hold the statistical data created by the statistic service.
 * @author Dániel
 */
public class WorkStatDTO {
    private String label; // "2015-11-22" (daily) or "2015-w45" (weekly) or "2015-11" (monthly)
    private Duration worked;

    public WorkStatDTO(String label, Duration worked) {
        this.label = label;
        this.worked = worked;
    }

    public String getLabel() {
        return label;
    }

    public Duration getWorked() {
        return worked;
    }
    
}
