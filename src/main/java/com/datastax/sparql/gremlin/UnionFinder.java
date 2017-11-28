package com.datastax.sparql.gremlin;

import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpVisitorBase;
import org.apache.jena.sparql.algebra.op.OpUnion;

public class UnionFinder extends OpVisitorBase {

    private boolean found;
    private Op leftOp;
    private Op rightOp;

    UnionFinder() {
        this.found = false;
    }

    boolean found() {
        return found;
    }

    Op getLeftOp() {
        return leftOp;
    }

    Op getRightOp() {
        return rightOp;
    }

    @Override
    public void visit(final OpUnion opUnion) {
        found = true;
        // TODO any filter that is after union will be ignored
        leftOp = opUnion.getLeft();
        rightOp = opUnion.getRight();
    }


}
