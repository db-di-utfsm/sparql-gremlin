package com.datastax.sparql.gremlin;

import org.apache.jena.ext.com.google.common.collect.HashBasedTable;
import org.apache.jena.ext.com.google.common.collect.Table;

import java.util.HashMap;

public abstract class Variable {

    private static HashMap<Type, Type[]> PtoSO;
    private static HashMap<Type, Type[]> OtoSP;
    private static Table<Type, Type, Type> SPtoO;
    private static Table<Type, Type, Type> SOtoP;

    static {
        PtoSO = new HashMap<>();
        PtoSO.put(Type.N_VALUE, new Type[]{Type.PROPERTY, Type.VALUE});
        PtoSO.put(Type.META, new Type[]{Type.PROPERTY, Type.VALUE});
        PtoSO.put(Type.NP, new Type[]{Type.NODE, Type.PROPERTY});
        PtoSO.put(Type.N_LABEL, new Type[]{Type.NODE, Type.VALUE});
        PtoSO.put(Type.N_ID, new Type[]{Type.NODE, Type.VALUE});
        PtoSO.put(Type.E_IN, new Type[]{Type.EDGE, Type.NODE});
        PtoSO.put(Type.E_OUT, new Type[]{Type.NODE, Type.EDGE});
        PtoSO.put(Type.E_ID, new Type[]{Type.EDGE, Type.VALUE});
        PtoSO.put(Type.E_LABEL, new Type[]{Type.EDGE, Type.VALUE});
        PtoSO.put(Type.EP, new Type[]{Type.EDGE, Type.VALUE});

        OtoSP = new HashMap<>();
        OtoSP.put(Type.PROPERTY, new Type[]{Type.NODE, Type.NP});
        OtoSP.put(Type.EDGE, new Type[]{Type.NODE, Type.E_OUT});
        OtoSP.put(Type.NODE, new Type[]{Type.EDGE, Type.E_IN});

        SPtoO = HashBasedTable.create();
        SPtoO.put(Type.NODE, Type.NP, Type.PROPERTY);
        SPtoO.put(Type.NODE, Type.E_OUT, Type.EDGE);
        SPtoO.put(Type.EDGE, Type.E_IN, Type.NODE);

        SOtoP = HashBasedTable.create();
        SOtoP.put(Type.NODE, Type.PROPERTY, Type.NP);
        SOtoP.put(Type.NODE, Type.EDGE, Type.E_OUT);
        SOtoP.put(Type.EDGE, Type.NODE, Type.E_IN);
    }

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
    }

    public static Type[] getSOTypesFromP(String p) {
        Type pType = PredicateCheck.getType(p);
        return PtoSO.get(pType);
    }

    public static Type getSTypeFromP(String p) {
        return getSOTypesFromP(p)[0];
    }

    public static Type getOTypeFromP(String p) {
        return getSOTypesFromP(p)[1];
    }

    public static Type getSTypeFromPType(HashMap<String, Type> variableTypesMap, String pStr) {
        return getSOTypesFromPType(variableTypesMap, pStr)[0];
    }

    public static Type[] getSPTypeFromOType(HashMap<String, Type> typesMap, String oStr) {
        Type oType = typesMap.get(oStr);
        return OtoSP.get(oType);
    }

    public static Type getPTypeFromSOTypes(HashMap<String, Type> typesMap, String sStr, String oStr) {
        Type sType = typesMap.get(sStr);
        Type oType = typesMap.get(oStr);
        return SOtoP.get(sType, oType);
    }

    public static Type getOTypeFromSPTypes(HashMap<String, Type> typesMap, String sStr, String pStr) {
        Type sType = typesMap.get(sStr);
        Type pType = typesMap.get(pStr);
        return SPtoO.get(sType, pType);
    }

    public static Type[] getSOTypesFromPType(HashMap<String, Type> typesMap, String pStr) {
        Type pType = typesMap.get(pStr);
        return PtoSO.get(pType);
    }


}