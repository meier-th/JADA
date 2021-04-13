package org.meier.check.visitor;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.meier.bean.NameTypeBean;
import org.meier.check.util.TypeResolver;

import java.util.List;

public class NameTypeVisitor extends VoidVisitorAdapter<List<NameTypeBean>> {

    @Override
    public void visit(VariableDeclarator n, List<NameTypeBean> arg) {
        super.visit(n, arg);
        String type = TypeResolver.getQualifiedName(n.getType());
        arg.add(new NameTypeBean(n.getNameAsString(), type));
    }
}
