/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.game.units.internal;

import com.jme3.asset.AssetManager;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.scene.shape.Sphere;

/**
 *
 * @author Victor
 */
public class Bandit extends Unit {

    private BanditStateEnum state;
    private BanditTypeEnum type;

    public BanditStateEnum getState() {
        return state;
    }

    public void setState(BanditStateEnum state) {
        this.state = state;
    }

    
    
    public Bandit(AssetManager manager, BanditTypeEnum type) {
        super();
        this.type = type;
        state = BanditStateEnum.ESPERA;
        super.combate = type.getCombate();
        super.condFisico = type.getCondFisico();
        super.persuasao = type.getPersuasao();
        super.mobilidade = type.getMobilidade();
    }

    public void setBanditState(BanditStateEnum state) {
        switch (state) {
            case ESPERA:
                break;
            case COMBATE:
                break;
            case FUGA:
                break;
        }
    }
}
