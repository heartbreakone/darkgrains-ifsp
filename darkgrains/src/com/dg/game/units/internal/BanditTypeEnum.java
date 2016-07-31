/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.game.units.internal;

/**
 *
 * @author Victor
 */
public enum BanditTypeEnum {

    EASY(1,1,1,1), MEDIUM(2,2,2,2), HARD(3,3,3,3);
    
    private float combate;
    private float condFisico;
    private float mobilidade;
    private float persuasao;

    private BanditTypeEnum(float combate, float condFisico, float mobilidade, float persuasao) {
        this.combate = combate;
        this.condFisico = condFisico;
        this.mobilidade = mobilidade;
        this.persuasao = persuasao;
    }

    

    public float getCombate() {
        return combate;
    }

    public float getCondFisico() {
        return condFisico;
    }

    public float getMobilidade() {
        return mobilidade;
    }

    public float getPersuasao() {
        return persuasao;
    }
}
