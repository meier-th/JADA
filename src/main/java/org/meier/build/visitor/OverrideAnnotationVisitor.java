package org.meier.build.visitor;

import com.github.javaparser.ast.expr.MarkerAnnotationExpr;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;

public class OverrideAnnotationVisitor extends GenericVisitorAdapter<Boolean, Void> {

    @Override
    public Boolean visit(MarkerAnnotationExpr n, Void arg) {
        return (n.toString().equals("@Override"));
    }
}
