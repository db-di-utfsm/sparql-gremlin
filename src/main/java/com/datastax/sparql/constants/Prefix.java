package com.datastax.sparql.constants;

public interface Prefix {
    String BASE_URI = "http://www.tinkerpop.com/traversal/";
    String NODE = "n";
    String EDGE = "e";
    String NODE_PROPERTY = "np";
    String EDGE_PROPERTY = "ep";
    String METAPROPERTY = "meta";
    String NODE_URI = BASE_URI + "node";
    String EDGE_URI = BASE_URI + "edge";
    String NODE_PROPERTY_URI = NODE_URI + "/property";
    String EDGE_PROPERTY_URI = EDGE_URI + "/property";
    String METAPROPERTY_URI = NODE_URI + "/metaproperty";
}
