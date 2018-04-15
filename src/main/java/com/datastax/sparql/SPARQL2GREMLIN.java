package com.datastax.sparql;

import com.datastax.sparql.gremlin.Compiler;
import com.datastax.sparql.io.Input;
import com.datastax.sparql.io.Output;
import com.datastax.sparql.star.SPARQLStarTranslator;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Graph;

class SPARQL2GREMLIN {

    public static void main(final String[] args){
        Input input = new Input(args);
        Graph graph = input.getGraph();
        String originalQuery = input.getQuery();
        String translatedQuery = SPARQLStarTranslator.translate(originalQuery);
        Compiler compiler = new Compiler(graph, translatedQuery);
        final GraphTraversal<?, ?> traversal = compiler.convertToGremlinTraversal();
        Output output = new Output(input, originalQuery, translatedQuery, traversal);
        output.print();
    }
}
