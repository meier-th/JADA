package org.meier.build.visitor;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import org.meier.model.ClassMeta;
import org.meier.model.FieldMeta;
import org.meier.model.MetaHolder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class FieldsAccessVisitor extends GenericListVisitorAdapter<FieldMeta, Void> {

    @Override
    public List<FieldMeta> visit(FieldAccessExpr n, Void arg) {
        try {
            Expression scope = n.getScope();
            String ownerClass = scope.calculateResolvedType().describe();
            String fieldName = n.resolve().isEnumConstant() ? n.resolve().asEnumConstant().toString() : n.resolve().asField().getName();
            ClassMeta owner =  MetaHolder.getClass(ownerClass);
            if (owner != null) {
                return owner.getFields().stream().filter(field -> field.getName().equals(fieldName)).collect(Collectors.toList());
            }
            return Collections.emptyList();
        } catch (UnsolvedSymbolException | UnsupportedOperationException error) {
            return Collections.emptyList();
        }
    }
}
