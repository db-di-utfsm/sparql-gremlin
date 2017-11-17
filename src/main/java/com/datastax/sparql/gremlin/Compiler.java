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
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementVisitorBase;
import org.apache.jena.sparql.syntax.ElementWalker;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.*;

public class Compiler extends OpVisitorBase {

    private GraphTraversal<Vertex, ?> traversal;
    private Builder builder;
    private Query query;
    Typifier typifier;

    public Compiler(Graph g, String query) {
        this.traversal = g.traversal().V();
        this.builder = new Builder();
        try {
            this.query = QueryFactory.create(Prefixes.preamblePrepend(query), Syntax.syntaxSPARQL);
        } catch (QueryParseException e){
            System.out.println("La consulta no cumple con la sintaxis SPARQL. Mensaje de error:");
            System.out.println(e.getMessage());
            System.exit(0);
        }
    }

    public GraphTraversal<Vertex, ?> convertToGremlinTraversal() {
        typifier = new Typifier();
        ElementWalker.walk(query.getQueryPattern(),
                new ElementVisitorBase() {
                    public void visit(ElementPathBlock el) {
                        typifier.exec(el.patternElts());
                    }
                });
        printMap(typifier);
        final Op op = Algebra.compile(query);
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



    private static void printMap(HashMap<String, Variable.Type> map){
        for(Map.Entry<String, Variable.Type> kv : map.entrySet()){
            System.out.println(kv.getKey() + " : " + kv.getValue().name());
        }
    }
    @Override
    public void visit(final OpBGP opBGP) {
        final List<Triple> triples = opBGP.getPattern().getList();
        final ArrayList<Traversal> matchTraversalsList = new ArrayList<>();
        for (final Triple triple : triples) {
            matchTraversalsList.addAll(builder.transform(triple, typifier));
        }
        int size = matchTraversalsList.size();
        final Traversal[] matchTraversalsArray = new Traversal[size];
        for( int i = 0; i < size ; i++){ // because match needs an array
            matchTraversalsArray[i] = matchTraversalsList.get(i);
        }
        // TODO this asummes just one bgp in query
        traversal = traversal.match(matchTraversalsArray);
    }

    @Override
    public void visit(final OpFilter opFilter) {
        //noinspection ResultOfMethodCallIgnored
        opFilter.getExprs().getList().stream().
                map(WhereTraversalBuilder::transform).
                reduce(traversal, GraphTraversal::where);
    }

}
