/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.datastax.sparql.gremlin;

/**
 *
 * @author edmolten
 */
public abstract class TestQueries {
    static public String test1 = "SELECT ?x WHERE {"
                + "?x np:name ?p ."
                + "?p n:value ?name ."
                + "?x e:out ?e ."
                + "?e e:label 'uses' ."
                + "?e ep:skill 3 ."
                + "?e e:in ?software ."
                + "?software np:name ?name ."
                + "?name n:value 'tinkergraph' ."
                + "}";
    static public String test2 =
            "SELECT ?x WHERE {"
            + "?x np:name ?p ."
            + "?p ?what 3 ."
            + "}";

    static public String test3 =
            "SELECT ?x WHERE {" +
                    "?p ?what 3 ." +
                    "?p ?np ?n ." +
                    "?n2 e:in ?n ."
                    + "}";

    static public String test7 =
            "SELECT ?x WHERE {"
                    + "?x np:name ?p ."
                    + "?p ?what ?what2 ."
                    + "?p2 meta:src ?asd ." +
                    " ?"
                    + "}";

    static public String test = test7;
}
