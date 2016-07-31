/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.game.controls;

import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.AbstractControl;
import com.jme3.scene.control.Control;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 *
 * @author Victor
 */
public class BattalionMoveControl extends AbstractControl {

    private float speed, moveFac, remain;
    private boolean move, reach = false;
    private Vector3f direction;
    private List<Vector3f> way;

    public BattalionMoveControl(float speed) {
        this.speed = speed;
        way = new LinkedList<>();
        move = false;
    }

    public void setWay(List<Vector3f> way) {
        for (Vector3f v : way) {
            this.way.add(v);
        }
    }

    public boolean isReach() {
        return reach;
    }

    @Override
    protected void controlUpdate(float tpf) {
        if (way == null) {
            return;
        }

        if (spatial != null) {
            if (move) {
                if (way.isEmpty()) {
                    reach = true;
                    move = false;
                } else {
                    Vector3f hlp = spatial.getLocalTranslation();
                    moveFac = tpf * speed;
                    remain = hlp.distance(direction);
                    if (moveFac < remain && remain != 0) {
                        hlp = hlp.interpolate(direction, moveFac / remain);
                        spatial.setLocalTranslation(hlp);
                    } else {
                        spatial.setLocalTranslation(direction);
                        direction = way.get(0);
                        way.remove(0);
                    }
                }
            }
        }
    }

    @Override
    public Control cloneForSpatial(Spatial spatial) {
        final BattalionMoveControl control = new BattalionMoveControl(speed);
        /* Optional: use setters to copy userdata into the cloned control */
        control.setSpatial(spatial);
        return control;
    }

    @Override
    protected void controlRender(RenderManager rm, ViewPort vp) {
    }

    public void moveTo(List<Vector3f> direction) {
        setWay(direction);
        this.direction = way.get(0);
        way.remove(0);
        setMove(true);
    }

    public boolean isMove() {
        return move;
    }

    public void setMove(boolean move) {
        this.move = move;
    }
}
