package com.datastax.sparql.constants;

public interface RE extends Uri{
    String DOT = "\\.";
    String ANY_SPACES = "\\s*?";
    String ONE_OR_MORE_SPACES = "\\s+?";
    String NAME = "(\\w|_)+";
    String VAR = "\\?" + NAME;
    String ANY_NODE_PROP = NODE_PROPERTY + ":" + NAME;
    String ANY_META_PROP = METAPROPERTY + ":" + NAME;
    String VALUE = "[^<.;>]+"; // depends on delimeters
    String NESTED_NODE_PROP = "(" + ANY_META_PROP + "|" + VAR + ")" + ONE_OR_MORE_SPACES + "(" + VAR + "|" + VALUE + ")";
    String SPARQL_STAR_LEFT_DELIMITER = "<<";
    String SPARQL_STAR_RIGHT_DELIMITER = ">>";
    String TO = EDGE + ":" + TO_SUFIX;
    String EDGE_ID = EDGE + ":" + ID_SUFIX;
    String EDGE_LABEL = EDGE + ":" + LABEL_SUFIX;
    String ANY_EDGE_PROP = EDGE_PROPERTY + ":" + NAME;
    String NESTED_EDGE_PROP = "(" + EDGE_ID + "|" + EDGE_LABEL + "|" + ANY_EDGE_PROP + "|" + VAR + ")"
            + ONE_OR_MORE_SPACES + "(" + VAR + "|" + VALUE + ")";
    String NPV_PATTERN = SPARQL_STAR_LEFT_DELIMITER + ANY_SPACES + VAR + ONE_OR_MORE_SPACES +
            ANY_NODE_PROP + ONE_OR_MORE_SPACES + VALUE + ANY_SPACES + SPARQL_STAR_RIGHT_DELIMITER + ANY_SPACES +
            "(" + DOT + "|;" + ANY_SPACES + "(" + NESTED_NODE_PROP + ANY_SPACES + ";)*" + ANY_SPACES +
            NESTED_NODE_PROP + ANY_SPACES + DOT + ")";
    String NEN_PATTERN = SPARQL_STAR_LEFT_DELIMITER + ANY_SPACES + VAR + ONE_OR_MORE_SPACES +
            TO + ONE_OR_MORE_SPACES + VAR + ANY_SPACES + SPARQL_STAR_RIGHT_DELIMITER + ANY_SPACES +
            "(" + DOT + "|;" + ANY_SPACES + "(" + NESTED_EDGE_PROP + ANY_SPACES + ";)*" + ANY_SPACES +
            NESTED_EDGE_PROP + ANY_SPACES + DOT + ")";
    String STAR_TRIPLE_CAPTURING = SPARQL_STAR_LEFT_DELIMITER + ANY_SPACES + "(?<s>" + VAR + ")" + ONE_OR_MORE_SPACES +
            "(?<p>" + ANY_NODE_PROP + "|" + TO + ")" + ONE_OR_MORE_SPACES + "(?<o>" + VALUE + "|" + VAR + ")" + ANY_SPACES + SPARQL_STAR_RIGHT_DELIMITER;
    String NESTED_TRIPLE_CAPTURING = ANY_SPACES + "(?<p>" + ANY_META_PROP + "|" + EDGE_ID + "|" + EDGE_LABEL + "|"
            + ANY_EDGE_PROP + "|" + VAR + ")" + ONE_OR_MORE_SPACES + "(?<o>" + VAR + "|"
            + VALUE + ")" + ANY_SPACES + DOT + "?";
}