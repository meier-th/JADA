package org.meier.check.rule.visitor;

import com.github.javaparser.ast.expr.ObjectCreationExpr;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;
import org.meier.check.rule.util.ClassMetaInfo;
import org.meier.model.ClassMeta;

import java.util.List;

public class ObjectCreationVisitor extends GenericListVisitorAdapter<Object, ClassMeta> {

    @Override
    public List<Object> visit(ObjectCreationExpr n, ClassMeta arg) {
        List<Object> objs = super.visit(n, arg);
        if (arg.getFullName().endsWith("."+n.getType().asString()))
            objs.add(new Object());
        else {
            if (ClassMetaInfo.getAllDescendants(arg).stream().anyMatch(cls -> cls.getFullName().endsWith("."+n.getType().asString())))
                objs.add(new Object());
        }
        return objs;
    }
}
