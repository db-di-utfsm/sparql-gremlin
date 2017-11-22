package com.datastax.sparql.star;

import com.datastax.sparql.gremlin.Prefixes;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SPARQLStarTranslator {

    private interface RE{
        String DOT = "\\.";
        String ANY_SPACES = "\\s*?";
        String ONE_OR_MORE_SPACES = "\\s+?";
        String NAME = "(\\w|_)*";
        String VAR = "\\?"+ NAME;
        String ANY_NODE_PROP = Prefixes.NODE_PROPERTY + ":" + NAME;
        String ANY_META_PROP = Prefixes.METAPROPERTY + ":" + NAME;
        String VALUE = "[^<.;>]+"; // depends on delimeters
        String NODE_PROP_COMPLIMENT = "(" + ANY_META_PROP + "|" + VAR + ")" + ONE_OR_MORE_SPACES + "(" + VAR + "|" + VALUE + ")";
        String SPARQL_STAR_LEFT_DELIMITER = "<<";
        String SPARQL_STAR_RIGHT_DELIMITER = ">>";
        String TO = Prefixes.EDGE + ":to";
        String EDGE_ID = Prefixes.EDGE + ":id";
        String EDGE_LABEL = Prefixes.EDGE + ":label";
        String ANY_EDGE_PROP = Prefixes.EDGE_PROPERTY + ":" + NAME;
        String EDGE_PROP_COMPLIMENT = "(" + EDGE_ID + "|" + EDGE_LABEL + "|" + ANY_EDGE_PROP + "|" + VAR + ")"
                + ONE_OR_MORE_SPACES + "(" + VAR + "|" + VALUE + ")";
        String NODE_PROPERTY_VALUE_PATTERN_STRING = SPARQL_STAR_LEFT_DELIMITER + ANY_SPACES + VAR + ONE_OR_MORE_SPACES +
                ANY_NODE_PROP + ONE_OR_MORE_SPACES + VALUE + ANY_SPACES + SPARQL_STAR_RIGHT_DELIMITER + ANY_SPACES +
                "("+DOT+"|;"+ANY_SPACES+"(" + NODE_PROP_COMPLIMENT + ANY_SPACES + ";)*" + ANY_SPACES +
                NODE_PROP_COMPLIMENT + ANY_SPACES + DOT +")";
        String NODE_EDGE_NODE_PATTERN_STRING = SPARQL_STAR_LEFT_DELIMITER + ANY_SPACES + VAR + ONE_OR_MORE_SPACES +
                TO + ONE_OR_MORE_SPACES +VAR + ANY_SPACES + SPARQL_STAR_RIGHT_DELIMITER + ANY_SPACES +
                "("+DOT+"|;"+ANY_SPACES+"(" + EDGE_PROP_COMPLIMENT + ANY_SPACES + ";)*" + ANY_SPACES +
                EDGE_PROP_COMPLIMENT + ANY_SPACES + DOT + ")";
    }

    // << >> SINTAX WILL NOT ALLOW SOMETHING DIFERENT IN PREDICATE THAN NP:_ AND E:TO,
    // EVERYTHING ELSE WITH LEAD TO PARSING ERROR IN JENA
    // super ad-hoc
    // TODO allow use of variables in predicate


    final static private String NODE_PROPERTY_VALUE_PATTERN_STRING = RE.SPARQL_STAR_LEFT_DELIMITER + "\\s*?\\?\\w*?\\s*?np:\\w*?\\s*?[^.;>]*?\\s*?" + RE.SPARQL_STAR_RIGHT_DELIMITER +
                    "\\s*?(\\.|;\\s*?((meta:.*?|\\?\\w*?)\\s*?;)*\\s*(meta:.*?|\\?\\w*?)\\s*?\\.)";
    final static private String NODE_EDGE_NODE_PATTERN_STRING = RE.SPARQL_STAR_LEFT_DELIMITER + "\\s*?\\?\\w*?\\s*?e:to\\s*?\\?\\w*?\\s*?" + RE.SPARQL_STAR_RIGHT_DELIMITER +
                    "\\s*?(\\.|;(\\s*?(|e:label|e:id|ep:\\w+?)[^.;]*?\\s*?;)*\\s*?(e:label|e:id|ep:\\w+?)[^.;]*?\\s*?\\.)";

    final static private Pattern nodePropertyValuePattern;
    final static private Pattern nodeEdgeNodePattern;
    final private static HashSet<Integer> usedVarNames;

    static{
        nodePropertyValuePattern = Pattern.compile( RE.NODE_PROPERTY_VALUE_PATTERN_STRING);
        nodeEdgeNodePattern = Pattern.compile(RE.NODE_EDGE_NODE_PATTERN_STRING);
        usedVarNames = new HashSet<>();
    }

    static String getRandomVarName(){
        int randomNum;
        do {
            randomNum = ThreadLocalRandom.current().nextInt(0, 99999 + 1);
        } while (usedVarNames.contains(randomNum)); // fast lookup, to ensure not collisions
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
