package org.meier.check.visitor;

import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.meier.bean.NameTypeBean;

import java.util.List;

public class NameTypeVisitor extends VoidVisitorAdapter<List<NameTypeBean>> {

    @Override
    public void visit(VariableDeclarator n, List<NameTypeBean> arg) {
        super.visit(n, arg);
        if (n.getType().isPrimitiveType()) {
            arg.add(new NameTypeBean(n.getNameAsString(),n.getType().asPrimitiveType().asString()));
        } else {
            String fullClassName = n.getType().resolve().asReferenceType().getQualifiedName();
            arg.add(new NameTypeBean(n.getNameAsString(), fullClassName));
        }
    }
}
