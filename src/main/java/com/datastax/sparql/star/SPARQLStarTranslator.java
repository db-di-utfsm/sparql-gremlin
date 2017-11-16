package com.datastax.sparql.star;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class SPARQLStarTranslator {

    final static private String SPARQL_STAR_LEFT_DELIMITER = "<<";
    final static private String SPARQL_STAR_RIGHT_DELIMITER = ">>";
    // TODO can variables have underscore?
    final static private String NODE_PROPERTY_VALUE_PATTERN_STRING =
            SPARQL_STAR_LEFT_DELIMITER + "\\s*?\\?\\w*?\\s*?np:\\w*?\\s*?[^.;>]*?\\s*?" + SPARQL_STAR_RIGHT_DELIMITER +
                    "\\s*?(\\.|;\\s*?(meta:.*?\\s*?;)*\\s*?meta:.*?\\s*?\\.)";
    final static private String NODE_EDGE_NODE_PATTERN_STRING =
            SPARQL_STAR_LEFT_DELIMITER + "\\s*?\\?\\w*?\\s*?e:to\\s*?\\?\\w*?\\s*?" + SPARQL_STAR_RIGHT_DELIMITER +
                    "\\s*?(\\.|;(\\s*?(e:label|e:id|ep:\\w+?)[^.;]*?\\s*?;)*\\s*?(e:label|e:id|ep:\\w+?)[^.;]*?\\s*?\\.)";

    final static private Pattern nodePropertyValuePattern;
    final static private Pattern nodeEdgeNodePattern;
    final static HashSet<Integer> usedVarNames;

    static{
        nodePropertyValuePattern = Pattern.compile(NODE_PROPERTY_VALUE_PATTERN_STRING);
        nodeEdgeNodePattern = Pattern.compile(NODE_EDGE_NODE_PATTERN_STRING);
        usedVarNames = new HashSet<>();
    }

    static String getRandomVarName(){
        int randomNum;
        do {
            randomNum = ThreadLocalRandom.current().nextInt(0, 99999 + 1); // collision almost imposible?
        } while (usedVarNames.contains(randomNum));
        usedVarNames.add(randomNum);
        return "?r" + String.valueOf(randomNum);
    }

    public static String translate(String query){
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
