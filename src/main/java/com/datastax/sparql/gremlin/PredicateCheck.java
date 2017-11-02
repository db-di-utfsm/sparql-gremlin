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
    
    private static final String VALUE_P = Prefixes.NODE + ":value";
    private static final String NODE_LABEL_P = Prefixes.NODE + ":label";
    private static final String NODE_ID_P = Prefixes.NODE + ":id"; 
    private static final String EDGE_IN_P = Prefixes.EDGE + ":in";
    private static final String EDGE_OUT_P = Prefixes.EDGE + ":out";
    private static final String EDGE_ID_P = Prefixes.EDGE + ":id"; 
    private static final String EDGE_LABEL_P = Prefixes.EDGE + ":label";
   
    public static boolean isValue(Node p){
        return p.toString().equals(VALUE_P);
    }
    
    public static boolean isNodeProperty(Node p){
        return p.toString().split(":")[0].equals(Prefixes.NODE_PROPERTY);
    }
    
    public static boolean isNodeLabel(Node p){
        return p.toString().equals(NODE_LABEL_P);
    }
    
    public static boolean isNodeId(Node p){
        return p.toString().equals(NODE_ID_P);
    }
    
    public static boolean isMeta(Node p){
        return p.toString().split(":")[0].equals(Prefixes.METAPROPERTY);
    }
    
    public static boolean isEdgeIn(Node p){
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
        return p.toString().split(":")[0].equals(Prefixes.EDGE_PROPERTY);
    }
}
