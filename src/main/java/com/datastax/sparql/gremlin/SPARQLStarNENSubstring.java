package com.datastax.sparql.gremlin;

import java.util.ArrayList;
import java.util.regex.Matcher;

class SPARQLStarNENSubstring extends SPARQLStarSubstring {

    SPARQLStarNENSubstring(Matcher matcher) {
        super(matcher);
    }

    @Override
    ArrayList<String> getSPARQLTriples() {
        return null;
    }

    static ArrayList<SPARQLStarSubstring> getSubstrings(Matcher matcher) {
        ArrayList<SPARQLStarSubstring> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(new SPARQLStarNENSubstring(matcher));
        }
        return list;
    }
}
