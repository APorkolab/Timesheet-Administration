/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.programozasoktatas.maven.worklogger.utility;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dániel
 */
    public class ConfigUtility {

        public static ConnectionConfig getConnectionConfig() {
            Properties prop = new Properties();
            try {
                prop.load(ConfigUtility.class.getResourceAsStream("/db.ini"));
                String url = prop.getProperty("db.url");
                String user = prop.getProperty("db.user");
                String pass = prop.getProperty("db.pass");
                return new ConnectionConfig(url, user, pass);
            } catch (IOException ex) {
                Logger.getLogger(ConfigUtility.class.getName()).log(Level.SEVERE, null, ex);
                return new ConnectionConfig("jdbc:mysql://localhost/worklogger", "root", "1234");
            }
        }
    }