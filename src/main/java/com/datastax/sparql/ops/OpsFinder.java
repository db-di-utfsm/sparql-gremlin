package com.datastax.sparql.ops;

import com.datastax.sparql.gremlin.Randomizer;
import com.datastax.sparql.gremlin.Typifier;
import org.apache.jena.sparql.algebra.Op;
import org.apache.jena.sparql.algebra.OpVisitorBase;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.algebra.op.OpFilter;
import org.apache.jena.sparql.algebra.op.OpLeftJoin;
import org.apache.tinkerpop.gremlin.process.traversal.Traversal;

import java.util.ArrayList;

public class OpsFinder extends OpVisitorBase {

    private final ArrayList<OpContainer> containersList;
    private final Typifier typifier;
    private final boolean isRightUnionOps;

    public OpsFinder(Typifier typifier, boolean isRightUnionOps) {
        containersList = new ArrayList<>();
        this.typifier = typifier;
        this.isRightUnionOps = isRightUnionOps;
    }

    public Traversal[] getTraversalsArray() {
        if(isRightUnionOps){
            Randomizer.setDuplicatedVariable();
        }
        ArrayList<Traversal> traversals = new ArrayList<>();
        containersList.forEach((container) -> traversals.addAll(container.getTraversals()));
        int size = traversals.size();
        final Traversal[] matchTraversalsArray = new Traversal[size];
        for (int i = 0; i < size; i++) { // because match needs an array
            matchTraversalsArray[i] = traversals.get(i);
        }
        return matchTraversalsArray;
    }

    @Override
    public void visit(final OpBGP opBGP) {
        containersList.add(new OpBGPContainer(opBGP, typifier));
    }

    @Override
    public void visit(final OpFilter opFilter) {
        containersList.add(new OpFilterContainer(opFilter, typifier));
    }

    @Override
    public void visit(final OpLeftJoin opLeftJoin) {
        for (OpContainer container : containersList) {
            Op optionalBGP = opLeftJoin.getRight();
            if (optionalBGP.toString().equals(container.op.toString())) {
                containersList.set(containersList.indexOf(container),
                        new OpBGPOptionalContainer(optionalBGP, typifier));
                return;
            }
        }
    }

}
