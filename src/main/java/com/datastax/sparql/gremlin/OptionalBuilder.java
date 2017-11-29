package com.datastax.sparql.gremlin;

import org.apache.jena.graph.Triple;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;

import java.util.List;

public abstract class OptionalBuilder {

    public static GraphTraversal transform(List<Triple> triples) {
        GraphTraversal traversal = null;
        Triple triple = null;
        GraphTraversal result = null;
        for (int i = 0; i < triples.size(); i++) {
            triple = triples.get(i);
            if (i == 0) {
                result = __.as(triple.getSubject().toString());
            } else {
                String p = triple.getPredicate().toString();
                traversal = buildTraversalFromPredicate(traversal, p);
            }
        }
        result = result.choose(traversal, __.constant("")).as(triple.getObject().toString());
        return result;
    }

    private static GraphTraversal buildTraversalFromPredicate(GraphTraversal traversal, String p) {
        switch (PredicateCheck.getType(p)) {
            case N_VALUE:
                return traversal.value();
            case NP:
                return traversal.properties(p.split("#")[1]);
            case META:
                return traversal.properties(p.split("#")[1]);
            case N_ID:
                return traversal.id();
            case N_LABEL:
                return traversal.label();
            case E_IN:
                return traversal.inV();
            case E_OUT:
                return traversal.outE();
            case E_ID:
                return traversal.id();
            case E_LABEL:
                return traversal.label();
            default: //case EP: TODO throws error as default
                return traversal.properties(p.split("#")[1]);
        }
    }
}
