package org.meier.check.rule.visitor;

import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import org.meier.check.rule.util.ClassMetaInfo;
import org.meier.model.FieldMeta;
import org.meier.model.MetaHolder;

public class CreateIfNullVisitor extends GenericVisitorAdapter<Boolean, FieldMeta> {

    @Override
    public Boolean visit(IfStmt n, FieldMeta arg) {
        Statement thenStatement = n.getThenStmt();
        Boolean thenHasReturnStatement = thenStatement.accept(new ReturnFieldVisitor(), arg);
        return ClassMetaInfo.isNullComparison(n, arg) && thenStatement.accept(new ObjectCreationVisitor(), MetaHolder.getClass(arg.getFullClassName())).size() > 0 ||
                ClassMetaInfo.isNotNullComparison(n, arg) && thenStatement.accept(new ObjectCreationVisitor(), MetaHolder.getClass(arg.getFullClassName())).size() == 0 &&
                        (thenHasReturnStatement != null && thenHasReturnStatement
                                || n.getElseStmt().isPresent() &&
                                n.getElseStmt().get().accept(new ObjectCreationVisitor(), MetaHolder.getClass(arg.getFullClassName())).size() > 0);
    }

}
