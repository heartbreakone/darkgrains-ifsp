/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.game.units;

import com.dg.core.path.Node;
import com.dg.game.OccurenceResolver;
import com.dg.game.controls.BattalionMoveControl;
import com.dg.game.units.internal.Cop;
import com.dg.game.units.internal.CopTypeEnum;
import com.dg.util.FastRandom;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.collision.Collidable;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.shape.Sphere;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Victor
 */
public class Battalion extends UnitPack<Cop> implements Collidable {

    private com.dg.core.path.Node node;
    private BattalionMoveControl moveControl;
    private BattalionStateEnum state;
    public OccurenceResolver resolver;
    public Occurrence destiny;
    private float timeColor;
    private boolean fade = true;

    public Battalion(List<Cop> cops, com.dg.core.path.Node node, AssetManager manager) {
        super();
        state = BattalionStateEnum.BEGIN;
        this.node = node;
        list.addAll(cops);
        moveControl = new BattalionMoveControl(250f);//speed
        addControl(moveControl);
        mesh = new Sphere(10, 10, 20f);
        material = new Material(manager, "Common/MatDefs/Misc/Unshaded.j3md");
        material.setColor("Color", ColorRGBA.Blue);
    }

    public void update(float tpf) {
        if (destiny == null) {
            return;
        }
        if (destiny.live) {
            timeColor += tpf;
            if (timeColor > 0.3f) {
                timeColor = 0f;
                fade = !fade;
                if (fade) {
                    material.setColor("Color", ColorRGBA.Blue);
                } else {
                    material.setColor("Color", ColorRGBA.Red);
                }

            }



            if (moveControl.isReach()) {
                state = BattalionStateEnum.REACHED;
            }
            if (destiny.getRemaningTime() > 0) {
                moveControl.update(tpf);
            } else {
                state = BattalionStateEnum.FINISHED;
                super.removeFromParent();
            }
        } else {
            state = BattalionStateEnum.FINISHED;
            super.removeFromParent();
        }
    }

    public Node getNode() {
        return node;
    }

    public void setNode(com.dg.core.path.Node node) {
        this.node = node;
    }

    public void moveByVec(List<Vector3f> vec) {
        state = BattalionStateEnum.DEPLOYED;
        moveControl.moveTo(vec);
    }

    public float potPersuasao(float ra, int vn) {
        return ((maiorPersuasaoCop().getPersuasao() * 0.6f) + (persuasaoMed() * 0.4f) + ra + vn) * FastRandom.randValue(ra, ra);
    }

    public float potPerseguisao() {
        return ((mobilidadeMed() * 0.6f) + (condFisicoMed() * 0.4f)) * FastRandom.randValue(timeColor, timeColor);
    }

    private synchronized Cop maiorPersuasaoCop() {
        list.sort(new Comparator<Cop>() {
            @Override
            public int compare(Cop o1, Cop o2) {
                if (o1.getPersuasao() > o2.getPersuasao()) {
                    return 1;
                } else if (o1.getPersuasao() < o2.getPersuasao()) {
                    return -1;
                } else if (o1.getPersuasao() == o2.getPersuasao()) {
                    return 0;
                }
                return 0;
            }
        });
        return list.get(0);
    }

    public CopTypeEnum getCopsType() {
        return list.get(0).getType();
    }

    public void setDestiny(Occurrence occ) {
        this.destiny = occ;
    }

    public BattalionStateEnum getState() {
        return state;
    }

    public void setState(BattalionStateEnum state) {
        this.state = state;
    }
}
