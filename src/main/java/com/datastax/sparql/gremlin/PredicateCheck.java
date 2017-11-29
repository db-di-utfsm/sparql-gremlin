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

    static private boolean isNValue(String p) {
        return p.equals(VALUE_P);
    }

    static private boolean isNodeProperty(String p) {
        return p.split("#")[0].equals(Prefixes.NODE_PROPERTY_URI);
    }

    static private boolean isNodeLabel(String p) {
        return p.equals(NODE_LABEL_P);
    }

    static private boolean isNodeId(String p) {
        return p.equals(NODE_ID_P);
    }

    static private boolean isMeta(String p) {
        return p.split("#")[0].equals(Prefixes.METAPROPERTY_URI);
    }

    static private boolean isEdgeIn(String p) {
        return p.equals(EDGE_IN_P);
    }

    static private boolean isEdgeOut(String p) {
        return p.equals(EDGE_OUT_P);
    }

    static private boolean isEdgeId(String p) {
        return p.equals(EDGE_ID_P);
    }

    static private boolean isEdgeLabel(String p) {
        return p.equals(EDGE_LABEL_P);
    }

    static private boolean isEdgeProperty(String p) {
        return p.split("#")[0].equals(Prefixes.EDGE_PROPERTY_URI);
    }

    static Variable.Type getType(String p) {
        if (isNValue(p)) return Variable.Type.N_VALUE;
        if (isMeta(p)) return Variable.Type.META;
        if (isNodeProperty(p)) return Variable.Type.NP;
        if (isNodeLabel(p)) return Variable.Type.N_LABEL;
        if (isNodeId(p)) return Variable.Type.N_ID;
        if (isEdgeIn(p)) return Variable.Type.E_IN;
        if (isEdgeOut(p)) return Variable.Type.E_OUT;
        if (isEdgeId(p)) return Variable.Type.E_ID;
        if (isEdgeLabel(p)) return Variable.Type.E_LABEL;
        else return Variable.Type.EP;// if (isEdgeProperty(p)) return  Variable.Type.EP;
    }
}
