/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dg.core.path;

/**
 *
 * @author Luís
 */
public class Connection {
    private final Node a;
    private final Node b;
    private final int id;

    public Connection(Node a, Node b, int id) {
        if(a == null || b == null) {
            throw new RuntimeException();
        }
        
        this.a = a;
        this.b = b;
        this.id = id;
    }

    public Node getA() {
        return a;
    }

    public Node getB() {
        return b;
    }

    public int getId() {
        return id;
    }

}
