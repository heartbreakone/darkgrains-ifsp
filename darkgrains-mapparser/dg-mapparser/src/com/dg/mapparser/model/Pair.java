/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.dg.mapparser.model;

/**
 * Pair / Tuple Implementation for Java
 * @author Arturh / arturh.de
 */
public class Pair<A, B> {
    private final A a;
    private final B b;

    public Pair(A a, B b) {
    	super();
    	this.a = a;
    	this.b = b;
    }

    @Override
    public int hashCode() {
    	int hashFirst = a != null ? a.hashCode() : 0;
    	int hashSecond = b != null ? b.hashCode() : 0;

    	return (hashFirst + hashSecond) * hashSecond + hashFirst;
    }

    @Override
    public boolean equals(Object other) {
    	if (other instanceof Pair) {
    		Pair otherPair = (Pair) other;
    		return 
    		((  this.a == otherPair.a ||
    			( this.a != null && otherPair.a != null &&
    			  this.a.equals(otherPair.a))) &&
    		 (	this.b == otherPair.b ||
    			( this.b != null && otherPair.b != null &&
    			  this.b.equals(otherPair.b))) );
    	}

    	return false;
    }

    @Override
    public String toString()
    { 
           return "(" + a + ", " + b + ")"; 
    }

    public A getA() {
    	return a;
    }

    public B getB() {
    	return b;
    }

}