package com.datastax.sparql.constants;

public interface Uri extends Prefix {
     String VALUE_SUFIX = "value";
     String LABEL_SUFIX = "label";
     String ID_SUFIX = "id";
     String IN_EDGE_SUFIX = "in";
     String OUT_EDGE_SUFIX = "out";
     String TO_SUFIX = "to";
     String VALUE_P = NODE_URI + "#" + VALUE_SUFIX;
     String NODE_LABEL_P = NODE_URI + "#" + LABEL_SUFIX;
     String NODE_ID_P = NODE_URI + "#" + ID_SUFIX;
     String EDGE_IN_P = EDGE_URI + "#" + IN_EDGE_SUFIX;
     String EDGE_OUT_P = EDGE_URI + "#" + OUT_EDGE_SUFIX;
     String EDGE_ID_P = EDGE_URI + "#" + ID_SUFIX;
     String EDGE_LABEL_P = EDGE_URI + "#" + LABEL_SUFIX;
}
