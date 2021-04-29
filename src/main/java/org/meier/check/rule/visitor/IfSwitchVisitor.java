package org.meier.check.rule.visitor;

import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.SwitchStmt;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;

import java.util.List;

public class IfSwitchVisitor extends GenericListVisitorAdapter<Object, Void> {

    @Override
    public List<Object> visit(IfStmt n, Void arg) {
        List<Object> objs = super.visit(n, arg);
        objs.add(new Object());
        return objs;
    }

    @Override
    public List<Object> visit(SwitchStmt n, Void arg) {
        List<Object> objs = super.visit(n, arg);
        objs.add(new Object());
        return objs;
    }
}
