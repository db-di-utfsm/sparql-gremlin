package com.datastax.sparql.ops;

import com.datastax.sparql.gremlin.Typifier;
import org.apache.jena.sparql.algebra.Op;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;

import java.util.ArrayList;

abstract class OpContainer {

    Op op;
    Typifier typifier;

    OpContainer(Op op, Typifier typifier) {
        this.op = op;
        this.typifier = typifier;
    }

    abstract ArrayList<Traversal> getTraversals();

}
