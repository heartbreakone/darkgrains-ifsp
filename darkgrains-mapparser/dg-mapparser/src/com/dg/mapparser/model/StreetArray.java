/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.mapparser.model;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author Luís
 */
public class StreetArray {
    public List<Spot> nodes;
    public List<Connection> connections;
    public List<Block> blocks;

    public StreetArray() {
        nodes = new ArrayList<> ();
        connections = new ArrayList<> ();
        blocks = new ArrayList<> ();
    }      

    public JSONObject toJSON() {
        JSONArray jNodes = new JSONArray ();
        for(Spot jn : nodes) {
            jNodes.add(jn.toJSON());
        }
        
        JSONArray jConnections = new JSONArray ();
        for(Connection jc : connections) {
            jConnections.add(jc.toJSON());
        }
        
        JSONArray jBlocks = new JSONArray ();
        for(Block jb : blocks) {
            jBlocks.add(jb.toJSON());
        }
        
        JSONObject obj = new JSONObject();
        obj.put("nodes", jNodes);
        obj.put("connections", jConnections);
        obj.put("blocks", jBlocks);
        return obj;
    }
}