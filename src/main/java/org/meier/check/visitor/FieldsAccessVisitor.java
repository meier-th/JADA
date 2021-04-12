package org.meier.check.visitor;

import com.github.javaparser.ast.expr.FieldAccessExpr;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;
import org.meier.model.ClassMeta;
import org.meier.model.FieldMeta;

import java.util.List;
import java.util.stream.Collectors;

public class FieldsAccessVisitor extends GenericListVisitorAdapter<FieldMeta, ClassMeta> {

    @Override
    public List<FieldMeta> visit(FieldAccessExpr n, ClassMeta classMeta) {
        String fieldName = n.resolve().asField().getName();
        return classMeta.getFields().stream().filter(field -> field.getName().equals(fieldName)).collect(Collectors.toList());
    }
}
