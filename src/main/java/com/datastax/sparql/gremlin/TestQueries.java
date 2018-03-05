package com.datastax.sparql.gremlin;

public abstract class TestQueries {

    static public String test1 = "SELECT ?x WHERE {" // ok  [v[8], v[9]]
            + "?x e:out ?e ."
            + "?e e:label 'uses' ."
            + "?e ep:skill 3 ."
            + "?e e:in ?software ."
            + "?software np:name ?n ."
            + "?n n:value 'tinkergraph' ."
            + "}";

    static public String test2 = // ok [v[10]]
            "SELECT ?x WHERE {"
                    + "?x np:name ?p ."
                    + "?p ?what 'gremlin' ."
                    + "}";

    static public String test3 =
            "SELECT ?x WHERE {" +
                    "?p ?what 3 ." +
                    "?p ?np ?n ." +
                    "?n2 e:in ?n ."
                    + "}";

    static public String test7 = // ok [2001]
            "SELECT ?s WHERE {"
                    + "?x np:location ?p ."
                    + "?p ?what 'santa cruz' ."
                    + "?p meta:startTime ?s ."
                    + "}";

    static public String simpleTest1 = // OK [v[1]]
            "SELECT ?x WHERE {" +
                    "?x np:name ?p . " +
                    "?p n:value 'marko' . " +
                    "}";

    static public String simpleTestStar = // OK [v[1]]
            "SELECT ?x WHERE {" +
                    "<<?x np:name 'marko' >>. }";


    static public String starQuery = // OK TRANSLATING
            "sfsf as asda sda SELECT ?p WHERE {" +
                    "<<   ?x np:name 'asdasdas' >>   . }";
    static public String starQuery2 = // OK TRANSALTING
            "sfsf as asda sda SELECT ?p WHERE {" +
                    "<<   ?x np:name 'asdasdas' >>   . }" +
                    "<<?x np:name 'asdas'>>.";
    static public String starQuery3 = // OK TRANSALTING
            "sfsf as asda sda SELECT ?p WHERE {" +
                    "<<   ?x np:name 'asdasdas' >>   . }" +
                    "<<?x np:name 'asdas'>>;" +
                    " meta:asdasd 'asasda' ;" +
                    " meta:as 'a'   ." +
                    "?x ?p ?t ." +
                    "<< ?varw e:to ?asd>> ;" +
                    "e:label 'asd' ;" +
                    "?asd ?y ." +
                    "<< ?x e:to ?p>> .";

    static public String starQuery4 = // ok []
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
                    "}";

    static public String metaTest = // ok [2009]
            "SELECT ?startTime WHERE {" +
                    "<< ?x np:name 'daniel' >> ." +
                    "<<?x np:location 'aachen'>> ;" +
                    "meta:startTime ?startTime ." +
                    "}";

    static public String labelTest = // ok  [person, person, person, person, software, software]
            "SELECT ?label WHERE {" +
                    "?x n:label ?label ." +
                    "}";

    static public String labelTest2 = // ok  [v[1], v[7], v[8], v[9]]
            "SELECT ?x WHERE {" +
                    "?x n:label 'person' ." +
                    "}";

    static public String idTest = //ok [v[1]]
            "SELECT ?x WHERE {" +
                    "?x n:id 1 ." +
                    "}";

    static public String idVarTest = //ok [{id=1, x=v[1]}, {id=7, x=v[7]}, {id=8, x=v[8]}, {id=9, x=v[9]}, {id=10, x=v[10]}, {id=11, x=v[11]}]
            "SELECT ?id ?x WHERE {" +
                    "?x n:id ?id ." +
                    "}";


    static public String edgesTest = // ok [v[7]]

            "SELECT ?x WHERE {" +
                    "?x e:out ?e ." +
                    "?e e:in ?y ." +
                    "?e e:id 17 .}";

    static public String edgesTest2 = // ok [v[11], v[10], v[10], v[11], v[10], v[11], v[10],
            // v[11], v[10], v[11], v[10], v[10], v[11], v[11]]
            "SELECT ?y WHERE {" +
                    "<<?x e:to ?y >>.}";

    static public String edgesTest3 = // ok [v[10], v[11], v[10], v[11], v[10]]
            "SELECT ?y WHERE {" +
                    "<<?x e:to ?y >> ;" +
                    " e:label 'develops' .}";


    static public String edgesTest4 = // ok [v[7]]
            "SELECT ?x WHERE {" +
                    "<<?x e:to ?y >> ;" +
                    " ep:since 2011 .}";

    static public String edgesTest5 = //OK [16, 15, 13, 14, 19, 20, 17, 18, 22, 23, 21, 24, 25, 26]
            "SELECT ?c WHERE {" +
                    "?x e:out ?e ." +
                    "?e e:in ?y ." +
                    "?e e:id ?c .}";

    static public String edgesTest6 = //OK [uses, uses, develops, develops, uses, uses, develops, develops, uses, uses, develops, uses, uses, traverses]
            "SELECT ?s WHERE {" +
                    "<< ?x e:to ?y>> ;" +
                    "e:label ?s ." +
                    "}";

    static public String edgesTest7 = //OK [5, 4, 5, 4, 3, 3, 5, 3]
            "SELECT ?s WHERE {" +
                    "<< ?x e:to ?y>> ;" +
                    "ep:skill ?s ." +
                    "}";

