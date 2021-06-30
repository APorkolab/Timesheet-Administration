/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.programozasoktatas.maven.worklogger.dao.impl;

import hu.programozasoktatas.maven.worklogger.dao.WorkLoggerRepository;
import hu.programozasoktatas.maven.worklogger.model.WorkLog;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;


public class WorkLoggerRepositoryImpl implements WorkLoggerRepository {

    private Connection conn;
    private final PreparedStatement findOne;
    private final PreparedStatement findAll;
    private final PreparedStatement findUnclosed;
    private final PreparedStatement insert;
    private final PreparedStatement update;
    //private final PreparedStatement  findByDateAfter;
    private PreparedStatement save;
    private PreparedStatement close;
    //private final PreparedStatement  count;
    //private final PreparedStatement  delete;
    //private final PreparedStatement  exists;

    public WorkLoggerRepositoryImpl(Connection conn) throws SQLException {
        this.conn = conn;
        this.findOne = conn.prepareStatement("SELECT * FROM worklog WHERE id = ?");
        this.findAll = conn.prepareStatement("SELECT * FROM worklog");
        //this.findByDateAfter = conn.prepareStatement("SELECT * FROM worklog WHERE end > ?");
        this.insert = conn.prepareStatement("INSERT INTO worklog (start, end, projekt) VALUES (?, ?, ?)");
        this.update = conn.prepareStatement("UPDATE worklog SET start =  ?, end =  ?, projekt =  ? WHERE  id=  ?");
        //this.count = conn.prepareStatement("SELECT COUNT(*) FROM worklog");
        //this.delete = conn.prepareStatement("DELETE FROM worklog WHERE id= ?");
        //this.exists = conn.prepareStatement("SELECT IF(EXISTS(?),1,0) AS result");
        this.findUnclosed = conn.prepareStatement("SELECT * FROM worklog WHERE end IS NULL");
    }

    @Override
    public WorkLog findOne(Integer id) throws SQLException {
        this.findOne.setInt(1, id);
        List<WorkLog> ret;
        try ( ResultSet byId = this.findOne.executeQuery()) {
            ret = makeList((ResultSet) byId);
        }
        if (ret.size() != 0) {
            return ret.get(0);
        }
        return null;
    }

    @Override
    public List<WorkLog> findAll() throws SQLException {
        ResultSet all = this.findAll.executeQuery();
        List<WorkLog> ret = makeList((ResultSet) all);
        all.close();
        return ret;
    }

    @Override
    public List<WorkLog> findByDateAfter(LocalDate date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void save(WorkLog entity) {
        if (entity.getId() == null) {
            insert(entity);
        } else {
            update(entity);
        }
    }

    @Override
    public void insert(WorkLog entity) {
        try {
            this.insert.setTimestamp(1, new Timestamp(entity.getStart().toDateTime().getMillis()));
            if (entity.getEnd() != null) {
                this.insert.setTimestamp(2, new Timestamp(entity.getEnd().toDateTime().getMillis()));
            } else {
                this.insert.setTimestamp(2, null);
            }
            this.insert.setString(3, entity.getMessage());
            this.insert.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void update(WorkLog entity) {
        try {
            this.update.setTimestamp(1, new Timestamp(entity.getStart().toDateTime().getMillis()));
            if (entity.getEnd() != null) {
                this.update.setTimestamp(2, new Timestamp(entity.getEnd().toDateTime().getMillis()));
            } else {
                this.update.setTimestamp(2, null);
            }
            this.update.setString(3, entity.getMessage());
            this.update.setInt(4, entity.getId());
            this.update.executeUpdate();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public int count() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void delete(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean exists(Integer id) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public WorkLog findUnclosed() throws SQLException {
        ResultSet unClosed = this.findUnclosed.executeQuery();
        List<WorkLog> ret = makeList((ResultSet) unClosed);
        unClosed.close();
        if (ret.size() != 0) {
            return ret.get(0);
        }
        return null;
    }

    private WorkLog makeOne(ResultSet rs) throws SQLException {
        WorkLog m = new WorkLog();
        m.setId(rs.getInt("id"));
        m.setStart(new LocalDateTime(rs.getTimestamp("start")));
        if (rs.getTimestamp("end") != null) {
            m.setEnd(new LocalDateTime(rs.getTimestamp("end")));
        }
        m.setMessage(rs.getString("projekt"));
        return m;
    }

    private List<WorkLog> makeList(ResultSet rs) throws SQLException {
        List<WorkLog> ret = new ArrayList<>();
        while (rs.next()) {
            ret.add((WorkLog) makeOne(rs));
        }
        return ret;
    }
}
