package com.datastax.sparql.gremlin;

import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;

import java.util.ArrayList;
import java.util.List;

public class OpBGPContainer extends OpContainer {

    OpBGPContainer(Op op, Typifier typifier) {
        super(op, typifier);
    }

    @Override
    ArrayList<Traversal> getTraversals() {
        List<Triple> triples = ((OpBGP) op).getPattern().getList();
        ArrayList<Traversal> matchTraversalsList = new ArrayList<>();
        for (final Triple triple : triples) {
            matchTraversalsList.addAll(Builder.transform(triple, typifier));
        }
        return matchTraversalsList;
    }
}
