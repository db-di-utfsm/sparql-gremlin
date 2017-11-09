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
public abstract class Variable {
    
    public static enum Type {
        NODE, 
        EDGE,
        PROPERTY,
        VALUE,
        METAPROPERTY,
        UNKNOW_S,
        UNKNOW_P;
    }
    
    public static Type[] getSOTypesFromP(Node p){
        Type[] types = new Type[2];
        if(PredicateCheck.isValue(p) ||
                PredicateCheck.isMeta(p)){
            types[0] = Type.PROPERTY;
            types[1] = Type.VALUE;
        }
        else if(PredicateCheck.isNodeProperty(p)){
            types[0] = Type.NODE;
            types[1] = Type.PROPERTY;
        }
        else if(PredicateCheck.isNodeLabel(p) ||
                PredicateCheck.isNodeId(p)){
            types[0] = Type.NODE;
            types[1] = Type.VALUE;
        }
        else if(PredicateCheck.isEdgeIn(p)){
            types[0] = Type.EDGE;
            types[1] = Type.NODE;
        }
        else if(PredicateCheck.isEdgeOut(p)){
            types[0] = Type.NODE;
            types[1] = Type.EDGE;
        }
        else if(PredicateCheck.isEdgeId(p) ||
                PredicateCheck.isEdgeLabel(p)){
            types[0] = Type.EDGE;
            types[1] = Type.VALUE;
        }
        else if(PredicateCheck.isEdgeProperty(p)){
            types[0] = Type.EDGE;
            types[1] = Type.PROPERTY;
        }
        return types;
    }
    
    public static Type getSTypeFromP(Node p){
        return getSOTypesFromP(p)[0];
    } 
}
