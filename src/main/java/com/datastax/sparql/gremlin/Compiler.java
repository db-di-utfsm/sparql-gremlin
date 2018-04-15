package com.datastax.sparql.gremlin;

import com.datastax.sparql.ops.OpsFinder;
import org.apache.jena.query.*;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpWalker;
import org.apache.jena.sparql.expr.Expr;
import org.apache.tinkerpop.gremlin.process.traversal.Order;
import org.apache.tinkerpop.gremlin.process.traversal.Scope;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Graph;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Compiler{

    private GraphTraversal<?, ?> traversal;
    private Query query;

    public Compiler(Graph g, String query) {
        this.traversal = g.traversal().V();
        try {
            this.query = QueryFactory.create(PreambleBuilder.preamblePrepend(query), Syntax.syntaxSPARQL);
        } catch (QueryParseException e) {
            System.out.println("Query doesn't match SPARQL syntax:");
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    // For testing
    private static void printMap(HashMap<String, Variable.Type> map) {
        for (Map.Entry<String, Variable.Type> kv : map.entrySet()) {
            System.out.println(kv.getKey() + " : " + kv.getValue().name());
        }
    }

    public GraphTraversal<?, ?> convertToGremlinTraversal() {
        Typifier typifier = new Typifier(query);
        typifier.exec();
        FilterBuilder.typifier = typifier;
        final Op op = Algebra.compile(query);
        UnionFinder unionFinder = new UnionFinder();
        OpWalker.walk(op, unionFinder);
        if (unionFinder.found()) {
            Op leftUnionOp = unionFinder.getLeftOp();
            Op rightUnionOp = unionFinder.getRightOp();
            OpsFinder leftOps = new OpsFinder(typifier, false);
            OpsFinder rightOps = new OpsFinder(typifier, true);
            OpWalker.walk(leftUnionOp, leftOps);
            OpWalker.walk(rightUnionOp, rightOps);
            traversal = traversal.union(__.match(leftOps.getTraversalsArray()), __.match(rightOps.getTraversalsArray()));

        } else {
            OpsFinder ops = new OpsFinder(typifier, false);
            OpWalker.walk(op, ops);
            traversal = traversal.match(ops.getTraversalsArray());
        }


        final List<String> vars = query.getResultVars();
        if (!query.isQueryResultStar()) {
            switch (vars.size()) {
                case 0:
                    throw new IllegalStateException();
                case 1:
                    if (query.isDistinct()) {
                        traversal = traversal.dedup(vars.get(0));
                    }
                    traversal = traversal.select(vars.get(0));
                    break;
                case 2:
                    if (query.isDistinct()) {
                        traversal = traversal.dedup(vars.get(0), vars.get(1));
                    }
                    traversal = traversal.select(vars.get(0), vars.get(1));
                    break;
                default:
                    final String[] all = new String[vars.size()];
                    vars.toArray(all);
                    final String[] others = Arrays.copyOfRange(all, 2, vars.size());
                    if (query.isDistinct()) {
                        traversal = traversal.dedup(all);
                    }
                    traversal = traversal.select(vars.get(0), vars.get(1), others);
                    break;
            }
        }

        return traversal;
    }
}
