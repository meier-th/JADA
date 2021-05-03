package org.meier.check.rule.visitor;

import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import org.meier.model.FieldMeta;

public class SetFieldVisitor extends GenericVisitorAdapter<Boolean, FieldMeta> {

    @Override
    public Boolean visit(AssignExpr n, FieldMeta arg) {
        return n.getTarget().asFieldAccessExpr().getName().toString().equals(arg.getName());
    }
}
