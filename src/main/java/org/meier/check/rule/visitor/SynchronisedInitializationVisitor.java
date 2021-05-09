package org.meier.check.rule.visitor;

import com.github.javaparser.ast.stmt.IfStmt;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import org.meier.check.rule.util.ClassMetaInfo;
import org.meier.model.FieldMeta;

public class SynchronisedInitializationVisitor extends GenericVisitorAdapter<Boolean, FieldMeta> {

    @Override
    public Boolean visit(IfStmt n, FieldMeta arg) {
            if (ClassMetaInfo.isNullComparison(n, arg)) {
                Boolean synchBlockVisitorResult = n.getThenStmt().accept(new SynchronisedBlockVisitor(), arg);
                return synchBlockVisitorResult != null && synchBlockVisitorResult;
            }
            if (ClassMetaInfo.isNotNullComparison(n, arg) && n.getElseStmt().isPresent()) {
                Boolean synchBlockVisitorResult = n.getElseStmt().get().accept(new SynchronisedBlockVisitor(), arg);
                return synchBlockVisitorResult != null && synchBlockVisitorResult;
            }
            return false;
    }
}
