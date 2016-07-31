/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.core.path;

import com.jme3.math.Vector2f;

/**
 *
 * @author Luís
 */
public class Node {

    private final Vector2f position;
    private final int id;

    public Node(Vector2f position, int id) {
        this.position = position;
        this.id = id;
    }

    public Vector2f getVecDist(Node n) {
        Vector2f v = this.position.subtract(n.getPosition());
        if (v.x < 0) {
            v.x = -v.x;
        }
        if (v.y < 0) {
            v.y = -v.y;
        }
        return v;
    }

    public Vector2f getPosition() {
        return position;
    }

    public int getId() {
        return id;
    }

    public float distanceTo(Node n) {
        return this.position.distance(n.getPosition());
    }

    @Override
    public String toString() {
        return "Node{" + "id=" + id + '}';
    }
}
