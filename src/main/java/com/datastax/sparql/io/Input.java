package com.datastax.sparql.io;

import com.datastax.sparql.gremlin.TestQueries;
import org.apache.commons.cli.*;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.io.IoCore;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerFactory;
import org.apache.tinkerpop.gremlin.tinkergraph.structure.TinkerGraph;

import java.io.*;
import java.util.ArrayList;

public class Input {

    private Graph graph;
    private String query;
    private ArrayList<String> expectedResult;
    private boolean isTest;

    public Input(final String[] args) {
        final Options options = new Options();
        options.addOption("f", "file", true, "a file that contains a SPARQL query");
        options.addOption("g", "graph", true, "the graph that's used to execute the query [classic|modern|crew|kryo file]");
        final CommandLineParser parser = new DefaultParser();
        final CommandLine commandLine;
        if (args.length == 0) {
            setNormalExec();
            isTest = false;
        } else {
            try {
                commandLine = parser.parse(options, args);
                setTestExec(commandLine);
                isTest = true;
            } catch (ParseException e) {
                System.out.println("Error parsing arguments");
                System.exit(1);
            }
        }
    }

    private void setNormalExec() {
        graph = TinkerFactory.createTheCrew();
        query = TestQueries.test;
    }

    private void setTestExec(CommandLine commandLine) {
        try {
            final InputStream inputStream;
            if (commandLine.hasOption("file")) {
                String path = commandLine.getOptionValue("file");
                inputStream = new FileInputStream(path);
            } else {
                inputStream = System.in;
            }
            final BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            final StringBuilder queryBuilder = new StringBuilder();
            expectedResult = new ArrayList<>();
            String line;
            while (null != (line = reader.readLine())) {
                if (line.equals("===")) break;
                queryBuilder.append(System.lineSeparator()).append(line);
            }
            while (null != (line = reader.readLine())) {
                expectedResult.add(line);
            }
            query = queryBuilder.toString();
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
        } catch (IOException e) {
            System.out.println("Error reading file");
            System.out.println(e.getMessage());
            System.exit(1);
        }
    }

    public Graph getGraph() {
        return graph;
    }

    public String getQuery() {
        return query;
    }

    boolean getIsTest() {
        return isTest;
    }

    ArrayList<String> getExpectedResult() {
        return expectedResult;
    }
}
