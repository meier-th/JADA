package org.meier.build.visitor;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.TypeDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.meier.model.ClassMeta;
import org.meier.model.EnumMeta;
import org.meier.model.MetaHolder;
import org.meier.model.Modifier;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InnerClassVisitor extends VoidVisitorAdapter<ClassMeta> {

    private static final Map<ClassMeta, TypeDeclaration<?>> innerClassesAstNodes = new LinkedHashMap<>();

    public static void runInnerClassesMethodVisitors() {
        innerClassesAstNodes.forEach((cls, astNode) -> astNode.accept(new MethodVisitor(), cls));
    }

    @Override
    public void visit(EnumDeclaration n, ClassMeta cls) {
        List<Modifier> modifiers = n.accept(new ModifierVisitor(), ModifierVisitor.ModifierLevel.ENUM);
        EnumMeta clazz = new EnumMeta(n.resolve().asEnum().getQualifiedName(), modifiers, true);
        clazz.setImplementedInterfaces(n.getImplementedTypes().stream().map(type -> type.resolve().getQualifiedName()).collect(Collectors.toList()));
        MetaHolder.addClass(clazz);
        clazz.setStartLine(n.getBegin().get().line);
        n.accept(new FieldVisitor(), clazz);
        n.accept(new EnumConstVisitor(), clazz);
        n.accept(new InitializerBlocksVisitor(), clazz);
        innerClassesAstNodes.put(clazz, n);
        cls.getInnerClasses().add(clazz);
        super.visit(n, clazz);
    }

    @Override
    public void visit(ClassOrInterfaceDeclaration n, ClassMeta cls) {
        ClassMeta clazz = cls;
        if (n.isNestedType() || n.isLocalClassDeclaration()) {
            List<Modifier> modifiersList = n.accept(new ModifierVisitor(), ModifierVisitor.ModifierLevel.CLASS);
            clazz = new ClassMeta(n.resolve().asReferenceType().getQualifiedName(), modifiersList, true);
            clazz.setExtendedClasses(n.getExtendedTypes().stream().map(type -> type.resolve().getQualifiedName()).collect(Collectors.toList()));
            clazz.setImplementedInterfaces(n.getImplementedTypes().stream().map(type -> type.resolve().getQualifiedName()).collect(Collectors.toList()));
            MetaHolder.addClass(clazz);
            clazz.setStartLine(n.getBegin().get().line);
            n.accept(new FieldVisitor(), clazz);
            n.accept(new InitializerBlocksVisitor(), clazz);
            innerClassesAstNodes.put(clazz, n);
            cls.getInnerClasses().add(clazz);
        }
        super.visit(n, clazz);
    }
}