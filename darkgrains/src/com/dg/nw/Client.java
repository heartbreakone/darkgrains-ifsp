/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.nw;

/**
 *
 * @author Luís
 */
public class Client {
    private String userName;
    private String clientHash;

    public Client(String userName, String clientHash) {
        this.userName = userName;
        this.clientHash = clientHash;
    }

    public String getUserName() {
        return userName;
    }

    public String getClientHash() {
        return clientHash;
    }
    
    
}
