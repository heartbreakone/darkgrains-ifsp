
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import org.apache.tomcat.util.codec.binary.Base64;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Luís
 */
public class Session {
    private int userId;
    private String userName;
    private String hash;
    private Date created;
    
    private static final int timeout = 60 * 1000; // An 'i'm alive' request should be enough to keep session alive
    private static final List<Session> activeSessions;
    static {
        activeSessions = new ArrayList<> ();
    }

    private Session() {}
    
    public static Session registerSession(String username, int userid) {
        Session s = null;
                
        verifyProcess();
        if(exists(username)) {
            Session x = getFromUser(username);
            x.created = new Date();
            return x;
        }
        
        /**
         * http://stackoverflow.com/questions/18268502/how-to-generate-salt-value-in-java
         */
        final Random r = new SecureRandom();
        byte[] salt = new byte[32];
        r.nextBytes(salt);
        String encodedSalt = Base64.encodeBase64String(salt);
        
        s = new Session();
        s.created = new Date();
        s.hash = encodedSalt;
        s.userName = username;
        s.userId = userid;
        
        activeSessions.add(s);      
        return s;
    }
    
    public static void verifyProcess() {
        for (int i = 0, n = activeSessions.size(); i < n; i++) {
            Session s = activeSessions.get(i);
            if(!s.isValid()) {
                activeSessions.remove(s);
            }
        }
    }
    
    public static Session validate(String hash) {
        for (int i = 0, n = activeSessions.size(); i < n; i++) {
            Session s = activeSessions.get(i);
            if(s.isValid()) {
                if(s.hash.equals(hash)) {
                    s.created = new Date();
                    return s;
                }
            }
        }
        return null;
    }
    
    private static boolean exists(String username) {
        for (int i = 0, n = activeSessions.size(); i < n; i++) {
            Session s = activeSessions.get(i);
            if(s.userName.equalsIgnoreCase(username)) {
                return true;
            }
        }
        return false;
    }
    
    private static Session getFromUser(String username) {
        for (int i = 0, n = activeSessions.size(); i < n; i++) {
            Session s = activeSessions.get(i);
            if(s.userName.equalsIgnoreCase(username)) {
                return s;
            }
        }
        return null;
    }
    
    public static void end(String hash) {
        for (int i = 0, n = activeSessions.size(); i < n; i++) {
            Session s = activeSessions.get(i);
            if(s.isValid()) {
                if(s.hash.equals(hash)) {
                    activeSessions.remove(s);
                    break;
                }
            }
        }
    }
    
    private boolean isValid() {
        return (this.created.getTime() + timeout) > new Date().getTime();
    }

    public String getUserName() {
        return userName;
    }

    public String getHash() {
        return hash;
    }

    public Date getCreated() {
        return created;
    }

    public int getUserId() {
        return userId;
    }    

    @Override
    public String toString() {
        return hash;
    }
    
    
    
}
