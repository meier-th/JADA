package org.meier.check.visitor;

import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.visitor.GenericVisitorAdapter;
import org.meier.bean.NameTypeBean;
import org.meier.model.FieldMeta;
import org.meier.model.Modifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class FieldsVisitor extends GenericVisitorAdapter<List<FieldMeta>, Void> {

    @Override
    public List<FieldMeta> visit(FieldDeclaration fieldDecl, Void arg) {
        try {
            List<Modifier> modifiersList = new ArrayList<>();
            List<NameTypeBean> names = new ArrayList<>();
            fieldDecl.accept(new NameTypeVisitor(), names);
            fieldDecl.accept(new ModifierVisitor(), modifiersList);
            Set<Modifier> modifiers = Set.copyOf(modifiersList);
            return names.stream()
                    .map(nameTypeBean -> new FieldMeta(nameTypeBean.getName(), nameTypeBean.getFullClassName(), modifiers))
                    .collect(Collectors.toList());
        } catch (Exception error) {
            return Collections.emptyList();
        }
    }
}
