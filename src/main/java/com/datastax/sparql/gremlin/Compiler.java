package com.datastax.sparql.gremlin;

import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QueryParseException;
import org.apache.jena.query.Syntax;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpVisitorBase;
import org.apache.jena.sparql.algebra.OpWalker;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.algebra.op.OpFilter;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Graph;

import java.util.*;

public class Compiler extends OpVisitorBase {

    private GraphTraversal<?, ?> traversal;
    private Query query;
    private Typifier typifier;

    public Compiler(Graph g, String query) {
        this.traversal = g.traversal().V();
        try {
            this.query = QueryFactory.create(Prefixes.preamblePrepend(query), Syntax.syntaxSPARQL);
        } catch (QueryParseException e) {
            System.out.println("Query doesn't match SPARQL syntax:");
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    private static void printMap(HashMap<String, Variable.Type> map) {
        for (Map.Entry<String, Variable.Type> kv : map.entrySet()) {
            System.out.println(kv.getKey() + " : " + kv.getValue().name());
        }
    }

    public GraphTraversal<?, ?> convertToGremlinTraversal() {
        typifier = new Typifier(query);
        typifier.exec();
        // printMap(typifier);
        final Op op = Algebra.compile(query);
        // ----------
        UnionFinder unionFinder = new UnionFinder();

        OpWalker.walk(op, unionFinder);

        if (unionFinder.found()) {
            Op leftUnionOp = unionFinder.getLeftOp();
            Op rightUnionOp = unionFinder.getRightOp();
            OpsFinder leftOps = new OpsFinder(typifier);
            OpsFinder rightOps = new OpsFinder(typifier);
            OpWalker.walk(leftUnionOp, leftOps);
            OpWalker.walk(rightUnionOp, rightOps);
            traversal = traversal.union(__.match(leftOps.getTraversalsArray()), __.match(rightOps.getTraversalsArray()));
        }
        OpWalker.walk(op, new VisitorAnalyzer());
        // -----------------------
        OpWalker.walk(op, this);
        // TODO COPIED AS IS FROM ORIGINAL CLASS
        if (!query.isQueryResultStar()) {
            final List<String> vars = query.getResultVars();
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
                    if (query.isDistinct()) {
                        traversal = traversal.dedup(all);
                    }
                    final String[] others = Arrays.copyOfRange(all, 2, vars.size());
                    traversal = traversal.select(vars.get(0), vars.get(1), others);
                    break;
            }
        } else {
            if (query.isDistinct()) {
                traversal = traversal.dedup();
            }
        }
        return traversal;
    }

    @Override
    public void visit(final OpBGP opBGP) {
        final List<Triple> triples = opBGP.getPattern().getList();
        final ArrayList<Traversal> matchTraversalsList = new ArrayList<>();
        for (final Triple triple : triples) {
            matchTraversalsList.addAll(Builder.transform(triple, typifier));
        }
        int size = matchTraversalsList.size();
        final Traversal[] matchTraversalsArray = new Traversal[size];
        for (int i = 0; i < size; i++) { // because match needs an array
            matchTraversalsArray[i] = matchTraversalsList.get(i);
        }
        // TODO this asummes just one bgp in query
        traversal = traversal.match(matchTraversalsArray);
    }

    // TODO COPIED AS IS FROM ORIGINAL
    @Override
    public void visit(final OpFilter opFilter) {
        opFilter.getExprs().getList().stream().
                map(WhereTraversalBuilder::transform).
                reduce(traversal, GraphTraversal::where);
    }

}
