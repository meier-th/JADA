package org.meier.build.visitor;

import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.body.*;
import com.github.javaparser.ast.visitor.GenericListVisitorAdapter;
import org.meier.model.Modifier;

import java.util.List;

public class ModifierVisitor extends GenericListVisitorAdapter<Modifier, ModifierVisitor.ModifierLevel> {

    public enum ModifierLevel {
        CLASS,
        METHOD,
        FIELD,
        ENUM,
        CODE_BLOCK,
        CONSTRUCTOR
    }

    @Override
    public List<Modifier> visit(com.github.javaparser.ast.Modifier n, ModifierLevel lvl) {
        List<Modifier> modifiers = super.visit(n, lvl);
        Node node = n.getParentNode().orElse(null);
        if (node instanceof ClassOrInterfaceDeclaration && lvl == ModifierLevel.CLASS ||
            node instanceof MethodDeclaration && lvl == ModifierLevel.METHOD ||
            node instanceof FieldDeclaration && lvl == ModifierLevel.FIELD ||
            node instanceof EnumDeclaration && lvl == ModifierLevel.ENUM ||
            node instanceof InitializerDeclaration && lvl == ModifierLevel.CODE_BLOCK ||
            node instanceof ConstructorDeclaration && lvl == ModifierLevel.CONSTRUCTOR)
            modifiers.add(Modifier.toModifier(n.getKeyword()));
        return modifiers;
    }
}
