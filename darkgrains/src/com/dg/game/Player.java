/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.game;

import com.dg.game.units.Battalion;
import com.dg.game.units.internal.CopTypeEnum;
import com.dg.game.units.internal.Cop;
import com.dg.game.units.internal.UnitStateEnum;
import com.jme3.asset.AssetManager;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Victor
 */
public class Player {

    private float grains;
    private AssetManager manager;
    private List<Cop> cops;

    public Player(AssetManager manager) {
        this.manager = manager;
        cops = new LinkedList<>();
        //test
        grains = 200f;
    }

    public boolean buyCop(CopTypeEnum type) {
        if (grains > type.getPreco()) {
            cops.add(new Cop(type));
            grains -= type.getPreco();
            return true;
        }
        return false;
    }

    public float getGrains() {
        return grains;
    }

    public void setGrains(float grains) {
        this.grains = grains;
    }

    public void payDay(float grains) {
        this.grains += grains;
    }

    public List<Cop> getCops() {
        return cops;
    }

    public Battalion deployCopsByType(CopTypeEnum type, com.dg.core.path.Node node) {
        List<Cop> cs = new ArrayList<>();
        for (Cop cop : cops) {
            if (cop.getType().equals(type)) {
                cs.add(cop);
            }
        }
        cops.removeAll(cs);
        return deployCops(cs, node);
    }

    public List<Cop> getSelectedCops() {
        //todo
        return cops;
    }

    protected Battalion deployCops(List<Cop> cops, com.dg.core.path.Node node) {
        if (cops == null) {
            return null;
        }
        return new Battalion(cops, node, manager);
    }

    public boolean sellCop(CopTypeEnum copTypeEnum) {
        if (cops.isEmpty()) {
            return false;
        }
        List<Cop> cs = new ArrayList<>();
        for (Cop c : cops) {
            if (c.getType().equals(copTypeEnum)) {
                cs.add(c);
            }
        }
        if (!cs.isEmpty()) {
            cops.remove(cs.get(0));
            return true;
        } else {
            return false;
        }
    }

    public int[] getCopsCount() {
        int q1 = 0, q2 = 0, q3 = 0;
        for (Cop c : cops) {
            switch (c.getType()) {
                case CABO:
                    q1++;
                    break;
                case TENENTE:
                    q2++;
                    break;
                case CORONEL:
                    q3++;
                    break;
            }
        }
        return new int[]{q1, q2, q3};
    }

    public void addBattalionResult(Battalion b) {
        for(Cop c : b.getList()) {
            if(c.getLifeState() != UnitStateEnum.MORTO) {
                cops.add(c);
            }
        }
    }
}
