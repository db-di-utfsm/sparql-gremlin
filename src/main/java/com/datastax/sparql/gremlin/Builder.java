package com.datastax.sparql.gremlin;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.ArrayList;

public class Builder {

    public ArrayList<GraphTraversal<Vertex, ?>> transform(Triple triple, Typifier typifier) {

        ArrayList<GraphTraversal<Vertex,?>> result =  new ArrayList<>();
        Node s, p, o;
        Object oLit;
        String sStr, pStr, oStr;
        Variable.Type sType, pType, oType;
        s = triple.getSubject();
        p = triple.getPredicate();
        o = triple.getObject();
        sStr = s.toString();
        pStr = p.toString();
        oStr = o.toString();
        final GraphTraversal<Vertex, ?> traversal = __.as(s.getName());
        if(p.isVariable()){
            if(o.isVariable()){ // v v v
                String oName = o.getName();
                if(typifier.unknown(sStr)){
                    if(typifier.unknown(pStr)) {
                        if(typifier.unknown(oStr)) {

                        }else{

                        }
                    }else {
                        if (typifier.unknown(oStr)) {

                        } else {

                        }
                    }
                }else{
                    sType = typifier.get(sStr);
                    if(typifier.unknown(pStr)){
                        if(typifier.unknown(oStr)) { // k u u

                        }else{ // k u k
                            oType = typifier.get(oStr);
                            result.add(getVVVFromSOTypes(traversal,sType,oType,oName));
                        }

                    }
                    else{
                        pType = typifier.get(pStr);
                        if(typifier.unknown(oStr)) { // k k u
                            result.add(getVVVFromPType(traversal,pStr,pType,oStr));
                        }else{ // k k k
                            result.add(getVVVFromPType(traversal,pStr,pType,oStr));
                        }
                    }
                }
            }
            else{ // v v u
                oLit = o.getLiteralValue();
                oStr = oLit.toString();
                if(typifier.unknown(sStr)){
                    if(typifier.unknown(pStr)) {
                        // TODO - NOT WORKING, USSUME ONE OF THEM IS KNOWN? check again
                        result.add(traversal.or(
                                __.hasId(oLit),
                                __.hasLabel(oStr),
                                __.properties().value().is(oLit),
                                __.hasValue(oLit)));
                    }else{
                        pType = typifier.get(pStr);
                        result.add(getVVUFromPType(traversal, pStr,  pType, oStr, oLit));
                    }
                } else{
                    if(typifier.unknown(pStr)){
                        sType = typifier.get(sStr);
                        result.add(getVVUFromSType(traversal, sType, oStr, oLit));
                    }
                    else{
                        pType = typifier.get(pStr);
                        result.add(getVVUFromPType(traversal, pStr, pType,oStr, oLit));
                    }
                }
            }
        }
        else{
            pType = PredicateCheck.getType(pStr);
            if(o.isVariable()){ // v u v
                String oName = o.getName();
                result.add(getVUVFromPType(traversal, pType, pStr, oName));
            }
            else { // v u u
                oLit = o.getLiteralValue();
                oStr = oLit.toString();
                result.add(getVUUFromPType(traversal, pType, pStr, oLit, oStr));
            }
        }
        return result;
    }

    GraphTraversal<Vertex, ?> getVVVFromSOTypes(GraphTraversal<Vertex, ?> traversal, Variable.Type sType, Variable.Type oType, String oName) {
            switch (sType){
                case NODE:
                    switch (oType) {
                        case EDGE:
                            return traversal.outE().as(oName);
                        case PROPERTY:
                            return traversal.properties().as(oName);
                        case VALUE:
                            return traversal.union(__.label(), __.id()).as
                    }
                case EDGE:
                    switch (oType) {
                        case NODE:
                            return traversal.inV().as(oName);
                        case VALUE:
                            return traversal.union(__.label(), __.id()).as
                    }

                case PROPERTY:
                    return traversal.union(__.value(),__.properties().value()).as(oName);

            }

    }

    GraphTraversal<Vertex, ?> getVVVFromPType(GraphTraversal<Vertex, ?> traversal, String pStr, Variable.Type pType, String oName) {
        return getVUVFromPType(traversal,pType,pStr,oName);
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

    GraphTraversal<Vertex, ?> getVVUFromSType(GraphTraversal<Vertex, ?> traversal, Variable.Type sType,
                                              String oStr, Object oLit) {
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

    GraphTraversal<Vertex, ?> getVVUFromPType(GraphTraversal<Vertex, ?> traversal, String pStr,
                                              Variable.Type pType, String oStr, Object oLit){
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
