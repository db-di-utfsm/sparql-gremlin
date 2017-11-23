package com.datastax.sparql.gremlin;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.ArrayList;

public class Builder {

    public ArrayList<GraphTraversal<Vertex, ?>> transform(Triple triple, Typifier2 typifier) {

        ArrayList<GraphTraversal<Vertex,?>> result =  new ArrayList<>();
        Node s, p, o;
        Object oLit;
        String sStr, pStr, oStr;
        s = triple.getSubject();
        p = triple.getPredicate();
        o = triple.getObject();
        sStr = s.toString();
        pStr = p.toString();
        final GraphTraversal<Vertex, ?> traversal = __.as(s.getName());
        if(p.isVariable()){
            if(o.isVariable()){ // v v v

            }
            else{ // v v u
                oLit = o.getLiteralValue();
                oStr = oLit.toString();
                if(typifier.unknown(sStr)){
                    if(typifier.unknown(pStr)) {
                        // TODO TEST THIS
                        result.add(traversal.or(
                                __.hasId(oLit),
                                __.hasLabel(oStr),
                                __.properties().value().is(oLit),
                                __.hasValue(oLit)));
                    }else{
                        result.add(getVVUFromPType(typifier, traversal, pStr, oStr, oLit));
                    }
                } else{
                    if(typifier.unknown(pStr)){
                        result.add(getVVUFromSType(typifier, traversal, sStr, oStr, oLit));
                    }
                    else{
                        result.add(getVVUFromPType(typifier, traversal, pStr, oStr, oLit));
                    }
                }
            }
        }
        else{
            if(o.isVariable()){ // v u v
                String oName = o.getName();
                Variable.Type pType = PredicateCheck.getType(pStr);
                result.add(getVUVFromPType(traversal, pType, pStr, oName));
            }
            else { // v u u
                oLit = o.getLiteralValue();
                oStr = oLit.toString();
                Variable.Type pType = PredicateCheck.getType(pStr);
                result.add(getVUUFromPType(traversal, pType, pStr, oLit, oStr));
            }
        }
        return result;
    }

    GraphTraversal<Vertex, ?> getVUVFromPType(GraphTraversal<Vertex, ?> traversal, Variable.Type pType, String pStr,
                                              String oName) {
        String property;
        switch (pType){
            case N_VALUE:
                return traversal.value().as(oName);
            case META:
                String metaProperty = pStr.split("#")[1];
                return traversal.values(metaProperty).as(oName);
            case N_ID:
                return traversal.id().as(oName);
            case E_ID:
                return traversal.id().as(oName);
            case N_LABEL:
                return traversal.label().as(oName);
            case E_LABEL:
                return traversal.label().as(oName);
            case NP:
                property = pStr.split("#")[1];
                return traversal.properties(property).as(oName);
            case EP:
                property = pStr.split("#")[1];
                return traversal.values(property).as(oName);
            case E_IN:
                return traversal.inV().as(oName);
            default: //case E_OUT:
                return traversal.outE().as(oName);
        }
    }

    GraphTraversal<Vertex, ?> getVUUFromPType(GraphTraversal<Vertex, ?> traversal, Variable.Type pType, String pStr,
                                              Object oLit, String oStr) {
        String property;
        switch (pType){
            case N_VALUE:
                return traversal.hasValue(oLit);
            case META:
                String metaProperty = pStr.split("#")[1];
                return traversal.values(metaProperty).is(oLit);
            case N_ID:
                return traversal.hasId(oLit);
            case E_ID:
                return traversal.hasId(oLit);
            case N_LABEL:
                return traversal.hasLabel(oStr);
            case E_LABEL:
                return traversal.hasLabel(oStr);
            default: //case EP:
                property = pStr.split("#")[1];
                return traversal.values(property).is(oLit);
            // Impossible:
            // E_IN
            // E_OUT
            // NP
        }
    }

    GraphTraversal<Vertex, ?> getVVUFromSType(Typifier2 typifier, GraphTraversal<Vertex, ?> traversal, String sStr,
                                              String oStr, Object oLit) {
        Variable.Type sType = typifier.get(sStr);
        switch (sType){
            case NODE:
                return traversal.or(__.hasLabel(oStr),
                        __.hasId(oLit));
            case EDGE:
                return traversal.or(__.hasLabel(oStr),
                        __.hasId(oLit),
                        __.properties().value().is(oLit));
            default: // case PROPERTY:
                return traversal.or(__.hasValue(oLit),
                        __.properties().value().is(oLit));
        }
    }

    GraphTraversal<Vertex, ?> getVVUFromPType(Typifier2 typifier, GraphTraversal<Vertex, ?> traversal, String pStr,
                                             String oStr, Object oLit){
        Variable.Type pType = typifier.get(pStr);
        switch (pType){
            case N_VALUE:
                return traversal.hasValue(oLit);
            case META:
                String metaProperty = pStr.split("#")[1];
                return traversal.values(metaProperty).is(oLit);
            case N_ID:
                return traversal.hasId(oLit);
            case E_ID:
                return traversal.hasId(oLit);
            case N_LABEL:
                return traversal.hasLabel(oStr);
            case E_LABEL:
                return traversal.hasLabel(oStr);
            default: // case EP:
                String property = pStr.split("#")[1];
                return traversal.values(property).is(oLit);
            // Impossible:
            // E_IN
            // E_OUT
            // NP
        }
    }
}