    static public String vvuTest = // ok [v[1], v[1], v[7], v[7], v[8], v[9], v[10]]
            "SELECT ?x WHERE {" +
                    "<< ?x e:to ?y>> ." +
                    "?y ?p 11 ." +
                    "}";

    static public String vvuTest2 = // ok [v[11], v[11], v[11], v[11], v[11], v[11]]
            "SELECT ?y WHERE {" +
                    "?x n:label 'person' ." +
                    "<< ?x e:to ?y>> ." +
                    "?y ?p 11 ." +
                    "}";

    static public String vvuTest3 = // ok [v[1], v[7]]
            "SELECT ?x WHERE {" +
                    "?x n:label 'person' ." +
                    "<< ?x e:to ?y>> ;" +
                    "?p 4 ." +
                    "}";

    static public String vvuTest4 = // ok [v[1]]
            "SELECT ?x WHERE {" +
                    "<< ?x np:location 'san diego'>> ;" +
                    "?p 2001 ." +
                    "}";

    static public String vvuTest5 =
            "SELECT ?v WHERE {" +
                    "?v ?p 16 ." +
                    "}";

    static String vvv1 =
            "select ?p where{" +
                    "?x n:label 'person' ." +
                    "<< ?x e:to ?y >> ." +
                    "?x np:name ?p ." +
                    "?y ?bla ?p " +
                    "}";

    static String vvv2 =
            "select ?x ?y ?v where{" +
                    "?x n:label ?v ." +
                    "<< ?x e:to ?y >> ." +
                    "?y ?bla ?v " +
                    "}";

    static String manyFuncTest = // ok
            "select distinct ?n where{" +
                    "?x n:label ?v ." +
                    "<< ?x e:to ?y >>;" +
                    "ep:skill ?v2." +
                    "?x np:name ?n." +
                    "FILTER (?v = 'person' && ?v2 > 3)" +
                    "}";
    static String opt1 = // ok [{x=v[8], d=p[since->2012]}, {x=v[8], d=p[since->2012]}, {x=v[9], d=}]
            "SELECT ?x ?d WHERE {" +
                    "<< ?x e:to ?y>> ;" +
                    "ep:skill 3 ." +
                    "OPTIONAL {" +
                    "?x e:out ?e ." +
                    "?e ep:since ?d." +
                    "}}" ;

    static String manyBlocks2 = // [{s=5, x=v[1], y=v[11], r80257=e[16][1-uses->11]}, {s=4, x=v[1], y=v[10], r80257=e[15][1-uses->10]}, {x=v[1], f=v[11], t=2010, r52033=e[14][1-develops->11]}, {s=5, x=v[7], y=v[10], r80257=e[19][7-uses->10]}, {s=4, x=v[7], y=v[11], r80257=e[20][7-uses->11]}, {x=v[7], f=v[10], t=2010, r52033=e[17][7-develops->10]}, {x=v[7], f=v[11], t=2011, r52033=e[18][7-develops->11]}, {x=v[8], f=v[10], t=2012, r52033=e[21][8-develops->10]}, {s=5, x=v[9], y=v[10], r80257=e[24][9-uses->10]}]
                                // ok, UNION just working with *
            "SELECT * WHERE {" +
                    "{<< ?x e:to ?y>> ;" +
                    "ep:skill ?s ." +
                    "FILTER (?s > 3)" +
                    "} UNION {" +
                    "<<?x e:to ?f>>;" +
                    "ep:since ?t." +
                    "FILTER (?t > 2009)" +
                    "}}";

    static String optFilter =
            "SELECT ?s ?d WHERE {" +
                    "<< ?x e:to ?y>> ;" +
                    "ep:skill ?s ." +
                    "FILTER (?s > 4) ." +
                    "OPTIONAL {" +
                    "?x e:out ?e ." +
                    "?e ep:since ?d." +
                    "}" +
                    "}";

    static String distinct1 = // [v[11], v[11]] ???
            "SELECT DISTINCT ?y WHERE {" +
            "?x n:label 'person' ." +
            "<< ?x e:to ?y>> ." +
            "?y ?p 11 ." +
            "}";

    static String exist = // no se si sirve mucho
            "SELECT ?x WHERE {" +
                    "?x n:label 'person' ." +
                    "?x e:out ?e ." +
                    " FILTER NOT EXISTS { ?e e:label ?s .}" +
                    "}";

    static public String order = // OK [{s=3, n=daniel}, {s=3, n=matthias}, {s=3, n=matthias}, {s=4, n=marko}, {s=4, n=stephen}, {s=5, n=daniel}, {s=5, n=marko}, {s=5, n=stephen}]
            "SELECT ?s ?n WHERE {" +
                    "<< ?x e:to ?y >> ;" +
                    "ep:skill ?s ." +
                    "?x np:name ?p ." +
                    "?p n:value ?n ." +
                    "} ORDER BY (?s)";

    static public String orderDes = // OK [{s=5, n=daniel}, {s=5, n=marko}, {s=5, n=stephen}, {s=4, n=marko}, {s=4, n=stephen}, {s=3, n=daniel}, {s=3, n=matthias}, {s=3, n=matthias}]
            "SELECT ?s ?n WHERE {" +
                    "<< ?x e:to ?y >> ;" +
                    "ep:skill ?s ." +
                    "?x np:name ?p ." +
                    "?p n:value ?n ." +
                    "} ORDER BY DESC(?s)";


    static public String test = order;

}