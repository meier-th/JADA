package org.meier.build.visitor;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;
import org.meier.model.ClassMeta;
import org.meier.model.FieldMeta;
import org.meier.model.MetaHolder;

import java.util.Collections;
import java.util.List;

public class FieldsAccessVisitor extends GenericListVisitorAdapter<FieldMeta, Void> {

    @Override
    public List<FieldMeta> visit(FieldAccessExpr n, Void arg) {
        try {
            Expression scope = n.getScope();
            String ownerClass = scope.calculateResolvedType().describe();
            String fieldName = n.resolve().isEnumConstant() ? n.resolve().asEnumConstant().toString() : n.resolve().asField().getName();
            ClassMeta owner = MetaHolder.getClass(ownerClass);
            if (owner != null) {
                FieldMeta field = owner.getField(fieldName);
                return field == null ? Collections.emptyList() : List.of(owner.getField(fieldName));
            }
            return Collections.emptyList();
        } catch (Exception error) {
            return Collections.emptyList();
        }
    }
}
