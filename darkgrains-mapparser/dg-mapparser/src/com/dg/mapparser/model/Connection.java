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
public class Connection {
    private final Spot a;
    private final Spot b;
    private final int id;

    private static int lastId = 0;

    public Connection(Spot a, Spot b) {
        this.a = a;
        this.b = b;

        this.id = ++lastId;
    }

    public Spot getA() {
        return a;
    }

    public Spot getB() {
        return b;
    }

    public JSONObject toJSON() {
        JSONObject ob = new JSONObject();
        ob.put("a", this.a.getId());
        ob.put("b", this.b.getId());
        ob.put("id", this.id);
        return ob;    
    }
}