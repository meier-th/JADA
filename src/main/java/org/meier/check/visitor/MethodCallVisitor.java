package org.meier.check.visitor;

import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.MethodCallExpr;
import com.github.javaparser.ast.expr.MethodReferenceExpr;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;
import com.github.javaparser.resolution.declarations.ResolvedMethodDeclaration;
import org.meier.bean.CalledMethodBean;
import org.meier.model.ClassMeta;
import org.meier.model.MetaHolder;
import org.meier.model.MethodMeta;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MethodCallVisitor extends GenericListVisitorAdapter<CalledMethodBean, Void> {

    @Override
    public List<CalledMethodBean> visit(MethodCallExpr n, Void arg) {
        try {
            return List.of(resolveMethod(n.getScope().orElse(null), n.resolve()));
        } catch (Exception error) {
            // somelogs
            return Collections.emptyList();
        }
    }

    @Override
    public List<CalledMethodBean> visit(MethodReferenceExpr n, Void arg) {
        return List.of(resolveMethod(n.getScope(), n.resolve()));
    }

    private CalledMethodBean resolveMethod(Expression scope, ResolvedMethodDeclaration method) {
        String className = scope == null ? null : scope.calculateResolvedType().describe();
        String fullMethodName = method.getQualifiedName();
        return new CalledMethodBean(className, fullMethodName);
    }

}
