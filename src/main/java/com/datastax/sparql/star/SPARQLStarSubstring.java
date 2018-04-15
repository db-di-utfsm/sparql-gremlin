package com.datastax.sparql.star;

import com.datastax.sparql.gremlin.Randomizer;

import java.util.regex.Matcher;

abstract class SPARQLStarSubstring {

    private static int deltaLenght = 0;
    private String s;
    private int startIndex;
    private int finalIndex;

    SPARQLStarSubstring(Matcher matcher) {
        this.startIndex = matcher.start();
        this.finalIndex = matcher.end();
        this.s = matcher.group().trim();
    }

    void replace(StringBuffer queryBuffer) {
        int lengthBefore = queryBuffer.length();
        queryBuffer.replace(startIndex + deltaLenght,
                finalIndex + deltaLenght, getSPARQLTriples());
        int lenghtAfter = queryBuffer.length();
        deltaLenght = deltaLenght + lenghtAfter - lengthBefore;
    }

    String getSPARQLTriples() {
        StringBuilder builder = new StringBuilder();
        String newVariable = Randomizer.getRandomVarName();
        if (isMultiple()) {
            String[] parts = s.split(";");
            String main = parts[0].trim();
            buildMainTriples(main, builder, newVariable);
            for (int i = 1; i < parts.length; i++) {
                String nestedTriple = parts[i];
                Matcher capturing = SPARQLStarTranslator.nestedTripleCapture.matcher(nestedTriple);
                capturing.find();
                String propP = capturing.group("p");
                String propO = capturing.group("o");
                builder.append(newVariable).append(" ").append(propP).append(" ").append(propO).append(" .")
                        .append(System.lineSeparator());
            }
        } else {
            buildMainTriples(s, builder, newVariable);
        }
        return builder.toString();
    }

    String[] splitStarTriple(String starTriple) {
        Matcher capturing = SPARQLStarTranslator.starTripleCapture.matcher(starTriple);
        capturing.find();
        String[] result = new String[3];
        result[0] = capturing.group("s");
        result[1] = capturing.group("p");
        result[2] = capturing.group("o");
        return result;
    }

    boolean isMultiple() {
        return s.contains(";");
    }

    abstract void buildMainTriples(String main, StringBuilder builder, String newVariable);
}
