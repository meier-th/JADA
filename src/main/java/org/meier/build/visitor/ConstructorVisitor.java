package org.meier.build.visitor;

import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import org.meier.build.util.TypeResolver;
import org.meier.model.ClassMeta;
import org.meier.model.ConstructorMeta;
import org.meier.model.Modifier;
import org.meier.model.Parameter;

import java.util.List;
import java.util.stream.Collectors;

public class ConstructorVisitor extends VoidVisitorAdapter<ClassMeta> {

    @Override
    public void visit(ConstructorDeclaration n, ClassMeta classMeta) {
        List<Modifier> modifiersList = n.accept(new ModifierVisitor(), ModifierVisitor.ModifierLevel.CONSTRUCTOR);
        try {
            String fullName = n.resolve().getQualifiedName();
            if (fullName.substring(0, fullName.lastIndexOf('.')).equals(classMeta.getFullName())) {

                List<Parameter> parameters = n.getParameters().stream().map(param ->
                        new Parameter(param.getNameAsString(), TypeResolver.getQualifiedName(param.getType()), n.getTypeParameters(), param.getType())
                ).collect(Collectors.toList());

                ConstructorMeta constructor = new ConstructorMeta(classMeta, modifiersList, parameters, n);
                classMeta.addConstructor(constructor);
            }
        } catch (Exception ignored) {}
    }
}
