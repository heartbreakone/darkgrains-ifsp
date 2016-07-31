/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.util;

import java.util.Random;

/**
 * Classe com alguns métodos randômicos
 * @author Victor
 */
public class FastRandom {

    private static final Random rd = new Random();

    /**
     * Retorna um valor pseudo-randômico que seja menor que max && maior que min
     *
     * @param min valor mínimo inclusive
     * @param max valor máximo inclusive
     * @return float randômico dentro da range
     */
    public static float randValue(float min, float max) {
        return ((rd.nextFloat() * (max - min)) + min);
    }

    /**
     * Retorna um valor pseudo-randômico que seja menor que max && maior que min
     *
     * @param min valor mínimo inclusive
     * @param max valor máximo inclusive
     * @return int randômico dentro da range
     */
    public static int randValue(int min, int max) {
        return ((rd.nextInt(max - min + 1) + min));
    }

    /**
     * Retorna um nextBoolean()
     *
     * @see Random
     * @return booleano randômico
     */
    public static boolean randBool() {
        return rd.nextBoolean();
    }
}
