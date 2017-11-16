/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.datastax.sparql;

import com.datastax.sparql.gremlin.SPARQLStarTranslator;
import com.datastax.sparql.gremlin.TestQueries;
import org.apache.commons.cli.*;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerFactory;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.io.*;
import java.util.Map;

/**
 * @author Daniel Kuppitz (http://gremlin.guru)
 */
class ConsoleCompiler {

    public static void main(final String[] args) throws IOException {

        final Options options = new Options();
        options.addOption("f", "file", true, "a file that contains a SPARQL query");
        options.addOption("g", "graph", true, "the graph that's used to execute the query [classic|modern|crew|kryo file]");

        // this is awful
        // TODO make better use of maven args
        String[] hardcodedArgs = {"-f", "examples/modern1.sparql"};
        final CommandLineParser parser = new DefaultParser();
        final CommandLine commandLine;

        try {
            commandLine = parser.parse(options, hardcodedArgs);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            printHelp(1);
            return;
        }

        final InputStream inputStream = commandLine.hasOption("file")
                ? new FileInputStream(commandLine.getOptionValue("file"))
                : System.in;
        final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        final StringBuilder queryBuilder = new StringBuilder();

        if (!reader.ready()) {
            printHelp(1);
        }

        String line;
        while (null != (line = reader.readLine())) {
            queryBuilder.append(System.lineSeparator()).append(line);
        }

        final String queryString = TestQueries.test;//= queryBuilder.toString();
        final Graph graph;

        if (commandLine.hasOption("graph")) {
            switch (commandLine.getOptionValue("graph").toLowerCase()) {
                case "classic":
                    graph = TinkerFactory.createClassic();
                    break;
                case "modern":
                    graph = TinkerFactory.createModern();
                    break;
                case "crew":
                    graph = TinkerFactory.createTheCrew();
                    break;
                default:
                    graph = TinkerGraph.open();
                    graph.io(IoCore.gryo()).readGraph(commandLine.getOptionValue("graph"));
                    break;
            }
        } else {
            graph = TinkerFactory.createModern();
        }

        //Dummies.add(graph);

        SPARQLStarTranslator.translate(queryString);
        /*
        Compiler compiler = new Compiler(graph, queryString);
        final Traversal<Vertex, ?> traversal = compiler.convertToGremlinTraversal();
        printWithHeadline("SPARQL Query", queryString);        
        printWithHeadline("Traversal (prior execution)", traversal);
        printWithHeadline("Result", String.join(System.lineSeparator(),
                traversal.toStream().map(Object::toString).collect(Collectors.toList())));
        printWithHeadline("Traversal (after execution)", traversal);
        */
    }

    private static void printHelp(final int exitCode) throws IOException {
        final Map<String, String> env = System.getenv();
        final String command = env.containsKey("LAST_COMMAND") ? env.get("LAST_COMMAND") : "sparql-gremlin.sh";
        printWithHeadline("Usage Examples", String.join("\n",
                command + " -f examples/modern1.sparql",
                command + " < examples/modern2.sparql",
                command + " <<< 'SELECT * WHERE { ?a e:knows ?b }'",
                command + " -g crew < examples/crew1.sparql"));
        if (exitCode >= 0) {
            System.exit(exitCode);
        }
    }

    private static void printWithHeadline(final String headline, final Object content) throws IOException {
        final StringReader sr = new StringReader(content != null ? content.toString() : "null");
        final BufferedReader br = new BufferedReader(sr);
        String line;
        System.out.println();
        System.out.println("\u001B[1m" + headline + "\u001B[0m");
        System.out.println();
        boolean skip = true;
        while (null != (line = br.readLine())) {
            skip &= line.isEmpty();
            if (!skip) {
                System.out.println("  " + line);
            }
        }
        System.out.println();
        br.close();
        sr.close();
    }
}
