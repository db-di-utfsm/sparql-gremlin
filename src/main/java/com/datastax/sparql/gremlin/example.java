package com.datastax.sparql.gremlin;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.vocabulary.RDF;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.T;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

public class example {

    public static void main(String[] asd) {
        String mov = "http://bestmoviesever.com/movie#";
        String chara = "http://bestmoviesever.com/character#";
        Model g = ModelFactory.createDefaultModel();
        Resource movie = g.createResource(mov + "345");
        movie.addProperty(RDF.type, g.createResource(mov + "Movie"));
        movie.addProperty(g.createProperty(mov + "title"), "The Matrix");
        movie.addLiteral(g.createProperty(mov + "year"), 1999);
        Resource character = g.createResource(chara + "123");
        movie.addProperty(g.createProperty(mov + "has_character"), character);
        character.addProperty(g.createProperty(chara + "has_name"), "Neo");
        g.setNsPrefix("mov", mov);
        g.setNsPrefix("char", chara);
        g.write(System.out, "TURTLE");

        String queryString = "PREFIX mov: <http://bestmoviesever.com/movie#>" +
                "PREFIX char: <http://bestmoviesever.com/character#>" +
                "SELECT ?name ?char WHERE {" +
                "?m a mov:Movie ;" +
                "mov:title ?name ;" +
                "mov:has_character ?c ." +
                "?c char:has_name ?char . }";
        Query query = QueryFactory.create(queryString);
        QueryExecution qexec = QueryExecutionFactory.create(query, g);
        ResultSet results = qexec.execSelect();
        for (; results.hasNext(); ) {
            QuerySolution soln = results.nextSolution();
            RDFNode movie_name = soln.get("name");
            RDFNode chara_name = soln.get("char");
            System.out.println(movie_name + ", " + chara_name);
        }

        Graph graph = TinkerGraph.open();
        Vertex matrix = graph.addVertex(T.label, "movie", "title", "The Matrix", "genre", "science fiction", "year", 1999);
        Vertex keanu = graph.addVertex(T.label, "actor", "name", "Keany Reeves", "born", "2-9-1964", "height", 1.68, "nationality", "Canadian");
        Vertex john = graph.addVertex(T.label, "movie", "title", "John Wick", "genre", "action", "year", 2014);
        Vertex hugo = graph.addVertex(T.label, "actor", "name", "Hugo Weaving", "born", "4-4-1960", "height", 1.88, "nationality", "British-Australian");
        Vertex lotr = graph.addVertex(T.label, "movie", "title", "The Lord of the Rings: The Fellowship of the Ring", "genre", "epic fantasy", "year", 2001);
        keanu.addEdge("acts_in", john, "name", "John Wick", "role", "protagonist");
        keanu.addEdge("acts_in", matrix, "name", "Neo", "role", "protagonist", "race", "human");
        hugo.addEdge("acts_in", matrix, "name", "Agent Smith", "role", "antagonist", "race", "agent");
        hugo.addEdge("acts_in", lotr, "name", "Elrond", "role", "tertiary", "race", "elf");

        GraphTraversalSource s = graph.traversal();
        //s.V().where(__.is(P.eq(2)))
        System.out.println(s.V().has("name","Hugo Weaving").out("acts_in").values("title").toSet());

    }
}
