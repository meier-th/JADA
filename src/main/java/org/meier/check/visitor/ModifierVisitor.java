package org.meier.check.visitor;

import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.meier.model.Modifier;

import java.util.List;

public class ModifierVisitor extends VoidVisitorAdapter<List<Modifier>> {

    @Override
    public void visit(com.github.javaparser.ast.Modifier n, List<Modifier> arg) {
        super.visit(n, arg);
        arg.add(Modifier.toModifier(n.getKeyword()));
    }
}
