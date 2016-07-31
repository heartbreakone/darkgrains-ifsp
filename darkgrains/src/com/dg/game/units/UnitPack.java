/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.game.units;

import com.dg.game.units.internal.Cop;
import com.dg.game.units.internal.Unit;
import com.jme3.scene.Geometry;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Victor
 */
public class UnitPack<E extends Unit> extends Geometry {

    public List<E> list;
    private static Random rd = new Random();
    private static final float minLuck = 0.8f, maxLuck = 1.2f;

    public UnitPack() {
        list = new LinkedList<>();
    }

    public List<E> getList() {
        return list;
    }
    
    public synchronized float persuasaoMed() {
        float med = 0f;
        for (E c : list) {
            med += c.getPersuasao();
        }
        return (med / list.size());
    }

    protected synchronized float mobilidadeMed() {
        float med = 0f;
        for (E c : list) {
            med += c.getMobilidade();
        }
        return (med / list.size());
    }

    protected synchronized float condFisicoMed() {
        float med = 0f;
        for (E c : list) {
            med += c.getCondFisico();
        }
        return (med / list.size());
    }

    public int getUnitQuantity() {
        return list.size();
    }
    
}
