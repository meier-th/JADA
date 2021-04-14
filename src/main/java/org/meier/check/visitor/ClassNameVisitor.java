package org.meier.check.visitor;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;

import java.util.List;

public class ClassNameVisitor extends GenericListVisitorAdapter<String, Void> {

    @Override
    public List<String> visit(ClassOrInterfaceDeclaration n, Void arg) {
        return List.of(n.resolve().asReferenceType().getQualifiedName());
    }
}
