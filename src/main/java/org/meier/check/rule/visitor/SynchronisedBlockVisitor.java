package org.meier.check.rule.visitor;

import com.github.javaparser.ast.stmt.SynchronizedStmt;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import org.meier.model.FieldMeta;

public class SynchronisedBlockVisitor extends GenericVisitorAdapter<Boolean, FieldMeta> {

    @Override
    public Boolean visit(SynchronizedStmt n, FieldMeta arg) {
        return n.accept(new CreateIfNullVisitor(), arg);
    }

}
