/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.programozasoktatas.maven.worklogger.dao;

import java.util.List;
import org.joda.time.LocalDate;
import hu.programozasoktatas.maven.worklogger.model.WorkLog;
import java.sql.SQLException;

/**
 *
 * @author Dániel
 */

public interface WorkLoggerRepository {

    WorkLog findOne(Integer id) throws SQLException;

    List<WorkLog> findAll() throws SQLException;

    List<WorkLog> findByDateAfter(LocalDate date) throws SQLException;

    // more can be added
    WorkLog findUnclosed() throws SQLException;

    void insert(WorkLog entity) throws SQLException;

    void update(WorkLog entity) throws SQLException;

    void save(WorkLog entity) throws SQLException;

    int count();

    void delete(Integer id);

    boolean exists(Integer id);
}
        