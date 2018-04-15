package com.datastax.sparql.star;

import com.datastax.sparql.constants.RE;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SPARQLStarTranslator implements RE {

    final static Pattern starTripleCapture;

    // << >> SINTAX WILL NOT ALLOW SOMETHING DIFERENT IN PREDICATE THAN NP:_ AND E:TO,
    // EVERYTHING ELSE WITH LEAD TO PARSING ERROR IN JENA
    // super ad-hoc
    // TODO allow use of variables in predicate
    final static Pattern nestedTripleCapture;
    final static private Pattern nodePropertyValuePattern;
    final static private Pattern nodeEdgeNodePattern;

    static {
        nodePropertyValuePattern = Pattern.compile(NPV_PATTERN);
        nodeEdgeNodePattern = Pattern.compile(NEN_PATTERN);
        starTripleCapture = Pattern.compile(STAR_TRIPLE_CAPTURING);
        nestedTripleCapture = Pattern.compile(NESTED_TRIPLE_CAPTURING);
    }

    public static String translate(String query) {
        StringBuffer queryBuffer = new StringBuffer(query);
        Matcher npvMatcher = nodePropertyValuePattern.matcher(queryBuffer);
        Matcher nenMatcher = nodeEdgeNodePattern.matcher(queryBuffer);
        ArrayList<SPARQLStarSubstring> substrings = new ArrayList<>();
        substrings.addAll(SPARQLStarNPVSubstring.getSubstrings(npvMatcher));
        substrings.addAll(SPARQLStarNENSubstring.getSubstrings(nenMatcher));
        substrings.forEach((substring) -> substring.replace(queryBuffer));
        return queryBuffer.toString();
    }
}
