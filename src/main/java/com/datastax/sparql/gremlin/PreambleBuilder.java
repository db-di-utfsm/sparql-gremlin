package com.datastax.sparql.gremlin;

import com.datastax.sparql.constants.Prefix;

import java.util.HashMap;

public class PreambleBuilder implements Prefix {

    private final static String PREAMBLE;

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
        PREAMBLE = builder.toString();
    }

    public static String preamblePrepend(String query) {
        return PREAMBLE + query;
    }

}
