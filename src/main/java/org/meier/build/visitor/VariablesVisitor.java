package org.meier.build.visitor;

import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.meier.bean.NameTypeBean;
import org.meier.build.util.TypeResolver;
import org.meier.model.CodeContainer;

public class VariablesVisitor extends VoidVisitorAdapter<CodeContainer> {

    @Override
    public void visit(VariableDeclarationExpr n, CodeContainer arg) {
        super.visit(n, arg);
        n.getVariables().forEach(variable -> {
            arg.addVariable(new NameTypeBean(variable.getNameAsString(), TypeResolver.getQualifiedName(variable.getType()), null));
        });
    }
}
