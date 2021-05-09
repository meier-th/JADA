package org.meier.check.rule.util;

import com.github.javaparser.ast.expr.BinaryExpr;
import com.github.javaparser.ast.stmt.IfStmt;
import org.meier.check.rule.visitor.ReturnFieldVisitor;
import org.meier.check.rule.visitor.SetFieldVisitor;
import org.meier.model.ClassMeta;
import org.meier.model.FieldMeta;
import org.meier.model.MethodMeta;

import java.util.HashSet;
import java.util.Optional;
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
        return cls.getMethods().stream().anyMatch(meth -> isSetter(meth) && meth.getFullQualifiedReturnType().equals(cls.getFullName()));
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

    public static boolean isGetter(MethodMeta method) {
        if (!method.getParameters().isEmpty())
            return false;
        if (!method.getShortName().startsWith("get") && !method.getShortName().startsWith("is"))
            return false;
        Optional<FieldMeta> field = method.getOwnerClass().getFields().stream().filter(fld ->
                fld.getFullClassName().equals(method.getFullQualifiedReturnType()) &&
                        fld.getName().endsWith(method.getShortName().substring(3))
        ).findAny();
        if (field.isEmpty())
            return false;
        Boolean returnsField = method.getContent().accept(new ReturnFieldVisitor(), field.get());
        return returnsField != null && returnsField;
    }

    public static boolean isSetter(MethodMeta method) {
        if (!(method.getParameters().size() == 1))
            return false;
        if (!(method.getFullQualifiedReturnType().equals("void") ||
                method.getFullQualifiedReturnType().equals("java.lang.Void") ||
                method.getFullQualifiedReturnType().equals(method.getOwnerClass().getFullName())))
            return false;
        Optional<FieldMeta> field = method.getOwnerClass().getFields().stream().filter(fld ->
                fld.getFullClassName().equals(method.getParameters().get(0).getTypeName()) &&
                        fld.getName().equalsIgnoreCase(method.getShortName().substring(3))
        ).findAny();
        if (field.isEmpty())
            return false;
        Boolean setsField = method.getContent().accept(new SetFieldVisitor(), field.get());
        return setsField != null && setsField;
    }

    public static boolean isGetterOrSetter(MethodMeta method) {
        return isGetter(method) || isSetter(method);
    }

}
