package com.datastax.sparql.gremlin;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.query.Query;
import org.apache.jena.sparql.core.TriplePath;
import org.apache.jena.sparql.syntax.ElementPathBlock;
import org.apache.jena.sparql.syntax.ElementVisitorBase;
import org.apache.jena.sparql.syntax.ElementWalker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class Typifier extends HashMap<String, Variable.Type> {

    private ArrayList<Triple> allTriples;

    Typifier(Query query) {
        allTriples = new ArrayList<>();
        ElementWalker.walk(query.getQueryPattern(),
                new ElementVisitorBase() {
                    public void visit(ElementPathBlock el) {
                        getTriples(el.patternElts());
                    }
                });
    }

    private void getTriples(Iterator<TriplePath> triples) {
        while (triples.hasNext()) {
            Triple triple = triples.next().asTriple();
            allTriples.add(triple);
        }
    }

    void exec() {
        boolean newVariableType;
        do { // try to assign all the possible specific types
            newVariableType = false;
            for (Triple triple : allTriples) {
                Node s, p, o;
                String sStr, pStr, oStr;
                s = triple.getSubject();
                p = triple.getPredicate();
                o = triple.getObject();
                sStr = s.toString();
                pStr = p.toString();
                oStr = o.toString();
                if (p.isVariable()) {
                    if (o.isVariable()) { // var var var
                        newVariableType = newVariableType || assignVV(sStr, pStr, oStr);
                    } else { // var var uri, impossible to know exact p type
                    }
                } else {
                    if (o.isVariable()) { // var uri var
                        newVariableType = newVariableType || assignUV(sStr, pStr, oStr);
                    } else { // var uri uri
                        newVariableType = newVariableType || assignUU(sStr, pStr);
                    }
                }
            }
        } while (newVariableType);


    }

    private boolean assignUU(String sStr, String pStr) {
        if (unknown(sStr)) {
            Variable.Type type = Variable.getSTypeFromP(pStr);
            put(sStr, type);
            return true;
        }
        return false;
    }

    private boolean assignUV(String sStr, String pStr, String oStr) {
        if (unknown(sStr)) {
            if (unknown(oStr)) {
                Variable.Type[] types = Variable.getSOTypesFromP(pStr);
                put(sStr, types[0]);
                put(oStr, types[1]);
                return true;
            } else {
                Variable.Type sType = Variable.getSTypeFromP(pStr);
                put(sStr, sType);
                return true;
            }
        } else {
            if (unknown(oStr)) {
                Variable.Type oType = Variable.getOTypeFromP(pStr);
                put(oStr, oType);
                return true;
            }
        }
        return false;
    }

    private boolean assignVV(String sStr, String pStr, String oStr) {
        if (unknown(sStr)) {
            if (unknown(pStr)) {
                if (unknown(oStr)) { // u u u
                    // can't get specific type
                } else { // u u k
                    Variable.Type[] types = Variable.getSPTypeFromOType(this, oStr);
                    if (types != null) {
                        put(sStr, types[0]);
                        put(pStr, types[1]);
                        return true;
                    }
                }
            } else {
                if (unknown(oStr)) { // u k u
                    Variable.Type[] types = Variable.getSOTypesFromPType(this, pStr);
                    put(sStr, types[0]);
                    put(oStr, types[1]);
                    return true;
                } else { // u k k
                    Variable.Type type = Variable.getSTypeFromPType(this, pStr);
                    put(sStr, type);
                    return true;
                }
            }
        } else {
            if (unknown(pStr)) {
                if (unknown(oStr)) { // k u u
                    // can't get specific type
                } else { // k u k
                    Variable.Type pType = Variable.getPTypeFromSOTypes(this, sStr, oStr);
                    if (pType != null) {
                        put(pStr, pType);
                        return true;
                    }
                }
            } else {
                if (unknown(oStr)) { // k k u
                    Variable.Type oType = Variable.getOTypeFromSPTypes(this, sStr, pStr);
                    if (oType != null) {
                        put(oStr, oType);
                        return true;
                    }
                }
            }
        }
        return false;
    }


    boolean unknown(String v) {
        return !containsKey(v);
    }

}
