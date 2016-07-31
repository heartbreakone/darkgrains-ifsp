/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.game.units.internal;

import com.dg.game.units.UnitPack;
import com.dg.util.FastRandom;
import com.jme3.scene.Geometry;

/**
 *
 * @author Victor
 */
public class Unit {

    public UnitStateEnum lifeState = UnitStateEnum.VIVO;
    protected float combate;
    protected float condFisico;
    protected float persuasao;
    protected float mobilidade;
    
    public float getCombate() {
        return combate;
    }

    public float getCondFisico() {
        return condFisico;
    }

    public float getPersuasao() {
        return persuasao;
    }

    public float getMobilidade() {
        return mobilidade;
    }

    public float poderLuta(float ra, int vn) {
        return ((combate * 0.9f) + (condFisico * 0.1f) + ra + vn) * FastRandom.randValue(ra, ra);
    }

    public float poderLuta() {
        return ((combate * 0.9f) + (condFisico * 0.1f)) * FastRandom.randValue(combate, combate);
    }

    public UnitStateEnum getLifeState() {
        return lifeState;
    }

    
    
    protected void setUnitState(UnitStateEnum state) {
        switch(state) {
            case VIVO:
                break;
            case FERIDO:
                break;
            case MORTO:
                break;
        }
    }

}
