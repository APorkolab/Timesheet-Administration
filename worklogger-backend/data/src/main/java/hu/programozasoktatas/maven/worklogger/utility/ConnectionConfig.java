/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.programozasoktatas.maven.worklogger.utility;

/**
 *
 * @author Dániel
 */
public class ConnectionConfig {
    private String username;
    private String password;
    private String url;

    public ConnectionConfig(String url, String username, String password) {
        this.username = username;
        this.password = password;
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }
    
}
