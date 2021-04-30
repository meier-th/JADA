package org.meier.check.rule.util;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.stmt.IfStmt;
import org.meier.model.ClassMeta;
import org.meier.model.FieldMeta;

import java.util.HashSet;
import java.util.Set;

public final class ClassMetaInfo {

    public static Set<ClassMeta> getAllDescendants(ClassMeta cls) {
        Set<ClassMeta> descendants = new HashSet<>();
        descendants.addAll(cls.getProjectExtendedBy());
        descendants.addAll(cls.getProjectImplementedBy());
        cls.getProjectExtendedBy().forEach(ext -> descendants.addAll(getAllDescendants(ext)));
        cls.getProjectImplementedBy().forEach(ext -> descendants.addAll(getAllDescendants(ext)));
        return descendants;
    }

    public static Set<ClassMeta> getAllAncestors(ClassMeta cls) {
        Set<ClassMeta> ancestors = new HashSet<>();
        ancestors.addAll(cls.getProjectExtendedClasses());
        ancestors.addAll(cls.getProjectImplementedInterfaces());
        cls.getProjectExtendedClasses().forEach(parent -> ancestors.addAll(getAllAncestors(parent)));
        cls.getProjectImplementedInterfaces().forEach(parent -> ancestors.addAll(getAllAncestors(parent)));
        return ancestors;
    }

    public static boolean hasBuilderSetter(ClassMeta cls) {
        return cls.getMethods().stream().anyMatch(meth -> {
            boolean returnsItself = meth.getFullQualifiedReturnType().equals(cls.getFullName());
            if (!returnsItself)
                return false;
            return cls.getMethods().stream().anyMatch(field -> meth.getShortName().equalsIgnoreCase("set"+field.getShortName()));
        });
    }

    public static boolean isNullComparison(IfStmt ifStmt, FieldMeta field) {
        if (!ifStmt.getCondition().isBinaryExpr())
            return false;
        BinaryExpr comparison = ifStmt.getCondition().asBinaryExpr();
        if (!comparison.getOperator().equals(BinaryExpr.Operator.EQUALS))
            return false;
        return comparison.getLeft().isNullLiteralExpr() && comparison.getRight().isNameExpr() && comparison.getRight().asNameExpr().toString().equals(field.getName()) ||
                comparison.getRight().isNullLiteralExpr() && comparison.getLeft().isNameExpr() && comparison.getLeft().asNameExpr().toString().equals(field.getName());
    }

    public static boolean isNotNullComparison(IfStmt ifStmt, FieldMeta field) {
        if (!ifStmt.getCondition().isBinaryExpr())
            return false;
        BinaryExpr comparison = ifStmt.getCondition().asBinaryExpr();
        if (!comparison.getOperator().equals(BinaryExpr.Operator.NOT_EQUALS))
            return false;
        return comparison.getLeft().isNullLiteralExpr() && comparison.getRight().isNameExpr() && comparison.getRight().asNameExpr().toString().equals(field.getName()) ||
                comparison.getRight().isNullLiteralExpr() && comparison.getLeft().isNameExpr() && comparison.getLeft().asNameExpr().toString().equals(field.getName());
    }

}
