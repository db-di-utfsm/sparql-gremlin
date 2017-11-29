package com.datastax.sparql.ops;

import com.datastax.sparql.gremlin.OptionalBuilder;
import com.datastax.sparql.gremlin.Typifier;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;

import java.util.ArrayList;
import java.util.List;

class OpBGPOptionalContainer extends OpContainer {

    OpBGPOptionalContainer(Op op, Typifier typifier) {
        super(op, typifier);
    }

    @Override
    ArrayList<Traversal> getTraversals() {
        List<Triple> triples = ((OpBGP) op).getPattern().getList();
        Traversal optionalTraversal = OptionalBuilder.transform(triples);
        ArrayList<Traversal> result = new ArrayList<>();
        result.add(optionalTraversal);
        return result;
    }
}
