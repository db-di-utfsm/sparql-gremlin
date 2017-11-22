package com.datastax.sparql.gremlin;

import java.util.HashMap;

public abstract class Variable {

    public enum Type {
        // RDF NODES
        NODE,
        EDGE,
        PROPERTY,
        VALUE,
        // RDF EDGES
        N_VALUE,
        NP,
        META,
        N_ID,
        N_LABEL,
        E_IN,
        E_OUT,
        E_ID,
        E_LABEL,
        EP,
        // multiples posible EDGES
        N_LABEL_ID, // n:label or n:id
        E_ID_LABEL_PROPERTY, // e:label or e:id or ep:property
        N_VALUE_META,


        P_FROM_PROPERTY, P_FROM_NODE, P_FROM_EDGE, // n:value or meta:something
    }

    public static Type[] getSOTypesFromP(String p) {
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

    public static Type getSTypeFromP(String p) {
        return getSOTypesFromP(p)[0];
    }

    public static Type getOTypeFromP(String p) {
        return getSOTypesFromP(p)[1];
    }


    public static Type getSTypeFromPType(HashMap<String, Type> variableTypesMap, String pStr) {
        Type pType = variableTypesMap.get(pStr);
        if (pType == Type.N_VALUE) {
            return Type.PROPERTY;
        } else if (pType == Type.N_LABEL || pType == Type.N_ID) {
            return Type.NODE;
        } else if (pType == Type.E_ID ||
                pType == Type.E_LABEL ||
                pType == Type.EP) {
            return Type.EDGE;
        } else {
            return null; // TODO this is not a good idea
        }
    }

    public static Type getPTypeFromSType(HashMap<String, Type> variableTypesMap, String sStr) {
        Type sType = variableTypesMap.get(sStr);
        if( sType == Type.PROPERTY){
            return Type.P_FROM_PROPERTY;
        }
        else if (sType == Type.NODE){
            return Type.P_FROM_NODE;
        }
        else if (sType == Type.EDGE){
            return Type.P_FROM_EDGE;
        }
        else{
            return null; // TODO again not good
        }
    }

    public static Type[] getSPTypeFromOType(HashMap<String, Type> typesMap, String oStr) {
        Type[] types = new Type[2];
        Type oType = typesMap.get(oStr);
        if(oType == Type.PROPERTY){
            types[0] = Type.NODE;
            types[1] = Type.NP;
        }
        else if(oType == Type.NODE){
            types[0] = Type.EDGE;
            types[1] = Type.E_IN;
        }
        else if(oType == Type.EDGE){
            types[0] = Type.NODE;
            types[1] = Type.E_OUT;
        }
        else if(oType == Type.VALUE){

        }

        return types;
    }

}