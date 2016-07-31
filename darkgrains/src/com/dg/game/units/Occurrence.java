/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.game.units;

import com.dg.core.path.Node;
import com.dg.game.units.internal.Bandit;
import com.dg.game.units.internal.BanditTypeEnum;
import com.dg.util.FastRandom;
import com.jme3.asset.AssetManager;
import com.jme3.collision.Collidable;
import com.jme3.material.Material;
import com.jme3.material.RenderState;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.shape.Sphere;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Victor
 */
public class Occurrence extends UnitPack<Bandit> implements Collidable {

    private OccurrenceTypeEnum type;
    private OccurrenceStateEnum state;
    private com.dg.core.path.Node node;
    private float timeElapsed, maxLife = 15f;
    public boolean live = true;
    private boolean letal;

    public Occurrence(OccurrenceTypeEnum type, AssetManager manager, com.dg.core.path.Node node) {
        super();
        this.type = type;
        this.node = node;
        state = OccurrenceStateEnum.OPEN;
        if (FastRandom.randValue(0, 100) < type.getPericles()) {
            letal = true;
        } else {
            letal = false;
        }



        mesh = new Sphere(20, 20, 40f);
        material = new Material(manager, "Common/MatDefs/Light/Lighting.j3md");
//        material.setColor("Color", new ColorRGBA(1f, 0f, 0f, 0.4f));
        material.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        material.setBoolean("UseMaterialColors", true);
        material.setColor("Ambient", new ColorRGBA(1f, 0.3f, 0.3f, 1f));
        material.setColor("Specular", new ColorRGBA(1f, 0.3f, 0.3f, 1f));
        material.setColor("Diffuse", ColorRGBA.Red);
        material.setColor("GlowColor", ColorRGBA.Black);
        material.setFloat("Shininess", 20f);
        this.setQueueBucket(Bucket.Transparent);

        //add bandits TODO
        list.add(new Bandit(manager, BanditTypeEnum.EASY));
        list.add(new Bandit(manager, BanditTypeEnum.EASY));
        list.add(new Bandit(manager, BanditTypeEnum.EASY));
        list.add(new Bandit(manager, BanditTypeEnum.HARD));
        this.setLocalTranslation(node.getPosition().x - 500f, 0f, node.getPosition().y - 500f);

    }

    public Node getNode() {
        return node;
    }

    public OccurrenceStateEnum getState() {
        return state;
    }

    public boolean isLetal() {
        return letal;
    }

    public void update(float tpf) {
        timeElapsed += tpf;
        if (timeElapsed >= maxLife) {
            //efeito?
            state = OccurrenceStateEnum.FINISH_SCAPE;
            this.removeFromParent();
            live = false;
            Logger.getGlobal().log(Level.INFO, "Ocorencia fechada: {0}", toString());
        }

    }

    @Override
    public String toString() {
        return type.name() + " - " + System.identityHashCode(this);
    }

    public float getRemaningTime() {
        return maxLife - timeElapsed;
    }

    public float potFuga() {
        return ((mobilidadeMed() * 0.6f) + (condFisicoMed() * 0.4f)) * FastRandom.randValue(0, 100);
    }

    public void setSelected(boolean b) {
        if (b) {
            material.setColor("GlowColor", ColorRGBA.Red);
        } else {
            material.setColor("GlowColor", ColorRGBA.Black);
        }
    }

    public OccurrenceTypeEnum getType() {
        return type;
    }

    public int getBanditQuantity() {
        return list.size();
    }

    public float getPericulosidade() {
        return type.getPericles();
    }
}
