/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.datastax.sparql.gremlin;

import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.Syntax;
import org.apache.jena.sparql.algebra.Algebra;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpVisitorBase;
import org.apache.jena.sparql.algebra.OpWalker;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.algebra.op.OpFilter;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementVisitorBase;
import org.apache.jena.sparql.syntax.ElementWalker;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.apache.tinkerpop.gremlin.structure.Graph;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.Arrays;
import java.util.List;

@Deprecated
/**
 * @author Daniel Kuppitz (http://gremlin.guru)
 */
// TODO: implement OpVisitor, don't extend OpVisitorBase
public class SparqlToGremlinCompiler extends OpVisitorBase {

    private GraphTraversal<Vertex, ?> traversal;

    private SparqlToGremlinCompiler(final GraphTraversal<Vertex, ?> traversal) {
        this.traversal = traversal;
    }

    private SparqlToGremlinCompiler(final GraphTraversalSource g) {
        this(g.V());
    }

    GraphTraversal<Vertex, ?> convertToGremlinTraversal(final Query query) {
        // TEST -----------------------
        //HashMap<String,Variable.Type> typesMap = new HashMap<>();

        String testQueryString = TestQueries.test;
        Query testQuery = QueryFactory.create(Prefixes.preamblePrepend(testQueryString), Syntax.syntaxSPARQL);
        final Typifier typifier = new Typifier();
        ElementWalker.walk(testQuery.getQueryPattern(),
                new ElementVisitorBase() {
                    // ...when it's a block of triples...
                    public void visit(ElementPathBlock el) {
                        typifier.exec(el.patternElts());
                        /*
                        ArrayList<String> newAddedVariables;
                        do {
                            Iterator<TriplePath> triples = el.patternElts();
                            newAddedVariables = new ArrayList<>();
                            while (triples.hasNext()) {
                                Triple triple = triples.next().asTriple();
                                Node s, p, o;
                                String sStr, pStr, oStr;
                                s = triple.getSubject();
                                sStr = s.toString();
                                p = triple.getPredicate();
                                pStr = p.toString();
                                o = triple.getObject();
                                oStr = o.toString();
                                if (s.isVariable()) {
                                    if (p.isVariable()) {
                                        if (o.isVariable()) { // var var var

                                            // TODO this case is something to worry about
                                            if (!typesMap.containsKey(sStr)) {
                                                if (!typesMap.containsKey(pStr)) {
                                                    if (!typesMap.containsKey(oStr)){ // unknow unknow unknow
                                                        continue;   // if this happens always until the analysis ends,
                                                                    // then this triple will match ALL
                                                    }
                                                    else{ // unknow unknow know
                                                        //Variable.Type oType = typesMap.get(oStr);
                                                        Variable.Type types[] = Variable.getSPTypeFromOType(typesMap, oStr);


                                                    }

                                                } else {
                                                    if (!typesMap.containsKey(oStr)){ // unknow know unknow
                                                        continue;   // if this happens always until the analysis ends,
                                                        // then this triple will match ALL
                                                    }
                                                    else{ // unknow know know

                                                    }
                                                }
                                            } else {
                                                if (!typesMap.containsKey(pStr)) {
                                                    if (!typesMap.containsKey(oStr)){ // know unknow unknow
                                                        continue;   // if this happens always until the analysis ends,
                                                        // then this triple will match ALL
                                                    }
                                                    else{ // know unknow know

                                                    }

                                                } else {
                                                    if (!typesMap.containsKey(oStr)){ // know know unknow
                                                        continue;   // if this happens always until the analysis ends,
                                                        // then this triple will match ALL
                                                    }
                                                    else{ // know know know
                                                        continue;
                                                    }
                                                }
                                            }
                                        } else { // var var uri
                                            if (!typesMap.containsKey(sStr)) {
                                                if (!typesMap.containsKey(pStr)) {
                                                    continue;
                                                } else {
                                                    Variable.Type sType = Variable.getSTypeFromPType(typesMap, pStr);
                                                    typesMap.put(sStr,sType);
                                                    newAddedVariables.add(sStr);
                                                }
                                            } else {
                                                if (!typesMap.containsKey(pStr)) {
                                                    Variable.Type pType = Variable.getPTypeFromSType(typesMap,sStr);
                                                    typesMap.put(pStr, pType);
                                                    newAddedVariables.add(pStr);
                                                } else {
                                                    continue;
                                                }
                                            }
                                        }
                                    } else {
                                        if (o.isVariable()) { // var uri var
                                            // check p to know both typesMap
                                            if (!typesMap.containsKey(sStr)) {
                                                if (!typesMap.containsKey(oStr)) {
                                                    Variable.Type[] types = Variable.getSOTypesFromP(p);
                                                    typesMap.put(sStr, types[0]);
                                                    typesMap.put(oStr, types[1]);
                                                    newAddedVariables.add(sStr);
                                                    newAddedVariables.add(oStr);
                                                } else {
                                                    Variable.Type sType = Variable.getSTypeFromP(p);
                                                    typesMap.put(sStr, sType);
                                                    newAddedVariables.add(sStr);
                                                }
                                            }
                                        } else { // var uri uri
                                            if (!typesMap.containsKey(sStr)) {
                                                Variable.Type type = Variable.getSTypeFromP(p);
                                                typesMap.put(sStr, type);
                                                newAddedVariables.add(sStr);
                                            }

                                        }
                                    }
                                }/* else { // TODO erase all this
                                    if (p.isVariable()) {
                                        if (o.isVariable()) { uri var var
                                            // IMPOSSIBLE
                                        } else { uri var uri
                                            // IMPOSSIBLE
                                        }
                                    } else {
                                        if (o.isVariable()) { uri uri var
                                            // IMPOSSIBLE
                                        } else { uri uri uri
                                            // USELESS
                                        }
                                    }
                                }

                            }
                        } while(!newAddedVariables.isEmpty());
                        */
                    }
                }
        );
        // This assumes that there are not triples with just variables
        // TODO add isVariable and check Object if triples with 3 variable are going to be considered
        /*
        unknowTriples.forEach((uTriple) -> {
            Node unknowS = uTriple.getSubject();
            Node unknowP = uTriple.getPredicate();
            if(!typesMap.containsKey(unknowS.toString())){
                typesMap.put(unknowS.toString(), Variable.Type.UNKNOW_S);

            }
            if (!typesMap.containsKey(unknowP.toString())) {
                typesMap.put(unknowP.toString(), Variable.Type.UNKNOW_P);
            }
        });*/
        // END TEST --------------------------------
        final Op op = Algebra.compile(query);
        OpWalker.walk(op, this);
        if (!query.isQueryResultStar()) {
            final List<String> vars = query.getResultVars();
            switch (vars.size()) {
                case 0:
                    throw new IllegalStateException();
                case 1:
                    if (query.isDistinct()) {
                        traversal = traversal.dedup(vars.get(0));
                    }
                    traversal = traversal.select(vars.get(0));
                    break;
                case 2:
                    if (query.isDistinct()) {
                        traversal = traversal.dedup(vars.get(0), vars.get(1));
                    }
                    traversal = traversal.select(vars.get(0), vars.get(1));
                    break;
                default:
                    final String[] all = new String[vars.size()];
                    vars.toArray(all);
                    if (query.isDistinct()) {
                        traversal = traversal.dedup(all);
                    }
                    final String[] others = Arrays.copyOfRange(all, 2, vars.size());
                    traversal = traversal.select(vars.get(0), vars.get(1), others);
                    break;
            }
        } else {
            if (query.isDistinct()) {
                traversal = traversal.dedup();
            }
        }
        return traversal;
    }

