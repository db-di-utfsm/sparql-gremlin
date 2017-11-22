package com.datastax.sparql.star;

import java.util.regex.Matcher;
// TODO use RE contants
abstract class SPARQLStarSubstring {

    String s;
    int startIndex;
    int finalIndex;

    static int deltaLenght = 0;

    SPARQLStarSubstring(Matcher matcher){
       this.startIndex = matcher.start();
        this.finalIndex = matcher.end();
        this.s = matcher.group().trim();
    }

    void replace(StringBuffer queryBuffer) {
        int lengthBefore = queryBuffer.length();
        queryBuffer.replace(startIndex + deltaLenght,
                finalIndex + deltaLenght,getSPARQLTriples());
        int lenghtAfter = queryBuffer.length();
        deltaLenght = deltaLenght + lenghtAfter - lengthBefore;
    }


    String getSPARQLTriples() {
        StringBuilder builder = new StringBuilder();
        String newVariable = SPARQLStarTranslator.getRandomVarName();
        if (isMultiple()) {
            String[] parts = s.split(";");
            String main = parts[0].trim();
            buildMainTriples(main, builder, newVariable);
            for(int i = 1; i < parts.length; i++){
                String[] propsStringParts;
                if (i != parts.length -1) {
                    propsStringParts = parts[i].trim().split("\\s+");
                }
                else{
                    propsStringParts = parts[i].trim().split("\\s*\\.")[0].split("\\s+");
                }
                String propP = propsStringParts[0];
                String propO = propsStringParts[1];
                builder.append(newVariable).append(" ").append(propP).append(" ").append(propO).append(" .")
                        .append(System.lineSeparator());
            }
        } else {
            buildMainTriples(s, builder, newVariable);
        }
        return builder.toString();
    }

    String[] splitStarTriple(String starTriple){
        return starTriple.split("<<\\s*")[1].split("\\s*>>")[0].split("\\s+");
    }

    boolean isMultiple() {
        return s.contains(";");
    }

    abstract void buildMainTriples(String main, StringBuilder builder, String newVariable);
}
