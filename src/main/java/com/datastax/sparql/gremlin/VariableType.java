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
public enum VariableType {
    NODE, 
    EDGE,
    PROPERTY,
    VALUE,
    METAPROPERTY,
    UNKNOW_S,
    UNKNOW_P;
    
    public static VariableType[] getSOTypesFromP(Node p){
        VariableType[] types = new VariableType[2];
        if(PredicateCheck.isValue(p) ||
                PredicateCheck.isMeta(p)){
            types[0] = PROPERTY;
            types[1] = VALUE;
        }
        else if(PredicateCheck.isNodeProperty(p)){
            types[0] = NODE;
            types[1] = PROPERTY;
        }
        else if(PredicateCheck.isNodeLabel(p) ||
                PredicateCheck.isNodeId(p)){
            types[0] = NODE;
            types[1] = VALUE;
        }
        else if(PredicateCheck.isEdgeIn(p)){
            types[0] = EDGE;
            types[1] = NODE;
        }
        else if(PredicateCheck.isEdgeOut(p)){
            types[0] = NODE;
            types[1] = EDGE;
        }
        else if(PredicateCheck.isEdgeId(p) ||
                PredicateCheck.isEdgeLabel(p)){
            types[0] = EDGE;
            types[1] = VALUE;
        }
        else if(PredicateCheck.isEdgeProperty(p)){
            types[0] = EDGE;
            types[1] = PROPERTY;
        }
        return types;
    }
    
    public static VariableType getSTypeFromP(Node p){
        return getSOTypesFromP(p)[0];
    } 
}
