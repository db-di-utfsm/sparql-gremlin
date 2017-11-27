package com.datastax.sparql.gremlin;

import org.apache.jena.sparql.algebra.OpVisitorBase;
import org.apache.jena.sparql.algebra.op.OpBGP;
import org.apache.jena.sparql.algebra.op.OpFilter;
import org.apache.jena.sparql.algebra.op.OpLeftJoin;
import org.apache.jena.sparql.algebra.op.OpUnion;

public class VisitorAnalyzer extends OpVisitorBase {

    @Override
    public void visit(final OpBGP opBGP) {
        System.out.println(opBGP);
        System.out.println("BGP");
    }

    @Override
    public void visit(final OpFilter opFilter) {
        System.out.println(opFilter);
        System.out.println("FILTER");
    }

    @Override
    public void visit(final OpUnion opUnion) {
        System.out.println(opUnion);
        System.out.println("UNION");
    }

    @Override
    public void visit(final OpLeftJoin opLeftJoin) {
        System.out.println(opLeftJoin);
        System.out.println("OPCIONAL");
    }
}
