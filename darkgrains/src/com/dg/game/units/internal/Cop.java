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
public class Cop extends Unit {

    private CopTypeEnum type;
    private CopStateEnum state;
    private float preco, awayTime;
    private boolean away;

    public CopStateEnum getState() {
        return state;
    }

    public void setState(CopStateEnum state) {
        this.state = state;
    }

    public float getAwayTime() {
        return awayTime;
    }

    public void setAwayTime(float awayTime) {
        this.awayTime = awayTime;
    }

    public boolean isAway() {
        return away;
    }

    public void setAway(boolean away) {
        this.away = away;
    }

    public Cop(CopTypeEnum type) {
        super();
        this.type = type;
        this.state = CopStateEnum.ESPERA;

        super.combate = type.getCombate();
        super.condFisico = type.getCondFisico();
        super.persuasao = type.getPersuasao();
        super.mobilidade = type.getMobilidade();
        preco = type.getPreco();
    }

    public Cop(AssetManager manager, CopTypeEnum type) {
        super();
        this.type = type;
        this.state = CopStateEnum.ESPERA;

        super.combate = type.getCombate();
        super.condFisico = type.getCondFisico();
        super.persuasao = type.getPersuasao();
        super.mobilidade = type.getMobilidade();
        preco = type.getPreco();
    }

    void setCopState(CopStateEnum state) {
        switch (state) {
            case ESPERA:
                break;
            case CAMINHO:
                break;
            case COMBATE:
                break;
            case COMBATE_LETAL:
                break;
            case PURSUIT:
                break;
        }
    }

    public CopTypeEnum getType() {
        return type;
    }
}
