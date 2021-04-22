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
            List<Modifier> modifiersList = fieldDecl.accept(new ModifierVisitor(), ModifierVisitor.ModifierLevel.FIELD);
            List<NameTypeBean> names = new ArrayList<>();
            fieldDecl.accept(new NameTypeVisitor(), names);
            Set<Modifier> modifiers = Set.copyOf(modifiersList);
            classMeta.getFields().addAll(names.stream()
                    .filter(name -> name.getClassName().equals(classMeta.getFullName()))
                    .map(nameTypeBean -> new FieldMeta(nameTypeBean.getName(), nameTypeBean.getFullClassName(), modifiers, classMeta))
                    .collect(Collectors.toList()));
        } catch (Exception error) {
            // some log
        }
    }
}
