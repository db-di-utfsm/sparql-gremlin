package com.datastax.sparql.io;

import com.datastax.sparql.gremlin.Randomizer;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class Output {

    private boolean isTest;
    private boolean isVerbose;
    private ArrayList<String> expectedResult;
    private String originalQuery;
    private String translatedQuery;
    private GraphTraversal<?, ?> traversal;

    public Output(Input input, String originalQuery, String translatedQuery, GraphTraversal<?, ?> traversal) {
        this.isTest = input.getIsTest();
        this.isVerbose = input.getIsVerbose();
        this.expectedResult = input.getExpectedResult();
        this.originalQuery = originalQuery;
        this.translatedQuery = translatedQuery;
        this.traversal = traversal;
    }

    public void print() {
        ArrayList<String> result = (ArrayList<String>) traversal.toStream().map(Object::toString).collect(Collectors.toList());
        deleteDupSufixes(result);
        if (isTest) {
            int resultSize = result.size();
            int expectedResultSize = expectedResult.size();
            if (expectedResultSize != resultSize) {
                testFail(result);
            } else {
                for (int i = 0; i < resultSize; i++) {
                    if (!result.get(i).equals(expectedResult.get(i))) {
                        testFail(result);
                    }
                }
            }
            testOk();
        } else {
            try {
                if(isVerbose){
                    if (!translatedQuery.equals(originalQuery)) {
                        printWithHeadline("SPARQL* Query", originalQuery);
                        printWithHeadline("SPARQL Query", translatedQuery);
                    } else {
                        printWithHeadline("SPARQL Query", originalQuery);
                    }
                    printWithHeadline("Traversal (prior execution)", traversal);
                    printWithHeadline("Result", result);
                }
                else{
                    System.out.println(result);
                }
            } catch (IOException e) {
                System.out.println("Error in output");
            }
        }
    }

    private void deleteDupSufixes(ArrayList<String> result) {
        if (!Randomizer.dup().equals("")) {
            for (int i = 0; i < result.size(); i++) {
                result.set(i, result.get(i).replace(Randomizer.dup(), ""));
            }
        }
    }

    private void testOk() {
        System.out.println("OK");
    }

    private void testFail(ArrayList<String> result) {
        System.out.println("FAIL");
        System.out.println("EXPECTED " + expectedResult);
        System.out.println("GOT " + result);
    }

    private void printWithHeadline(final String headline, final Object content) throws IOException {
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
