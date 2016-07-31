/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.nw;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Luís
 */
public class ClientRequest {
    static private final String ENCODING = "UTF-8";
    
    static String buildLoginAction(String user, String password) {
        try {
            user = URLEncoder.encode(user, ENCODING);
            password = URLEncoder.encode(password, ENCODING);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ClientRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "action=logusr&n=" + user + "&pw=" + password;
    }
    
    static String buildRegisterAction(String user, String password, String email) {
        try {
            user = URLEncoder.encode(user, ENCODING);
            password = URLEncoder.encode(password, ENCODING);
            email = URLEncoder.encode(email, ENCODING);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ClientRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "action=regusr&n=" + user + "&pw=" + password + "&em=" + email;
    }
    
    static String updateAction(String hash) {
        try {
            hash = URLEncoder.encode(hash, ENCODING);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ClientRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "action=upd&h=" + hash ;
    }
    
    static String endAction(String hash) {
        try {
            hash = URLEncoder.encode(hash, ENCODING);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ClientRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "action=end&h=" + hash ;
    }
    
    static String getWorldListAction(String hash) {
        try {
            hash = URLEncoder.encode(hash, ENCODING);
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(ClientRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "action=gtwd&h=" + hash ;
    }
    
    static String deleteWorldAction(String hash, int world) {   
        System.out.println("action=delwd" + "&w=" + world + "&h=" + hash);
        return "action=delwd" + "&w=" + world + "&h=" + hash;
    }
}
