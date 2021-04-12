package org.meier.check.visitor;

import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;

import java.util.List;

public class TypeVisitor extends VoidVisitorAdapter<List<String>> {

    @Override
    public void visit(ClassOrInterfaceType n, List<String> types) {
        String typeStr = n.resolve().asReferenceType().toString();
        types.add(typeStr);
    }
}
