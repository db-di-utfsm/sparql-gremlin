package com.datastax.sparql.star;

import com.datastax.sparql.constants.Uri;

import java.util.ArrayList;
import java.util.regex.Matcher;

class SPARQLStarNPVSubstring extends SPARQLStarSubstring implements Uri {

    SPARQLStarNPVSubstring(Matcher matcher) {
        super(matcher);
    }

    static ArrayList<SPARQLStarSubstring> getSubstrings(Matcher matcher) {
        ArrayList<SPARQLStarSubstring> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(new SPARQLStarNPVSubstring(matcher));
        }
        return list;
    }

    void buildMainTriples(String main, StringBuilder builder, String newVariable) {
        String[] splitted = splitStarTriple(main);
        String s = splitted[0];
        String p = splitted[1];
        String o = splitted[2];
        builder.append(s).append(" ").append(p).append(" ").append(newVariable).append(" .").
                append(System.lineSeparator()).append(newVariable).append(" ")
                .append(NODE).append(":").append(VALUE_SUFIX).append(" ").append(o).append(" .").
                append(System.lineSeparator());
    }

}
