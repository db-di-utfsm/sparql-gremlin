package com.datastax.sparql.gremlin;

import java.util.ArrayList;
import java.util.regex.Matcher;

abstract class SPARQLStarSubstring {

    String s;
    int startIndex;
    int finalIndex;

    SPARQLStarSubstring(Matcher matcher){
       this.startIndex = matcher.start();
        this.finalIndex = matcher.end();
        this.s = matcher.group();
    }

    boolean isMultiple() {
        return s.contains(";");
    }

    abstract ArrayList<String> getSPARQLTriples();
}
