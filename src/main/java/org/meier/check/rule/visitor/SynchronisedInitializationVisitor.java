package org.meier.check.rule.visitor;

import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import org.meier.check.rule.util.ClassMetaInfo;
import org.meier.model.FieldMeta;

public class SynchronisedInitializationVisitor extends GenericVisitorAdapter<Boolean, FieldMeta> {

    @Override
    public Boolean visit(IfStmt n, FieldMeta arg) {
        return ClassMetaInfo.isNullComparison(n, arg) && n.getThenStmt().accept(new SynchronisedBlockVisitor(), arg) ||
                ClassMetaInfo.isNotNullComparison(n, arg) && n.getElseStmt().isPresent() && n.getElseStmt().get().accept(new SynchronisedBlockVisitor(), arg);
    }
}
