package org.meier.build.visitor;

import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.meier.model.EnumConstantMeta;
import org.meier.model.EnumMeta;
import org.meier.model.MetaHolder;

import java.util.Collections;

public class EnumConstVisitor extends VoidVisitorAdapter<EnumMeta> {

    @Override
    public void visit(EnumConstantDeclaration n, EnumMeta parent) {
        EnumConstantMeta enumMeta = new EnumConstantMeta(parent.getFullName()+"."+n.resolve().asEnumConstant().getName(), Collections.emptyList(), true);
        parent.addEnumConstant(enumMeta);
        MetaHolder.addClass(enumMeta);
        enumMeta.setStartLine(n.getBegin().get().line);
        n.accept(new FieldVisitor(), enumMeta);
        n.accept(new InnerClassVisitor(), enumMeta);
        n.accept(new MethodVisitor(), enumMeta);
    }
}
