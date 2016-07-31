/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.game;

import java.text.DateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author Luís
 */
public class World {
    private int id;
    private String name;
    private Double crimerate;
    private Date created;
    private int difficulty;

    public World(int id, int difficulty, String name, Date created) {
        this.id = id;
        this.difficulty = difficulty;
        this.name = name;
        this.created = created;
    }
    
    public World(String jsonString) {
        try {
            JSONParser jp = new JSONParser();
            JSONObject obj = (JSONObject) jp.parse(jsonString);
            loadFromJSON(obj);
        } catch (ParseException ex) {
            Logger.getLogger(World.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public World(JSONObject jsonObject) {
        loadFromJSON(jsonObject);
    }
    
    private void loadFromJSON(JSONObject jsonObject) {
        this.id = (int) ((int) 0 + (Long) jsonObject.get("id"));
        this.difficulty = (int) ((int) 0 + (Long) jsonObject.get("difficulty"));
        this.name = (String) jsonObject.get("name");
        this.created = new Date((Long) jsonObject.get("created"));
        this.crimerate = (Double) jsonObject.get("crimerate");
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Date getCreated() {
        return created;
    }

    public Double getCrimerate() {
        return crimerate;
    }

    @Override
    public String toString() {
        return name;
    }

    public int getDifficulty() {
        return difficulty;
    }
    
    
}
