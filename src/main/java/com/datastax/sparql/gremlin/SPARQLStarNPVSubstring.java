package com.datastax.sparql.gremlin;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class SPARQLStarNPVSubstring extends SPARQLStarSubstring {

    SPARQLStarNPVSubstring(Matcher matcher){
        super(matcher);
    }

    @Override
    ArrayList<String> getSPARQLTriples() {
        if (isMultiple()) {
            String[] parts = s.split(";");
            String main = parts[0].trim();
            String[] mainSplitted = main.split("<<\\s*")[1].split("\\s*>>")[0].split("\\s*");
            String s = mainSplitted[0];
            String p = mainSplitted[1];
            String o = mainSplitted[2];


        } else {

        }
    }

    static ArrayList<SPARQLStarSubstring> getSubstrings(Matcher matcher) {
        ArrayList<SPARQLStarSubstring> list = new ArrayList<>();
        while (matcher.find()) {
            list.add(new SPARQLStarNPVSubstring(matcher));
        }
        return list;
    }


}
