package com.datastax.sparql.gremlin;

import org.apache.jena.sparql.algebra.Op;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;

import java.util.ArrayList;

class OpBGPOptionalContainer extends OpContainer {

    OpBGPOptionalContainer(Op op, Typifier typifier) {
        super(op, typifier);
    }

    @Override
    ArrayList<Traversal> getTraversals() {
        return null; // TODO all the optional transaltion
    }
}
