package org.meier.check.visitor;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.meier.model.ClassMeta;
import org.meier.model.MethodMeta;
import org.meier.model.Modifier;

import java.util.ArrayList;
import java.util.List;

public class InnerClassVisitor extends VoidVisitorAdapter<ClassMeta> {

    @Override
    public void visit(ClassOrInterfaceDeclaration n, ClassMeta cls) {
        ClassMeta clazz = cls;
        if (n.isNestedType() || n.isLocalClassDeclaration()) {
            List<Modifier> modifiersList = new ArrayList<>();
            n.accept(new ModifierVisitor(), modifiersList);
            clazz = new ClassMeta(n.resolve().asReferenceType().getQualifiedName(), modifiersList);
            n.accept(new FieldVisitor(), clazz);
            n.accept(new MethodVisitor(), clazz);
            clazz.getMethods().forEach(MethodMeta::resolveCalledMethods); // TODO fix it - won't work
            cls.getInnerClasses().add(clazz);
        }
        super.visit(n, clazz);
    }
}
