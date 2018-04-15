package com.datastax.sparql.star;

import com.datastax.sparql.constants.Uri;

import java.util.ArrayList;
import java.util.regex.Matcher;

class SPARQLStarNENSubstring extends SPARQLStarSubstring implements Uri{

    SPARQLStarNENSubstring(Matcher matcher) {
        super(matcher);
    }

    static ArrayList<SPARQLStarSubstring> getSubstrings(Matcher matcher) {
        ArrayList<SPARQLStarSubstring> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(new SPARQLStarNENSubstring(matcher));
        }
        return list;
    }

    @Override
    void buildMainTriples(String main, StringBuilder builder, String newVariable) {
        String[] splitted = splitStarTriple(main);
        String s = splitted[0];
        String o = splitted[2];
        builder.append(s).append(" ").append(EDGE).append(":").append(OUT_EDGE_SUFIX).append(" ")
                .append(newVariable).append(" .").append(System.lineSeparator()).append(newVariable)
                .append(" ").append(EDGE).append(":").append(IN_EDGE_SUFIX).append(" ").append(o)
                .append(" .").append(System.lineSeparator());
    }
}
