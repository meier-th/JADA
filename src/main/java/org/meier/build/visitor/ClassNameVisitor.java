package org.meier.build.visitor;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;

public class ClassNameVisitor extends GenericVisitorAdapter<String, boolean[]> {

    @Override
    public String visit(ClassOrInterfaceDeclaration n, boolean[] arg) {
        try {
            arg[0] = n.isInterface();
            return n.resolve().asReferenceType().getQualifiedName();
        } catch (Exception error) {
            return "";
        }
    }
}
