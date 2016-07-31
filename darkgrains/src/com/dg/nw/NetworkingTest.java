/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.nw;

import java.io.IOException;

/**
 *
 * @author Luís
 */
public class NetworkingTest {
    public static void main(String[] args) throws IOException {
        ConnectionHandler ch = new ConnectionHandler("http://localhost:8084/darkgrains-web/gamedata", "Stewie", "1234");
    }
}
