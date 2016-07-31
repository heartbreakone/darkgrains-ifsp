/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.game.units.internal;

/**
 *
 * @author Victor
 */
public enum CopTypeEnum {

    CABO(5f,1,1,1,1), TENENTE(10f,2,2,2,1), CORONEL(15f,3,3,3,1);
    
    private float preco;
    private float combate;
    private float condFisico;
    private float mobilidade;
    private float persuasao;
    
    
    private CopTypeEnum(float preco, float combate, float condFisico, float mobilidade, float persuasao) {
        this.preco = preco;
        this.combate = combate;
        this.condFisico = condFisico;
        this.mobilidade = mobilidade;
        this.persuasao = persuasao;
    }

    public float getPreco() {
        return preco;
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
