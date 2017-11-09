/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastax.sparql.gremlin;

import org.apache.jena.graph.Node;

import java.util.HashMap;

/**
 *
 * @author edmolten
 */
public abstract class Variable {


    public enum Type {
        NODE,
        EDGE,
        PROPERTY,
        VALUE,
        METAPROPERTY,
        N_VALUE,
        UNKNOW_S,
        UNKNOW_P, N_LABEL, N_ID, E_PROPERTY, E_LABEL, E_ID,
        N_LABEL_ID, // n:label or n:id
        E_ID_LABEL_PROPERTY; // e:label or e:id or ep:property
    }

    public static Type[] getSOTypesFromP(Node p) {
        Type[] types = new Type[2];
        if (PredicateCheck.isValue(p) ||
                PredicateCheck.isMeta(p)) {
            types[0] = Type.PROPERTY;
            types[1] = Type.VALUE;
        } else if (PredicateCheck.isNodeProperty(p)) {
            types[0] = Type.NODE;
            types[1] = Type.PROPERTY;
        } else if (PredicateCheck.isNodeLabel(p) ||
                PredicateCheck.isNodeId(p)) {
            types[0] = Type.NODE;
            types[1] = Type.VALUE;
        } else if (PredicateCheck.isEdgeIn(p)) {
            types[0] = Type.EDGE;
            types[1] = Type.NODE;
        } else if (PredicateCheck.isEdgeOut(p)) {
            types[0] = Type.NODE;
            types[1] = Type.EDGE;
        } else if (PredicateCheck.isEdgeId(p) ||
                PredicateCheck.isEdgeLabel(p)) {
            types[0] = Type.EDGE;
            types[1] = Type.VALUE;
        } else if (PredicateCheck.isEdgeProperty(p)) {
            types[0] = Type.EDGE;
            types[1] = Type.PROPERTY;
        }
        return types;
    }

    public static Type getSTypeFromP(Node p) {
        return getSOTypesFromP(p)[0];
    }

    public static Type getSTypeFromPType(HashMap<String, Type> variableTypesMap, String pStr) {
        Type pType = variableTypesMap.get(pStr);
        if (pType == Type.N_VALUE) {
            return Type.PROPERTY;
        } else if (pType == Type.N_LABEL || pType == Type.N_ID) {
            return Type.NODE;
        } else if (pType == Type.E_ID ||
                pType == Type.E_LABEL ||
                pType == Type.E_PROPERTY) {
            return Type.EDGE;
        } else {
            return null; // TODO this is not a good idea
        }
    }

    public static Type getPTypeFromSType(HashMap<String, Type> variableTypesMap, String sStr) {
        Type sType = variableTypesMap.get(sStr);
        if( sType == Type.PROPERTY){
            return Type.N_VALUE;
        }
        else if (sType == Type.NODE){
            return Type.N_LABEL_ID;
        }
        else if (sType == Type.EDGE){
            return Type.E_ID_LABEL_PROPERTY;
        }
        else{
            return null; // TODO again not good
        }
    }

}