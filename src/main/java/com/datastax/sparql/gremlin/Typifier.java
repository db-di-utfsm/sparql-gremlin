package com.datastax.sparql.gremlin;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.jena.sparql.core.TriplePath;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

class Typifier extends HashMap<String,Variable.Type> {

    void exec(Iterator<TriplePath> triples) {
        TripleContainer container = new TripleContainer();
        while (triples.hasNext()) {
            Triple triple = triples.next().asTriple();
            container.add(triple);
        }
        assingTypes(container);
    }

    private class TripleContainer{

        private ArrayList<Triple> vuu;
        private ArrayList<Triple> vvu;
        private ArrayList<Triple> vuv;
        private ArrayList<Triple> vvv;

        TripleContainer(){
            vuu = new ArrayList<>();
            vvu = new ArrayList<>();
            vuv = new ArrayList<>();
            vvv = new ArrayList<>();
        }

        private void add(Triple triple){
            Node s, p, o;
            s = triple.getSubject();
            p = triple.getPredicate();
            o = triple.getObject();
            if (s.isVariable()) {
                if (p.isVariable()) {
                    if (o.isVariable()) { // var var var
                        vvv.add(triple);
                    } else { // var var uri
                        vvu.add(triple);
                    }
                } else {
                    if (o.isVariable()) { // var uri var
                        vuv.add(triple);

                    } else { // var uri uri
                        vuu.add(triple);
                    }
                }
            }
        }


        ArrayList<Triple> getVuu(){
            return vuu;
        }

        ArrayList<Triple> getVvu(){
            return vvu;
        }

        ArrayList<Triple> getVuv(){
            return vuv;
        }

        ArrayList<Triple> getVvv(){
            return vvv;
        }


    }

    private void assingTypes(TripleContainer container) {
            assingTypeForVuu(container);
            assingTypeForVuv(container);
            assingTypeForVvu(container);
            // TODO assingTypeForVvv(container);
    }


    private void assingTypeForVuu(TripleContainer container) {
        String sStr, pStr;
        for (Triple triple : container.getVuu()) {
            sStr = triple.getSubject().toString();
            pStr = triple.getPredicate().toString();
            if (!containsKey(sStr)) {
                Variable.Type type = Variable.getSTypeFromP(pStr);
                put(sStr, type);
            }
        }
    }

    private void assingTypeForVuv(TripleContainer container) {
        String sStr, pStr, oStr;
        for (Triple triple : container.getVuv()) {
            sStr = triple.getSubject().toString();
            pStr = triple.getPredicate().toString();
            oStr = triple.getObject().toString();
            if (!containsKey(sStr)) {
                if (!containsKey(oStr)) {
                    Variable.Type[] types = Variable.getSOTypesFromP(pStr);
                    put(sStr, types[0]);
                    put(oStr, types[1]);
                } else {
                    Variable.Type sType = Variable.getSTypeFromP(pStr);
                    put(sStr, sType);
                }
            } else{
                if (!containsKey(oStr)) {
                    Variable.Type oType = Variable.getOTypeFromP(pStr);
                    put(oStr, oType);
                } else {
                    // everything is known
                }
            }
        }

    }

    private void assingTypeForVvu(TripleContainer container){
        String sStr, pStr;
        for (Triple triple : container.getVvu()){
            sStr = triple.getSubject().toString();
            pStr = triple.getPredicate().toString();
            if (!containsKey(sStr)) {
                if (containsKey(pStr)) {
                    Variable.Type sType = Variable.getSTypeFromPType(this, pStr);
                    put(sStr,sType);
                }
            } else {
                if (!containsKey(pStr)) {
                    Variable.Type pType = Variable.getPTypeFromSType(this, sStr);
                    put(pStr, pType);
                }
            }
        }
    }

    private void assingTypeForVvv(TripleContainer container){
        String sStr, pStr, oStr;
        for (Triple triple : container.getVvv()) {
            sStr = triple.getSubject().toString();
            pStr = triple.getPredicate().toString();
            oStr = triple.getObject().toString();
            // TODO this case is something to worry about
            if (!containsKey(sStr)) {
                if (!containsKey(pStr)) {
                    if (containsKey(oStr)) { // unknow unknow know
                        //Variable.Type oType = typesMap.get(oStr);
                        //Variable.Type types[] = Variable.getSPTypeFromOType(typesMap, oStr);
                    }
                } else { // if p is known, p and o are known
                    if (!containsKey(oStr)) { // unknow know unknow
                        continue;   // if this happens always until the analysis ends,
                        // then this triple will match ALL
                    } else { // unknow know know

                    }
                }
            } else {
                if (!containsKey(pStr)) {
                    if (!containsKey(oStr)) { // know unknow unknow
                        continue;   // if this happens always until the analysis ends,
                        // then this triple will match ALL
                    } else { // know unknow know

                    }

                } else {
                    if (!containsKey(oStr)) { // know know unknow
                        continue;   // if this happens always until the analysis ends,
                        // then this triple will match ALL
                    } else { // know know know
                        continue;
                    }
                }
            }
        }
    }

}
