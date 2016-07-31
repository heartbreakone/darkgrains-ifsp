/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.game.units;

/**
 *
 * @author Victor
 */
public enum OccurrenceTypeEnum {

    FACIL(10f, 1, 1), MEDIO(50f, 2, 5), DIFICIL(100f, 3, 10);
    private float periculosidade;
    private int qntFact;
    private float crimeValue;

    private OccurrenceTypeEnum(float p, int qnt, float crime) {
        periculosidade = p;
        this.qntFact = qnt;
        this.crimeValue = crime;
    }

    public float getCrimeValue() {
        return crimeValue;
    }

    
    
    public float getPericles() {
        return periculosidade;
    }

    public int getQuantityFactor() {
        return qntFact;
    }
}
