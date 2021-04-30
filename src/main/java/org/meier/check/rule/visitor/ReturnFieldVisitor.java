package org.meier.check.rule.visitor;

import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import org.meier.model.FieldMeta;

public class ReturnFieldVisitor extends GenericVisitorAdapter<Boolean, FieldMeta> {

    @Override
    public Boolean visit(ReturnStmt n, FieldMeta arg) {
        return n.toString().replaceAll(" ", "").equals("return"+arg.getName()+";");
    }

}
