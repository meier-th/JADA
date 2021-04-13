package org.meier.check.visitor;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;
import com.github.javaparser.resolution.UnsolvedSymbolException;
import org.meier.model.ClassMeta;
import org.meier.model.FieldMeta;
import org.meier.model.MetaHolder;

import java.util.List;
import java.util.stream.Collectors;

public class FieldsAccessVisitor extends GenericListVisitorAdapter<FieldMeta, ClassMeta> {

    @Override
    public List<FieldMeta> visit(FieldAccessExpr n, ClassMeta classMeta) {
        String ownerClass = "";
        Expression scope = n.getScope();
        try {
            ownerClass = scope.calculateResolvedType().describe();
        } catch (UnsolvedSymbolException error) {
            // some logs
        }
        String fieldName = n.resolve().isEnumConstant() ? n.resolve().asEnumConstant().toString() : n.resolve().asField().getName();
        return MetaHolder.getClass(ownerClass).getFields().stream().filter(field -> field.getName().equals(fieldName)).collect(Collectors.toList());
    }
}
