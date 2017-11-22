package com.datastax.sparql.gremlin;

import org.apache.jena.graph.Node;
import org.apache.jena.graph.Triple;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;
import org.apache.tinkerpop.gremlin.structure.Vertex;

import java.util.ArrayList;

public class Builder {

    public ArrayList<GraphTraversal<Vertex, ?>> transform(Triple triple, Typifier typifier) {
        Node s, p, o;
        ArrayList<GraphTraversal<Vertex,?>> result =  new ArrayList<>();
        s = triple.getSubject();
        p = triple.getPredicate();
        o = triple.getObject();
        final GraphTraversal<Vertex, ?> traversal = __.as(s.getName());
        if(p.isVariable()){
            if(o.isVariable()){ // v v v

            }
            else{ // v v u
                Object oLit = o.getLiteralValue();
                String oStr = oLit.toString();
                String pName = p.getName();
                Variable.Type pType = typifier.get(p.toString());
                switch (pType){
                    case P_FROM_NODE:
                        result.add(traversal.or(__.hasLabel(oStr),
                                __.hasId(oLit)));
                        break;
                    case P_FROM_EDGE:
                        result.add(traversal.or(__.hasLabel(oStr),
                                __.hasId(oLit),
                                __.properties().value().is(oLit)));
                        break;
                    case P_FROM_PROPERTY:


                        break;
                    /*
                    case N_VALUE:
                        result.add(traversal.properties().as(pName));
                        GraphTraversal<Vertex, ?> as = __.as(pName);
                        result.add(as.hasValue(oStr));
                        return result;
                    case NP:
                        // imposible
                        break;
                    case ANY_META_PROP:
                        result.add(traversal.properties().as(pName));
                        GraphTraversal<Vertex, ?> as2 = __.as(pName);
                        result.add(as2.hasValue(oStr));
                        return result;
                    case N_ID:
                        result.add(traversal.id().as(pName)); // TODO check this, dont know if works
                        GraphTraversal<Vertex, ?> as3 = __.as(pName);
                        result.add(as3.hasValue(oStr));
                        return result;
                    case N_LABEL:
                        result.add(traversal.label().as(pName)); // TODO check this, dont know if works
                        GraphTraversal<Vertex, ?> as4 = __.as(pName);
                        result.add(as4.hasValue(oStr));
                        return result;
                    case E_IN:
                        // imposible
                        break;
                    case E_OUT:
                        // imposible
                        break;
                    case E_ID:
                        result.add(traversal.id().as(pName)); // TODO check this, dont know if works
                        GraphTraversal<Vertex, ?> as5 = __.as(pName);
                        result.add(as5.hasValue(oStr));
                        return result;
                    case E_LABEL:
                        result.add(traversal.label().as(pName)); // TODO check this, dont know if works
                        GraphTraversal<Vertex, ?> as6 = __.as(pName);
                        result.add(as6.hasValue(oStr));
                        return result;
                    case EP:
                        result.add(traversal.properties().as(pName));
                        GraphTraversal<Vertex, ?> as7 = __.as(pName);
                        result.add(as7.hasValue(oStr));
                        return result;
                    case N_LABEL_ID:
                        break;
                        String pName = p.getName();


                        __.as(pName + "ID").


                    case E_ID_LABEL_PROPERTY:
                        break;
                    case N_VALUE_META:
                        break;
                        */
                }
            }
        }
        else{
            if(o.isVariable()){ // v u v
                String oName = o.getName();
                String pStr = p.toString();
                if (PredicateCheck.isValue(pStr)){
                    result.add(traversal.value().as(oName)); // ok
                } else if (PredicateCheck.isMeta(pStr)) { // ok
                    String metaProperty = pStr.split("#")[1];
                    result.add(traversal.values(metaProperty).as(oName));
                } else if (PredicateCheck.isNodeProperty(pStr)) { // ok
                    String property = pStr.split("#")[1];
                    result.add(traversal.properties(property).as(oName));
                } else if (PredicateCheck.isNodeLabel(pStr)){ // ok
                    result.add(traversal.label().as(oName));
                } else if (PredicateCheck.isNodeId(pStr)) { // ok
                    result.add(traversal.id().as(oName));
                } else if (PredicateCheck.isEdgeIn(pStr)) { // ok
                    result.add(traversal.inV().as(oName));
                } else if (PredicateCheck.isEdgeOut(pStr)) { // ok
                    result.add(traversal.outE().as(oName));
                } else if (PredicateCheck.isEdgeId(pStr)){ // ok
                    result.add(traversal.id().as(oName));
                } else if (PredicateCheck.isEdgeLabel(pStr)) { // ok
                    result.add(traversal.label().as(oName));
                } else if (PredicateCheck.isEdgeProperty(pStr)) { // ok
                    String property = pStr.split("#")[1];
                    result.add(traversal.values(property).as(oName));
                }
            }
            else { // v u u
                Object oLit = o.getLiteralValue();
                String oStr = oLit.toString();
                String pStr = p.toString();
                if (PredicateCheck.isValue(pStr)){ // ok
                    result.add(traversal.hasValue(oLit));
                } else if (PredicateCheck.isMeta(pStr)) { // ok
                    String metaProperty = pStr.split("#")[1];
                    result.add(traversal.values(metaProperty).is(oLit));
                } else if (PredicateCheck.isNodeProperty(pStr)) { // -
                    // imposible
                } else if (PredicateCheck.isNodeLabel(pStr)){
                    result.add(traversal.hasLabel(oStr)); // ok
                } else if (PredicateCheck.isNodeId(pStr)) { // ok
                    result.add(traversal.hasId(oLit));
                } else if (PredicateCheck.isEdgeIn(pStr)) { // -
                    // imposible
                } else if (PredicateCheck.isEdgeOut(pStr)) { // -
                    // imposible
                } else if (PredicateCheck.isEdgeId(pStr)){ // ok
                    result.add(traversal.hasId(oLit));
                } else if (PredicateCheck.isEdgeLabel(pStr)) { // ok
                    result.add(traversal.hasLabel(oStr));
                } else if (PredicateCheck.isEdgeProperty(pStr)) { // ok
                    String property = pStr.split("#")[1];
                    result.add(traversal.values(property).is(oLit));
                }
            }
        }
        return result;
    }
}
