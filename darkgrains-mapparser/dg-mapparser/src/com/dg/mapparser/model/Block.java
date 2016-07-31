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
public class Block {
        private final Double x;
        private final Double y;
        private final Double w;
        private final Double h;
        private final Double c;

        public Block(Double x, Double y, Double w, Double h, Double c) {
            this.x = x;
            this.y = y;
            this.w = w;
            this.h = h;
            this.c = c;
        }

        public Double getX() {
            return x;
        }

        public Double getY() {
            return y;
        }

        public Double getW() {
            return w;
        }

        public Double getH() {
            return h;
        }

        public Double getC() {
            return c;
        }

        @Override
        public String toString() {
            return "Block{" + "x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + ", c=" + c + '}';
        }
        
        public JSONObject toJSON() {
            JSONObject ob = new JSONObject();
            ob.put("x", this.x);
            ob.put("y", this.y);
            ob.put("width", this.w);
            ob.put("height", this.h);
            ob.put("color", this.c);
            return ob;            
        }        
    }