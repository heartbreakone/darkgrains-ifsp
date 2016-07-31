/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.nw;

import com.dg.game.World;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Luís
 */
public class Response {
    private boolean success;
    private String message;

    public Response(boolean success, String content) {
        this.success = success;
        this.message = content;
    }
    
    public Response(String jsonString) {
        JSONParser jp = new JSONParser();
        try {
            JSONObject jo = (JSONObject) jp.parse(jsonString);
            this.success = (boolean) jo.get("success");
            this.message = (String) jo.get("message");
        } catch (ParseException ex) {
            Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
        }    
    }

    public boolean getSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

}
