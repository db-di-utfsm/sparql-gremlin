package com.datastax.sparql.gremlin;

import java.util.HashMap;

public class Prefixes {

    public final static String BASE_URI = "http://www.tinkerpop.com/traversal/";
    public final static String NODE = "n";
    public final static String EDGE = "e";
    public final static String NODE_PROPERTY = "np";
    public final static String EDGE_PROPERTY = "ep";
    public final static String METAPROPERTY = "meta";
    public final static String NODE_URI = BASE_URI + "node";
    public final static String EDGE_URI = BASE_URI + "edge";
    public final static String NODE_PROPERTY_URI = NODE_URI + "/property";
    public final static String EDGE_PROPERTY_URI = EDGE_URI + "/property";
    public final static String METAPROPERTY_URI = NODE_URI + "/metaproperty";
    final static String PREFIXES_PREAMBLE;

    static {
        HashMap<String, String> prefixesMap = new HashMap<>();
        prefixesMap.put(NODE, NODE_URI);
        prefixesMap.put(EDGE, EDGE_URI);
        prefixesMap.put(NODE_PROPERTY, NODE_PROPERTY_URI);
        prefixesMap.put(EDGE_PROPERTY, EDGE_PROPERTY_URI);
        prefixesMap.put(METAPROPERTY, METAPROPERTY_URI);
        final StringBuilder builder = new StringBuilder();
        prefixesMap.forEach((key, value) ->
                builder.append("PREFIX ")
                        .append(key)
                        .append(": <")
                        .append(value)
                        .append("#")
                        .append(">")
                        .append(System.lineSeparator()));
        PREFIXES_PREAMBLE = builder.toString();
    }

    public static String preamblePrepend(String query) {
        return PREFIXES_PREAMBLE + query;
    }

}
