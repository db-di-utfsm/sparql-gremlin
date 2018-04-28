package com.datastax.sparql.io;

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
    private boolean isTest = false;
    private boolean isRegular = false;

    public Input(final String[] args) {
        final Options options = new Options();
        options.addOption("f", "file", true, "a file that contains a SPARQL query");
        options.addOption("g", "graph", true, "the graph that's used to execute the query [classic|modern|crew|kryo file]");
        options.addOption("t", "test", false, "set test query, with expected result");
        final CommandLineParser parser = new DefaultParser();
        final CommandLine commandLine;
        if (args.length == 0) {
            setDevExec();
            isTest = false;
        } else {
            try {
                commandLine = parser.parse(options, args);
                if (commandLine.hasOption("test")) {
                    isTest = true;
                } else {
                    isRegular = true;
                }
                setExec(commandLine);
            } catch (ParseException e) {
                System.out.println("Error parsing arguments");
                System.exit(1);
            }
        }
    }

    private void setDevExec() {
        graph = TinkerFactory.createTheCrew();
        query = TestQueries.test;
    }

    private void setExec(CommandLine commandLine) {
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

            String line;

            if (this.isRegular) {
                while (null != (line = reader.readLine())) {
                    queryBuilder.append(System.lineSeparator()).append(line);
                }
            } else {
                while (null != (line = reader.readLine())) {
                    if (line.equals("===")) break;
                    queryBuilder.append(System.lineSeparator()).append(line);
                }
                expectedResult = new ArrayList<>();
                while (null != (line = reader.readLine())) {
                    expectedResult.add(line);
                }
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
