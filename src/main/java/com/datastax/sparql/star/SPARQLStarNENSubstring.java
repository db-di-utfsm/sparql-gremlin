package com.datastax.sparql.star;

import java.util.ArrayList;
import java.util.regex.Matcher;

class SPARQLStarNENSubstring extends SPARQLStarSubstring {

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
    void buildMainTriples(String main, StringBuilder builder, String newVariable){
        String[] splitted = splitStarTriple(main);
        String s = splitted[0];
        String o = splitted[2];
        builder.append(s).append(" e:out ").append(newVariable).append(" .").
                append(System.lineSeparator()).append(newVariable).append(" e:in ").append(o).append(" .").
                append(System.lineSeparator());
    }
}
