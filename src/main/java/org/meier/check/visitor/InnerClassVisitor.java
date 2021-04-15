package org.meier.check.visitor;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.meier.model.ClassMeta;
import org.meier.model.MethodMeta;

public class InnerClassVisitor extends VoidVisitorAdapter<ClassMeta> {

    @Override
    public void visit(ClassOrInterfaceDeclaration n, ClassMeta cls) {
        ClassMeta clazz = cls;
        if (n.isNestedType() || n.isLocalClassDeclaration()) {
            clazz = new ClassMeta(n.resolve().asReferenceType().getQualifiedName());
            n.accept(new FieldVisitor(), clazz);
            n.accept(new MethodVisitor(), clazz);
            clazz.getMethods().forEach(MethodMeta::resolveCalledMethods); // TODO fix it - won't work
            cls.getInnerClasses().add(clazz);
        }
        super.visit(n, clazz);
    }
}