    private static GraphTraversal<Vertex, ?> convertToGremlinTraversal(final GraphTraversalSource g, final Query query) {
        return new SparqlToGremlinCompiler(g).convertToGremlinTraversal(query);
    }

    public static GraphTraversal<Vertex, ?> convertToGremlinTraversal(final Graph graph, final String query) {
        return convertToGremlinTraversal(graph.traversal(), QueryFactory.create(Prefixes.prepend(query), Syntax.syntaxSPARQL));
    }

    public static GraphTraversal<Vertex, ?> convertToGremlinTraversal(final GraphTraversalSource g, final String query) {
        return convertToGremlinTraversal(g, QueryFactory.create(Prefixes.prepend(query), Syntax.syntaxSPARQL));
    }

    @Override
    public void visit(final OpBGP opBGP) {
        final List<Triple> triples = opBGP.getPattern().getList();
        final Traversal[] matchTraversals = new Traversal[triples.size()];
        int i = 0;
        for (final Triple triple : triples) {
            matchTraversals[i++] = TraversalBuilder.transform(triple);
        }
        traversal = traversal.match(matchTraversals);
    }

    @Override
    public void visit(final OpFilter opFilter) {
        //noinspection ResultOfMethodCallIgnored
        opFilter.getExprs().getList().stream().
                map(WhereTraversalBuilder::transform).
                reduce(traversal, GraphTraversal::where);
    }
}
