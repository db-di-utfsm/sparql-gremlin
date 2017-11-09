/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastax.sparql.gremlin;

import org.apache.jena.graph.Node;

/**
 *
 * @author edmolten
 */
public abstract class PredicateCheck {
    
    private static final String VALUE_P = Prefixes.NODE_URI + "#value";
    private static final String NODE_LABEL_P = Prefixes.NODE_URI + "#label";
    private static final String NODE_ID_P = Prefixes.NODE_URI + "#id"; 
    private static final String EDGE_IN_P = Prefixes.EDGE_URI + "#in";
    private static final String EDGE_OUT_P = Prefixes.EDGE_URI + "#out";
    private static final String EDGE_ID_P = Prefixes.EDGE_URI + "#id"; 
    private static final String EDGE_LABEL_P = Prefixes.EDGE_URI + "#label";
   
    public static boolean isValue(Node p){
        System.out.println("checking this: ");
        System.out.println(p.toString());
        return p.toString().equals(VALUE_P);
    }
    
    public static boolean isNodeProperty(Node p){
        System.out.println(p.toString());
        return p.toString().split(":")[0].equals(Prefixes.NODE_PROPERTY_URI);
    }
    
    public static boolean isNodeLabel(Node p){
        System.out.println(p.toString());
        return p.toString().equals(NODE_LABEL_P);
    }
    
    public static boolean isNodeId(Node p){
        System.out.println(p.toString());
        return p.toString().equals(NODE_ID_P);
    }
    
    public static boolean isMeta(Node p){
        System.out.println(p.toString());
        return p.toString().split(":")[0].equals(Prefixes.METAPROPERTY_URI);
    }
    
    public static boolean isEdgeIn(Node p){
        System.out.println(p.toString());
        return p.toString().equals(EDGE_IN_P);
    }
    
    public static boolean isEdgeOut(Node p){
        return p.toString().equals(EDGE_OUT_P);
    }
    
    public static boolean isEdgeId(Node p){
        return p.toString().equals(EDGE_ID_P);
    }
    
    public static boolean isEdgeLabel(Node p){
        return p.toString().equals(EDGE_LABEL_P);
    }
    
    public static boolean isEdgeProperty(Node p){
        return p.toString().split(":")[0].equals(Prefixes.EDGE_PROPERTY_URI);
    }
}
