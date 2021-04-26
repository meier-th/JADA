package org.meier.build.visitor;

import com.github.javaparser.ast.body.InitializerDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.meier.model.ClassMeta;
import org.meier.model.CodeBlockMeta;
import org.meier.model.Modifier;

import java.util.List;

public class InitializerBlocksVisitor extends VoidVisitorAdapter<ClassMeta> {

    @Override
    public void visit(InitializerDeclaration n, ClassMeta arg) {
        List<Modifier> modifiersList = n.accept(new ModifierVisitor(), ModifierVisitor.ModifierLevel.CODE_BLOCK);
        CodeBlockMeta block = CodeBlockMeta.newInstance()
                .setStaticBlock(modifiersList.contains(Modifier.STATIC))
                .setCode(n)
                .setStartLine(n.getBegin().get().line);
        n.accept(new VariablesVisitor(), block);
        arg.addCodeBlock(block);
    }
}
