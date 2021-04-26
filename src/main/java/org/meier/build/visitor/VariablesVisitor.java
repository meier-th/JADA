package org.meier.build.visitor;

import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.meier.bean.NameTypeBean;
import org.meier.build.util.TypeResolver;
import org.meier.model.CodeBlockMeta;

public class VariablesVisitor extends VoidVisitorAdapter<CodeBlockMeta> {

    @Override
    public void visit(VariableDeclarationExpr n, CodeBlockMeta arg) {
        super.visit(n, arg);
        n.getVariables().forEach(variable -> {
            arg.addVariable(new NameTypeBean(variable.getNameAsString(), TypeResolver.getQualifiedName(variable.getType()), null));
        });
    }
}
