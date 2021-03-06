package com.datastax.sparql.ops;

import com.datastax.sparql.gremlin.FilterBuilder;
import com.datastax.sparql.gremlin.Typifier;
import org.apache.jena.sparql.algebra.op.OpFilter;
import org.apache.jena.sparql.expr.Expr;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.__;

import java.util.ArrayList;

public class OpFilterContainer extends OpContainer {

    OpFilterContainer(OpFilter op, Typifier typifier) {
        super(op, typifier);
    }

    @Override
    ArrayList<Traversal> getTraversals() {
        Traversal traversal;
        ArrayList<Traversal> whereTraversalsList = new ArrayList<>();
        for (Expr expr : ((OpFilter) op).getExprs().getList()) {
            if (expr != null) {
                traversal = __.where(FilterBuilder.transform(expr));
                whereTraversalsList.add(traversal);
            }
        }
        return whereTraversalsList;
    }
}
