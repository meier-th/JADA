package org.meier.check.visitor;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.meier.bean.NameTypeBean;
import org.meier.model.ClassMeta;
import org.meier.model.FieldMeta;
import org.meier.model.Modifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FieldVisitor extends VoidVisitorAdapter<ClassMeta> {

    @Override
    public void visit(FieldDeclaration fieldDecl, ClassMeta classMeta) {
        try {
            List<Modifier> modifiersList = new ArrayList<>();
            List<NameTypeBean> names = new ArrayList<>();
            fieldDecl.accept(new NameTypeVisitor(), names);
            fieldDecl.accept(new ModifierVisitor(), modifiersList);
            Set<Modifier> modifiers = Set.copyOf(modifiersList);
            classMeta.getFields().addAll(names.stream()
                    .map(nameTypeBean -> new FieldMeta(nameTypeBean.getName(), nameTypeBean.getFullClassName(), modifiers, classMeta))
                    .collect(Collectors.toList()));
        } catch (Exception error) {
            // some log
        }
    }
}
