package com.datastax.sparql.gremlin;

public abstract class PredicateCheck {

    // TODO use constants for this values, extract them in another class or interface
    private static final String VALUE_P = Prefixes.NODE_URI + "#value";
    private static final String NODE_LABEL_P = Prefixes.NODE_URI + "#label";
    private static final String NODE_ID_P = Prefixes.NODE_URI + "#id"; 
    private static final String EDGE_IN_P = Prefixes.EDGE_URI + "#in";
    private static final String EDGE_OUT_P = Prefixes.EDGE_URI + "#out";
    private static final String EDGE_ID_P = Prefixes.EDGE_URI + "#id"; 
    private static final String EDGE_LABEL_P = Prefixes.EDGE_URI + "#label";
   
    public static boolean isValue(String p){
        return p.equals(VALUE_P);
    }
    
    public static boolean isNodeProperty(String p){
        return p.split("#")[0].equals(Prefixes.NODE_PROPERTY_URI);
    }

    public static boolean isNodeLabel(String p){
        return p.equals(NODE_LABEL_P);
    }
    
    public static boolean isNodeId(String p){
        return p.equals(NODE_ID_P);
    }

    public static boolean isMeta(String p){
        return p.split("#")[0].equals(Prefixes.METAPROPERTY_URI);
    }

    public static boolean isEdgeIn(String p){
        return p.equals(EDGE_IN_P);
    }

    public static boolean isEdgeOut(String p){
        return p.equals(EDGE_OUT_P);
    }

    public static boolean isEdgeId(String p){
        return p.equals(EDGE_ID_P);
    }

    public static boolean isEdgeLabel(String p){
        return p.equals(EDGE_LABEL_P);
    }
    
    public static boolean isEdgeProperty(String p){
        return p.split("#")[0].equals(Prefixes.EDGE_PROPERTY_URI);
    }
}
