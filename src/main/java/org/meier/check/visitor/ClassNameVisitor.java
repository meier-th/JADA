package org.meier.check.visitor;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;

public class ClassNameVisitor extends GenericVisitorAdapter<String, Void> {

    @Override
    public String visit(ClassOrInterfaceDeclaration n, Void arg) {
        try {
            return n.resolve().asReferenceType().getQualifiedName();
        } catch (Exception error) {
            return "";
        }
    }
}
