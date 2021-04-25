package org.meier.build.visitor;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;
import org.meier.model.Modifier;

import java.util.List;

public class ModifierVisitor extends GenericListVisitorAdapter<Modifier, ModifierVisitor.ModifierLevel> {

    public enum ModifierLevel {
        CLASS,
        METHOD,
        FIELD,
        ENUM
    }

    @Override
    public List<Modifier> visit(com.github.javaparser.ast.Modifier n, ModifierLevel lvl) {
        List<Modifier> modifiers = super.visit(n, lvl);
        Node node = n.getParentNode().orElse(null);
        if (node instanceof ClassOrInterfaceDeclaration && lvl == ModifierLevel.CLASS ||
            node instanceof MethodDeclaration && lvl == ModifierLevel.METHOD ||
            node instanceof FieldDeclaration && lvl == ModifierLevel.FIELD ||
            node instanceof EnumDeclaration && lvl == ModifierLevel.ENUM)
            modifiers.add(Modifier.toModifier(n.getKeyword()));
        return modifiers;
    }
}
