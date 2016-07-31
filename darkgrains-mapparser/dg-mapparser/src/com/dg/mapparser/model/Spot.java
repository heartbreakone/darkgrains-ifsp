/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.mapparser.model;

import org.json.simple.JSONObject;

/**
 *
 * @author Luís
 */
public class Spot extends Pair<Double, Double> {
    private final int id;
    
    public Spot(Double x, Double y, int id) {
        super(x, y);
        
        this.id = id;
    }

    public int getId() {
        return id;
    }
    
    public JSONObject toJSON() {
        JSONObject ob = new JSONObject();
        ob.put("x", this.getA());
        ob.put("y", this.getB());
        ob.put("id", this.id);
        return ob;            
    }   
}
