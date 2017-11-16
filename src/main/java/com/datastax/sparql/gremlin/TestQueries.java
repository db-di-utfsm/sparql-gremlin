package com.datastax.sparql.gremlin;

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
                    + "?p ?what 1 ."
                    + "?p2 meta:src ?asd ."
                    + "}";

    static public String simpleTest1 =
            "SELECT ?p WHERE {" +
                    "?x np:name ?p . " +
                    "?p n:value 'marko' . " +
                    "}";

    static public String starQuery =
            "sfsf as asda sda SELECT ?p WHERE {" +
                    "<<   ?x np:name 'asdasdas' >>   . }";
    static public String starQuery2 =
            "sfsf as asda sda SELECT ?p WHERE {" +
                    "<<   ?x np:name 'asdasdas' >>   . }" +
                    "<<?x np:name 'asdas'>>.";
    static public String starQuery3 =
            "sfsf as asda sda SELECT ?p WHERE {" +
                    "<<   ?x np:name 'asdasdas' >>   . }" +
                    "<<?x np:name 'asdas'>>;" +
                    " meta:asdasd 'asasda' ;" +
                    " meta:as 'a'   ." +
                    "?x ?p ?t ." +
                    "<< ?varw e:to ?asd>> ;" +
                    "e:label 'asd' ." +
                    "?asd ?y ." +
                    "<< ?x e:to ?p>> ;";

    static public String starQuery4 =
            "SELECT ?p WHERE {" +
                    "<<   ?x np:name 'asdasdas' >>." +
                    "<<?x np:name 'asdas'>>;" +
                    " meta:asdasd 'asasda' ;" +
                    " meta:as 'a'   ." +
                    "?x ?p ?t ." +
                    "<< ?a np:this ?b >> ;" +
                    "meta:asd 'ASD' ." +
                    "<< ?x e:to ?y >> ;" +
                    " ep:cssssss ?p ." +
                    "}"  ;

    static public String test = starQuery4;
}