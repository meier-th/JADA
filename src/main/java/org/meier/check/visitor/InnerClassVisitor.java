package org.meier.check.visitor;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.meier.model.ClassMeta;
import org.meier.model.MetaHolder;
import org.meier.model.Modifier;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class InnerClassVisitor extends VoidVisitorAdapter<ClassMeta> {

    private static final Map<ClassMeta, ClassOrInterfaceDeclaration> innerClassesAstNodes = new LinkedHashMap<>();

    public static void runInnerClassesMethodVisitors() {
        innerClassesAstNodes.forEach((cls, astNode) -> astNode.accept(new MethodVisitor(), cls));
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, ClassMeta cls) {
        ClassMeta clazz = cls;
        if (n.isNestedType() || n.isLocalClassDeclaration()) {
            List<Modifier> modifiersList = new ArrayList<>();
            n.accept(new ModifierVisitor(), modifiersList);
            clazz = new ClassMeta(n.resolve().asReferenceType().getQualifiedName(), modifiersList, true);
            n.accept(new FieldVisitor(), clazz);
            innerClassesAstNodes.put(clazz, n);
            cls.getInnerClasses().add(clazz);
        }
        super.visit(n, clazz);
    }
}
